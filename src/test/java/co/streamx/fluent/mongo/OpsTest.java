package co.streamx.fluent.mongo;

import static co.streamx.fluent.mongo.grammar.FluentFilters.elemMatch;
import static co.streamx.fluent.mongo.grammar.FluentFilters.eq;
import static co.streamx.fluent.mongo.grammar.FluentProjections.*;
import static co.streamx.fluent.mongo.grammar.FluentSorts.ascending;
import static co.streamx.fluent.mongo.grammar.FluentUpdates.combine;
import static co.streamx.fluent.mongo.grammar.FluentUpdates.currentDate;
import static co.streamx.fluent.mongo.grammar.FluentUpdates.set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class OpsTest implements TutorialTypes, CommonTest {

    private static final QueryBuilder<Restaurant> FLUENT = FluentMongo.queryBuilder(Restaurant.class);
    private static final MongoCollection<?> collection = mock(MongoCollection.class);
    private static final FindIterable<?> find = mock(FindIterable.class);

    @BeforeAll
    public static void beforeAll() {
        when(find.projection(any(Bson.class))).then(in -> in.getMock());
        when(find.sort(any(Bson.class))).then(in -> in.getMock());
        when(collection.find(any(Bson.class))).then(in -> find);
        when(collection.updateOne(any(Bson.class), any(Bson.class))).then(in -> null);
    }

    @Test
    public void finds() {

        Bson filter = FLUENT.filter(r -> r.getName() == "456 Cookies Shop");

        assertQuery(filter, "{\"name\": \"456 Cookies Shop\"}");

        collection.find(filter);

        filter = FLUENT.filter(
                r -> r.getStars() >= 2 && r.getStars() < 5 && elemMatch(r.getCategories(), cat -> cat.equals("Bakery"))
                        && elemMatch(r.getResults(), re -> re >= 80 && re < 85));
        try {
            assertQuery(filter,
                    "{\"stars\": {\"$gte\": 2, \"$lt\": 5}, \"categories\": {\"$elemMatch\": {\"$eq\": \"Bakery\"}}, \"results\": {\"$elemMatch\": {\"$gte\": 80, \"$lt\": 85}}}");

        } catch (AssertionError e) {
            assertQuery(filter,
                    "{\"$and\": [{\"stars\": {\"$gte\": 2}}, {\"$and\": [{\"stars\": {\"$lt\": 5}}, {\"$and\": [{\"categories\": {\"$elemMatch\": {\"$eq\": \"Bakery\"}}}, {\"results\": {\"$elemMatch\": {\"$and\": [{\"$gte\": 80}, {\"$lt\": 85}]}}}]}]}]}");
        }
        collection.find(filter);

        List<String> categories = Arrays.asList("Bakery", "Pharm");

        filter = FLUENT.filter(r -> r.getCategories().containsAll(categories) && r.getResults().contains(3));
        Bson order = FLUENT.sort(r -> ascending(r.getName()));

        Bson computed = Projections.computed(
                "firstCategory",
                new Document("$arrayElemAt", Arrays.asList("$categories", 0))
        );

        Bson projection = FLUENT
                .project(r -> fields(include(r.getName(), r.getStars(), r.getCategories()),
                        computed(computed), excludeId()));

        try {
            assertQuery(filter, "{\"categories\": {\"$all\": [\"Bakery\", \"Pharm\"]}, \"results\": 3}");

        } catch (AssertionError e) {
            assertQuery(filter,
                    "{\"$and\": [{\"categories\": {\"$all\": [\"Bakery\", \"Pharm\"]}}, {\"results\": 3}]}");
        }
        assertQuery(order, "{\"name\": 1}");
        assertQuery(projection, "{\"name\": 1, \"stars\": 1, \"categories\": 1, \"firstCategory\": {\"$arrayElemAt\": [\"$categories\", 0]}, \"_id\": 0}");

        collection.find(filter).sort(order).projection(projection);
    }

    @Test
    public void updates() {
        Bson filter = FLUENT.filter(r -> eq("57506d62f57802807471dd41"));
        Bson update = FLUENT.update(r -> combine(set(r.getStars(), 1), set(r.getContact().getPhone(), "228-555-9999"),
                currentDate(r.getLastModified())));

        assertQuery(filter, "{\"_id\": \"57506d62f57802807471dd41\"}");
        assertQuery(update,
                "{\"$set\": {\"stars\": 1, \"contact.phone\": \"228-555-9999\"}, \"$currentDate\": {\"lastModified\": true}}");

        collection.updateOne(filter, update);
    }
}
