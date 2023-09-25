package co.streamx.fluent.mongo.grammar;

import co.streamx.fluent.mongo.functions.Function1;
import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Function;
import co.streamx.fluent.mongo.notation.Local;
import co.streamx.fluent.mongo.notation.ParamType;
import com.mongodb.client.model.mql.MqlArray;
import com.mongodb.client.model.mql.MqlNumber;
import com.mongodb.client.model.mql.MqlValue;

@Function(factory = MqlArray.class, isStatic = false)
public interface FluentMqlArray {
    static <TItem, T extends MqlValue> long sum(@FieldName(type = MqlArray.class, factoryMethod = "getArray") TItem[] field,
                                                @Local @ParamType(java.util.function.Function.class) Function1<? super T, ? extends MqlNumber> mapper) {
        throw new UnsupportedOperationException();
    }

    static <TItem, T extends MqlValue> long sum(@FieldName(type = MqlArray.class, factoryMethod = "getArray") Iterable<TItem> field,
                                                @Local @ParamType(java.util.function.Function.class) Function1<? super T, ? extends MqlNumber> mapper) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> long sum(Iterable<TItem> field) {
        return sum(field, v -> (MqlNumber) v);
    }
}
