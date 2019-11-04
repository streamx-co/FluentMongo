package co.streamx.fluent.mongo.functions;

import java.io.Serializable;
import java.util.function.BiFunction;

@FunctionalInterface
public interface Function2<T, U, R> extends BiFunction<T, U, R>, Serializable {

    default Function2<T, U, Boolean> and(Function2<T, U, Boolean> other) {
        return (t,
                u) -> (Boolean) apply(t, u) && other.apply(t, u);
    }

    default Function2<T, U, Boolean> or(Function2<T, U, Boolean> other) {
        return (t,
                u) -> (Boolean) apply(t, u) || other.apply(t, u);
    }
}
