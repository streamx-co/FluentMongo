package co.streamx.fluent.mongo;

import org.bson.conversions.Bson;

import co.streamx.fluent.mongo.functions.Function1;
import co.streamx.fluent.mongo.grammar.Index;
import co.streamx.fluent.mongo.grammar.Projection;
import co.streamx.fluent.mongo.grammar.Sort;
import co.streamx.fluent.mongo.grammar.Update;

public interface TypedCollection<T> {
    default Bson filter(Function1<T, Boolean> predicate) {
        return FluentMongo.process(predicate, new FilterInterpreter());
    }

    default Bson project(Function1<T, Projection> projection) {
        return FluentMongo.process(projection, new FilterInterpreter());
    }

    default Bson sort(Function1<T, Sort> sort) {
        return FluentMongo.process(sort, new GenericInterpreter());
    }

    default Bson update(Function1<T, Update> update) {
        if (!FluentMongo.checkLicense())
            throw TranslationError.REQUIRES_LICENSE.getError("UPDATE functionality");
        return FluentMongo.process(update, new FilterInterpreter());
    }

    default Bson index(Function1<T, Index> index) {
        return FluentMongo.process(index, new GenericInterpreter());
    }
}
