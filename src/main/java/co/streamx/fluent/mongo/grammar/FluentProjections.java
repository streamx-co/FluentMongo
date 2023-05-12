package co.streamx.fluent.mongo.grammar;

import java.util.Collection;

import co.streamx.fluent.mongo.notation.*;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Projections;

import co.streamx.fluent.mongo.functions.Function1;

/**
 * Strongly type-safe mappings (forward) to Mongo native {@link Projections} helper methods.
 */
@Function(factory = Projections.class)
public interface FluentProjections {

    /**
     * Fallback for the case when the type safe variant cannot be built.
     * @param computed Prebuilt computed field Document
     */
    @Function(factory = Object.class, passThrough = true)
    static Projection computed(@Local Bson computed) {
        throw new UnsupportedOperationException();
    }

    static <TItem, TExpression> Projection computed(@FieldName TItem field, @ParamType(Object.class) @NestedExpression TExpression expression) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection computedSearchMeta(@FieldName TItem field) {
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

    static <TItem> Projection metaSearchScore(@FieldName TItem field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Projection metaSearchHighlights(@FieldName TItem field) {
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
