package co.streamx.fluent.mongo;

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

    default Bson sort(Function1<T, Sort> sort) {
        return FluentMongo.process(sort, new GenericInterpreter());
    }

    default Bson update(Function1<T, Update> update) {
        return FluentMongo.process(update, new FilterInterpreter());
    }

    default Bson index(Function1<T, Index> index) {
        return FluentMongo.process(index, new GenericInterpreter());
    }
}
