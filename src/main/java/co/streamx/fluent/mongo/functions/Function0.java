package co.streamx.fluent.mongo.functions;

import java.io.Serializable;
import java.util.function.Supplier;

@FunctionalInterface
public interface Function0<T> extends Supplier<T>, Serializable {

    default Function0<Boolean> and(Function0<Boolean> other) {
        return () -> (Boolean) get() && other.get();
    }

    default Function0<Boolean> or(Function0<Boolean> other) {
        return () -> (Boolean) get() || other.get();
    }
}
