package co.streamx.fluent.mongo;

import co.streamx.fluent.extree.expression.LambdaExpression;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Function;

final class LambdaInvocationHandler implements InvocationHandler {

    private final Function<Object[], ?> compiled;

    public LambdaInvocationHandler(LambdaExpression<?> lambdaExpression) {

        compiled = lambdaExpression.compile();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.isDefault() || method.getDeclaringClass().equals(Object.class)) {
            //return method.invoke(proxy, args);
            throw new UnsupportedOperationException();
        }

        return compiled.apply(args);
    }
}
