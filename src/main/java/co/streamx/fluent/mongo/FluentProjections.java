package co.streamx.fluent.mongo;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Projections;

import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Function;

@Function(factory = Projections.class)
public interface FluentProjections {
    @SafeVarargs
    static <TItem> Projection include(@FieldName final TItem... field) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    static <TItem> Projection exclude(@FieldName final TItem... field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection excludeId() {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection elemMatch(@FieldName final TItem field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection elemMatch(@FieldName final TItem field,
                          final Bson filter) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection metaTextScore(@FieldName final TItem field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection slice(@FieldName final TItem field,
                      final int limit) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection slice(@FieldName final TItem field,
                      final int skip,
                      final int limit) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection fields(final Projection... projections) {
        throw new UnsupportedOperationException();
    }
}
