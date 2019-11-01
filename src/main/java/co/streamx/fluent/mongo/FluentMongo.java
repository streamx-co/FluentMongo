package co.streamx.fluent.mongo;

import org.bson.conversions.Bson;

import co.streamx.fluent.extree.expression.LambdaExpression;
import co.streamx.fluent.mongo.functions.Function1;

public final class FluentMongo {

    private static final TypedCollection<?> typed = new TypedCollection<Object>() {
    };
//
//    public static <T> Bson filter(Function1<T, Boolean> predicate) {
//        GenericInterpreter interpreter = new FilterInterpreter();
//
//        return process(predicate, interpreter);
//    }

    @SuppressWarnings("unchecked")
    public static <T> TypedCollection<T> collection(T collection) {
        return (TypedCollection<T>) typed;
    }

    @SuppressWarnings("unchecked")
    public static <T> TypedCollection<T> collection(Class<T> collectionType) {
        return (TypedCollection<T>) typed;
    }

    static <T> Bson process(Function1<T, ?> lambda,
                            GenericInterpreter interpreter) {
        LambdaExpression<?> e = LambdaExpression.parse(lambda);
        interpreter.visit(e);

        return interpreter.popResult();
    }
}
