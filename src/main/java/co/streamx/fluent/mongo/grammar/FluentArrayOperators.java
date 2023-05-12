package co.streamx.fluent.mongo.grammar;

import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Function;

@Function(factory = ArrayOperators.class)
public interface FluentArrayOperators {

    static <TItem> TItem arrayElemAt(@FieldName TItem[] field, int index) {
        throw new UnsupportedOperationException();
    }

    static <TItem> TItem arrayElemAt(@FieldName Iterable<TItem> field, int index) {
        throw new UnsupportedOperationException();
    }
}
