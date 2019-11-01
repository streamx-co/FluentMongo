package co.streamx.fluent.mongo;

import org.bson.conversions.Bson;

import co.streamx.fluent.mongo.functions.Function1;

public interface TypedCollection<T> {
    default Bson filter(Function1<T, Boolean> predicate) {
        return FluentMongo.process(predicate, new FilterInterpreter());
    }
}
