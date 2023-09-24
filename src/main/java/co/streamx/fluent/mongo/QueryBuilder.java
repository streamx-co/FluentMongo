package co.streamx.fluent.mongo;

import co.streamx.fluent.mongo.functions.Function2;
import com.mongodb.client.model.mql.MqlDocument;
import com.mongodb.client.model.mql.MqlValue;
import org.bson.conversions.Bson;

import co.streamx.fluent.mongo.functions.Function1;
import co.streamx.fluent.mongo.grammar.FluentFilters;
import co.streamx.fluent.mongo.grammar.FluentProjections;
import co.streamx.fluent.mongo.grammar.Index;
import co.streamx.fluent.mongo.grammar.Projection;
import co.streamx.fluent.mongo.grammar.Sort;
import co.streamx.fluent.mongo.grammar.Update;

public interface QueryBuilder<T> {

    /**
     * Creates a Mongo filter expression using POJO properties, Java operators and {@link FluentFilters} helper methods.
     */
    default Bson filter(Function1<T, Boolean> predicate) {
        return FluentMongo.process(predicate, new FilterInterpreter());
    }

    /**
     * Creates a Mongo filter expression using POJO properties, Java operators and {@link FluentProjections} helper
     * methods.
     */
    default Bson project(Function1<T, Projection> projection) {
        return FluentMongo.process(projection, new FilterInterpreter());
    }

    default <PROJECTION> Bson project(Function2<T, PROJECTION, Projection> projection) {
        return FluentMongo.process(projection, new FilterInterpreter());
    }

    default Bson sort(Function1<T, Sort> sort) {
        return FluentMongo.process(sort, new BsonGenericInterpreter());
    }

    default Bson update(Function1<T, Update> update) {
        return FluentMongo.process(update, new FilterInterpreter());
    }

    default Bson index(Function1<T, Index> index) {
        return FluentMongo.process(index, new BsonGenericInterpreter());
    }

  /*  default MqlValue mql(Function1<T, ?> mql) {
        return FluentMongo.process(mql, new MqlInterpreter());
    }
*/
    default <R extends MqlValue> R mql(MqlDocument doc, Function1<T, ?> mql) {
        return (R) FluentMongo.process(mql, new MqlInterpreter(doc));
    }
}
