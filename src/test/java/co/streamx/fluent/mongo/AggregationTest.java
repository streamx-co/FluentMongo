package co.streamx.fluent.mongo;

import co.streamx.fluent.mongo.functions.Function1;
import com.mongodb.client.model.mql.*;
import lombok.Data;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.mql.MqlValues.*;
import static co.streamx.fluent.mongo.grammar.FluentMqlArray.*;
import static java.util.Arrays.asList;

//interface MqlDocument<T> extends com.mongodb.client.model.mql.MqlDocument {}

@Data
class ShowTime {
    private Instant date;
    private List<Integer> seats;
    private int ticketsBought;
}

@Data
class Movie {
    private Long id;
    private String movie;
    private List<ShowTime> showtimes;
}

public class AggregationTest implements CommonTest {

//    static <T> MqlArray<MqlDocument<T>> getArray(Function1<?, T> x) {
//        return null;
//    }

    public void testGrammar() {

    }

    @Test
    public void test1() {
        MqlInteger month =  current().getDate("date").month(of("UTC"));
        MqlInteger precip = current().getInteger("precipitation");
        List<Bson> list = asList(group(
                month,
                avg("avgPrecipMM", precip.multiply(25.4))
        ));

        CommonTest.print(list);

//        assertQuery(order, "{\"name\": 1}");
    }

    @Test
    public void test2() {
        Bson matchStage = match(eq("some_field", "some_criteria"));
        Bson sortByCountStage = sortByCount("some_field");
    }

    @Test
    public void test3() {
        current()
                .getArray("visitDates")
                .size()
                .gt(of(0))
                .and(current()
                        .getString("state")
                        .eq(of("New Mexico")));

        MqlArray<MqlDocument> showtimes = current().getArray("showtimes");
        asList(project(fields(
                computed("availableShowtimes", showtimes
                        .filter(showtime -> {
                            MqlArray<MqlInteger> seats = showtime.getArray("seats");
                            MqlNumber totalSeats = seats.sum(n -> n);
                            MqlInteger ticketsBought = showtime.getInteger("ticketsBought");
                            MqlBoolean isAvailable = ticketsBought.lt(totalSeats);
                            return isAvailable;
                        }))
        )));

        Predicate<ShowTime> p = (showtime) -> {
            Long totalSeats = sum(showtime.getSeats()); //, n -> (MqlNumber)n
            Integer ticketsBought = showtime.getTicketsBought();
            return ticketsBought < totalSeats;
        };

    }

    @Test
    public void test31() {

        MqlArray<MqlDocument> showtimes = current().getArray("showtimes");
        QueryBuilder<ShowTime> showtimeBuilder = FluentMongo.queryBuilder(ShowTime.class);

        List<Bson> list = asList(project(fields(
                computed("availableShowtimes", showtimes
                        .filter(showtimeDoc -> showtimeBuilder.mql(showtimeDoc, showtime -> {
                            Long totalSeats = sum(showtime.getSeats(),  n -> {
//                                System.out.println(n);
                                return (MqlNumber)n;
                            }); //, n -> (MqlNumber)n
                            Integer ticketsBought = showtime.getTicketsBought();
                            return ticketsBought < totalSeats;
                        }))
        ))));

        CommonTest.print(list);
    }

    @Test
    public void test4() {

        QueryBuilder<BasicTypes.Movie> person = FluentMongo.queryBuilder(BasicTypes.Movie.class);

        MqlValue mql = person.mql(current(), p -> p.getDirector().getBorn());


        MqlArray<MqlDocument> showtimes = current().getArray("showtimes");
        List<Bson> list = asList(project(fields(
                computed("availableShowtimes", mql)
        )));

        CommonTest.print(list);
    }

    @Test
    public void test5() {
        MqlString location = current().getString("location");
        asList(match(expr(location.eq(of("California")))));
    }
}
