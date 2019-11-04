package co.streamx.fluent.mongo;

import static co.streamx.fluent.mongo.grammar.FluentFilters.all;
import static co.streamx.fluent.mongo.grammar.FluentFilters.eq;

import java.util.Collection;

import org.bson.conversions.Bson;

import co.streamx.fluent.extree.expression.LambdaExpression;
import co.streamx.fluent.mongo.functions.Function1;
import co.streamx.fluent.mongo.functions.Function2;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FluentMongo {

    static {
        MongoConfig.registerMethodSubstitution(Object::equals, (a,
                                                                b) -> a == b);
        MongoConfig.registerMethodSubstitution(
                (Function2<Collection<Comparable>, Comparable, Boolean>) Collection::contains, (collection,
                                                                                                item) -> eq(collection,
                                                                                                        item));

        MongoConfig.registerMethodSubstitution(
                (Function2<Collection<Comparable>, Collection<Comparable>, Boolean>) Collection::containsAll,
                (collection,
                 items) -> all(collection, items));
    }

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
        e = (LambdaExpression<?>) Normalizer.get().visit(e);
        interpreter.visit(e);

        return interpreter.popResult();
    }
}
