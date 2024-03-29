package co.streamx.fluent.mongo.grammar;

import java.util.Collection;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Projections;

import co.streamx.fluent.mongo.functions.Function1;
import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Function;
import co.streamx.fluent.mongo.notation.NestedExpression;

/**
 *
 * Strongly type-safe mappings (forward) to Mongo native {@link Projections} helper methods.
 *
 */
@Function(factory = Projections.class)
public interface FluentProjections {

    @Function(factory = Object.class, passThrough = true)
    static Projection computed(Bson computed) {
        throw new UnsupportedOperationException();
    }

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
                                        @NestedExpression Function1<TItem, Boolean> filter) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection elemMatch(@FieldName Collection<TItem> field,
                                        Bson filter) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection meta(@FieldName TItem field, String metaFieldName) {
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

    static Projection fields(@NestedExpression Projection... projections) {
        throw new UnsupportedOperationException();
    }
}
