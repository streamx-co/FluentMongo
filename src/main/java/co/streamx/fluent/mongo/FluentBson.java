package co.streamx.fluent.mongo;

import org.bson.conversions.Bson;

import co.streamx.fluent.extree.expression.LambdaExpression;
import co.streamx.fluent.mongo.functions.Function1;

public class FluentBson {
    public static <T> Bson filter(Function1<T, Boolean> predicate) {
        GenericInterpreter interpreter = new GenericInterpreter();

        LambdaExpression<?> e = LambdaExpression.parse(predicate);
        interpreter.visit(e);

        return interpreter.popResult();
    }
}
