package co.streamx.fluent.mongo.grammar;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.lang.Nullable;

import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Function;
import co.streamx.fluent.mongo.notation.NestedExpression;

@Function(factory = Updates.class)
public interface FluentUpdates {

    static Update combine(final Update... updates) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update set(@FieldName final TItem field,
                              @Nullable final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update unset(@FieldName final TItem field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update setOnInsert(@FieldName final TItem field,
                                      @Nullable final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update inc(@FieldName final TItem field,
                                             final TItem number) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update mul(@FieldName final TItem field,
                                             final Number number) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update min(@FieldName final TItem field,
                                             final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update max(@FieldName final TItem field,
                                             final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Date> Update currentDate(@FieldName final TItem field) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Date> Update currentTimestamp(@FieldName final TItem field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update addToSet(@FieldName final Collection<TItem> field,
                                   @Nullable final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update addEachToSet(@FieldName final Collection<TItem> field,
                                       final List<TItem> values) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update push(@FieldName final Collection<TItem> field,
                               @Nullable final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update pushEach(@FieldName final Collection<TItem> field,
                                   final List<TItem> values) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update pushEach(@FieldName final Collection<TItem> field,
                                   final List<TItem> values,
                                   final PushOptions options) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update pull(@FieldName final Collection<TItem> field,
                               @Nullable final TItem value) {
        throw new UnsupportedOperationException();
    }

    static Update pullByFilter(@NestedExpression final boolean filter) {
        throw new UnsupportedOperationException();
    }

    static Update pullByFilter(final Bson filter) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update pullAll(@FieldName final Collection<TItem> field,
                                  final List<TItem> values) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update popFirst(@FieldName final Collection<TItem> field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> Update popLast(@FieldName final Collection<TItem> field) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update bitwiseAnd(@FieldName final TItem field,
                                                    final int value) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update bitwiseAnd(@FieldName final TItem field,
                                                    final long value) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update bitwiseOr(@FieldName final TItem field,
                                                   final int value) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update bitwiseOr(@FieldName final TItem field,
                                                   final long value) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update bitwiseXor(@FieldName final TItem field,
                                                    final int value) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> Update bitwiseXor(@FieldName final TItem field,
                                                    final long value) {
        throw new UnsupportedOperationException();
    }
}
