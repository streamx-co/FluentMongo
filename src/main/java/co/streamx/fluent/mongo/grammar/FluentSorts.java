package co.streamx.fluent.mongo.grammar;

import com.mongodb.client.model.Sorts;

import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Function;
import co.streamx.fluent.mongo.notation.NestedExpression;

@Function(factory = Sorts.class)
public interface FluentSorts {

    @SafeVarargs
    static <TItem> Sort ascending(@FieldName TItem... field) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    static <TItem> Sort descending(@FieldName TItem... field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Sort metaTextScore(@FieldName TItem field) {
        throw new UnsupportedOperationException();
    }

    static Sort orderBy(@NestedExpression Sort... sorts) {
        throw new UnsupportedOperationException();
    }
}
