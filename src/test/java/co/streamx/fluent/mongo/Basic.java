package co.streamx.fluent.mongo;

import static co.streamx.fluent.mongo.FluentFilters.and;
import static co.streamx.fluent.mongo.FluentFilters.elemMatch;
import static co.streamx.fluent.mongo.FluentFilters.filter;
import static co.streamx.fluent.mongo.FluentFilters.regex;
import static co.streamx.fluent.mongo.FluentProjections.include;

import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

public class Basic implements CommonTest, BasicTypes {

    @Test
    public void test1() {

        int born = 5;
        int born1 = 6;
        String string = "dfg";

        Bson filter = extractedTest1(born, born1, string);

        assertQuery(filter,
                "{ \"$or\" : [{ \"born\" : { \"$lt\" : 5 }, \"name\" : \"dfg\" }, { \"born\" : { \"$gte\" : 6 }, \"name\" : { \"$regex\" : \".*\", \"$options\" : \"\" } }] }");
    }

    private Bson extractedTest1(int born,
                                int born1,
                                String string) {
        
        TypedCollection<Person> person = FluentMongo.collection(Person.class);
        
        Bson filter = person.filter(p -> {
            return p.getBorn() < born && p.getName() == string || p.getBorn() >= born1 && regex(p.getName(), ".*", "");
        });
        return filter;
    }

    @Test
    public void test2() {

        int born = 5;
        String string = "dfg";

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        Bson filter = person.filter(p -> {
            return and(p.getBorn() < born, p.getName() == string); //
        });

        assertQuery(filter, "{ \"born\" : { \"$lt\" : 5 }, \"name\" : \"dfg\" }");
    }

    @Test
    public void test3() {

        String string = "dfg";

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        Bson filter = person.filter(p -> {
            return elemMatch(p.getActedMovies(), m -> m.getTagline() == string);
        });

        assertQuery(filter, "{ \"actedMovies\" : { \"$elemMatch\" : { \"tagline\" : \"dfg\" } } }");
    }

    @Test
    public void test4() {

        int born = 5;
        int born1 = 6;
        String string = "dfg";

        Bson f = extractedTest1(born, born1, string);

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        Bson ff = person.filter(p -> filter(f));

        assertQuery(ff,
                "{ \"$or\" : [{ \"born\" : { \"$lt\" : 5 }, \"name\" : \"dfg\" }, { \"born\" : { \"$gte\" : 6 }, \"name\" : { \"$regex\" : \".*\", \"$options\" : \"\" } }] }");
    }

    @Test
    public void testProjection() {

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        Bson filter = person.project(p -> {
            return include(p.getBorn(), p.getName());
        });

        assertQuery(filter, "{ \"born\" : 1, \"name\" : 1 }");
    }
}
