package co.streamx.fluent.mongo;

import java.util.Collection;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Projections;

import co.streamx.fluent.mongo.functions.Function1;
import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Filter;
import co.streamx.fluent.mongo.notation.Function;

@Function(factory = Projections.class)
public interface FluentProjections {
    @SafeVarargs
    static <TItem> Projection include(@FieldName TItem... field) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    static <TItem> Projection exclude(@FieldName TItem... field) {
        throw new UnsupportedOperationException();
    }

    static Projection excludeId() {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection elemMatch(@FieldName Collection<TItem> field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection elemMatch(@FieldName Collection<TItem> field,
                                        @Filter Function1<TItem, Boolean> filter) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection elemMatch(@FieldName Collection<TItem> field,
                                        Bson filter) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection metaTextScore(@FieldName TItem field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection slice(@FieldName Collection<TItem> field,
                                    int limit) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection slice(@FieldName Collection<TItem> field,
                                    int skip,
                                    int limit) {
        throw new UnsupportedOperationException();
    }

    static Projection fields(@Filter Projection... projections) {
        throw new UnsupportedOperationException();
    }
}
