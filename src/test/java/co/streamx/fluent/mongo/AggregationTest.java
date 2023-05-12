package co.streamx.fluent.mongo;

import com.mongodb.client.model.mql.*;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.mql.MqlValues.*;
import static java.util.Arrays.asList;


public class AggregationTest implements CommonTest {
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
    }

    @Test
    public void test4() {


        MqlArray<MqlDocument> showtimes = current().getArray("showtimes");
        List<Bson> list = asList(project(fields(
                computed("availableShowtimes", showtimes)
        )));

        CommonTest.print(list);
    }
}
