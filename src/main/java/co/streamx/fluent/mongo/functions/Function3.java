package co.streamx.fluent.mongo.functions;

import java.io.Serializable;

@FunctionalInterface
public interface Function3<T1, T2, T3, R> extends Functional, Serializable {
    R apply(T1 t1,
            T2 t2,
            T3 t3);

    default Function3<T1, T2, T3, Boolean> and(Function3<T1, T2, T3, Boolean> other) {
        return (t1,
                t2,
                t3) -> (Boolean) apply(t1, t2, t3) && other.apply(t1, t2, t3);
    }

    default Function3<T1, T2, T3, Boolean> or(Function3<T1, T2, T3, Boolean> other) {
        return (t1,
                t2,
                t3) -> (Boolean) apply(t1, t2, t3) || other.apply(t1, t2, t3);
    }
}
