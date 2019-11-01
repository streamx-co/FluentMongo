package co.streamx.fluent.mongo.functions;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface Function1<T, R> extends Function<T, R>, Serializable {

    default Function1<T, Boolean> and(Function1<T, Boolean> other) {
        return (t) -> (Boolean) apply(t) && other.apply(t);
    }

    default Function1<T, Boolean> or(Function1<T, Boolean> other) {
        return (t) -> (Boolean) apply(t) || other.apply(t);
    }
}
