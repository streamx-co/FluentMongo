package co.streamx.fluent.mongo;

import static co.streamx.fluent.mongo.grammar.FluentFilters.and;
import static co.streamx.fluent.mongo.grammar.FluentFilters.elemMatch;
import static co.streamx.fluent.mongo.grammar.FluentFilters.filter;
import static co.streamx.fluent.mongo.grammar.FluentIndexes.hashed;
import static co.streamx.fluent.mongo.grammar.FluentProjections.excludeId;
import static co.streamx.fluent.mongo.grammar.FluentProjections.fields;
import static co.streamx.fluent.mongo.grammar.FluentProjections.include;
import static co.streamx.fluent.mongo.grammar.FluentSorts.ascending;
import static co.streamx.fluent.mongo.grammar.FluentSorts.descending;
import static co.streamx.fluent.mongo.grammar.FluentSorts.orderBy;
import static co.streamx.fluent.mongo.grammar.FluentUpdates.pullByFilter;

import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

public class BasicTest implements CommonTest, BasicTypes {

    @Test
    public void test1() {

        int born = 5;
        int born1 = 6;
        String string = "dfg";

        Bson filter = extractedTest1(born, born1, string);

        assertQuery(filter,
                "{ \"$or\" : [{ \"born\" : { \"$lt\" : 5 }, \"name\" : \"dfg\" }, { \"born\" : { \"$gte\" : 6 } }] }");
        // , \"name\" : { \"$regex\" : \".*\", \"$options\" : \"\" }
    }

    private Bson extractedTest1(int born,
                                int born1,
                                String string) {
        
        TypedCollection<Person> person = FluentMongo.collection(Person.class);
        
        Bson filter = person.filter(p -> {
            return p.getBorn() < born && p.getName() == string || p.getBorn() >= born1; // && regex(p.getName(), ".*",
                                                                                        // "")
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
                "{ \"$or\" : [{ \"born\" : { \"$lt\" : 5 }, \"name\" : \"dfg\" }, { \"born\" : { \"$gte\" : 6 } }] }");
        // , \"name\" : { \"$regex\" : \".*\", \"$options\" : \"\" }
    }

    @Test
    public void testProjection() {

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        Bson filter = person.project(p -> {
            return include(p.getBorn(), p.getName());
        });

        assertQuery(filter, "{ \"born\" : 1, \"name\" : 1 }");
    }

    @Test
    public void testProjection1() {

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        Bson filter = person.project(p -> {
            return fields(include(p.getBorn(), p.getName()), excludeId());
        });

        assertQuery(filter, "{ \"born\" : 1, \"name\" : 1, \"_id\" : 0 }");
    }

    @Test
    public void testSort() {

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        Bson filter = person.sort(p -> ascending(p.getBorn()));

        assertQuery(filter, "{ \"born\" : 1 }");
    }

    @Test
    public void testSort1() {

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        Bson filter = person.sort(p -> orderBy(ascending(p.getBorn()), descending(p.getName())));

        assertQuery(filter, "{ \"born\" : 1, \"name\" : -1 }");
    }

    @Test
    public void testUpdate() {

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        String string = "xyz";
        Bson filter = person.update(p -> pullByFilter(elemMatch(p.getActedMovies(), m -> m.getTagline() == string)));

        assertQuery(filter, "{ \"$pull\" : { \"actedMovies\" : { \"$elemMatch\" : { \"tagline\" : \"xyz\" } } } }");
    }

    @Test
    public void testIndex() {

        TypedCollection<Person> person = FluentMongo.collection(Person.class);

        Bson filter = person.index(p -> hashed(p.getBorn()));

        assertQuery(filter, "{ \"born\" : \"hashed\" }");
    }
}
