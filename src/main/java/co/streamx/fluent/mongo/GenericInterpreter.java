package co.streamx.fluent.mongo;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import co.streamx.fluent.extree.expression.*;
import co.streamx.fluent.mongo.notation.*;

import lombok.SneakyThrows;

abstract class GenericInterpreter<T> extends SimpleExpressionVisitor {

    public T getResult(Class<?> resultType) {
        return bsons.pop();
    }

    protected abstract Class<T> getType();

    protected final Deque<T> bsons = new ArrayDeque<>();

    protected final Deque<String> paths = new ArrayDeque<>();
    protected final Deque<Object> constants = new ArrayDeque<>();

    @Override
    public Expression visit(ParameterExpression e) {
        List<Expression> contextArguments = getContextArguments();
        if (contextArguments != null) {
            List<Expression> args = popContextArguments();
            try {
                Expression expression = contextArguments.get(e.getIndex());
                expression.accept(this);
            } finally {
                pushContextArguments(args);
            }
        }
        return e;
    }

    private <X> Class<X[]> getArrayType(Class<X> clazz) {
        return (Class<X[]>) Array.newInstance(clazz, 0).getClass();
    }

    protected Object pollTarget(Class<?> type) {
        return paths.poll();
    }

    private static Expression removeCasts(Expression e) {
        while (e.getExpressionType() == ExpressionType.Convert) {
            e = ((UnaryExpression) e).getFirst();
        }
        return e;
    }

    @SneakyThrows
    @Override
    public Expression visit(MemberExpression e) {

        AnnotatedElement annotated = (AnnotatedElement) e.getMember();
        Function func = annotated.getAnnotation(Function.class);

        if (func == null)
            func = e.getMember().getDeclaringClass().getAnnotation(Function.class);

        if (func != null) {

            Method method = (Method) e.getMember();
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Annotation[][] parametersAnnotations = method.getParameterAnnotations();
            Object[] args = new Object[parameterTypes.length];

            List<Expression> currentArguments = popContextArguments();

            try {

                int varArg = method.isVarArgs() ? parametersAnnotations.length - 1 : -1;
                for (int i = 0; i < parametersAnnotations.length; i++) {

                    Annotation[] parameterAnnotations = parametersAnnotations[i];
                    int indexOfLocal = Lists.indexOf(parameterAnnotations,
                            a -> a instanceof Local);

                    Expression arg = removeCasts(currentArguments.get(i));
                    if (indexOfLocal >= 0) {
                        if (arg.getExpressionType() != ExpressionType.Parameter &&
                                arg.getExpressionType() != ExpressionType.Constant &&
                                !(arg.getExpressionType() == ExpressionType.Lambda && ((LambdaExpression<?>)arg).isMethodRef()))
                            throw TranslationError.REQUIRES_EXTERNAL_PARAMETER.getError(arg);
                    } else {
                        arg.accept(this);
                    }

                    int indexOfField = Lists.indexOf(parameterAnnotations,
                            a -> a instanceof FieldName);
                    if (indexOfField >= 0) {
                        FieldName field = (FieldName) parameterAnnotations[indexOfField];
                        if (i == varArg) {
                            parameterTypes[i] = Array.newInstance(field.type(), 0).getClass();
                            args[i] = getVarArgs((NewArrayInitExpression) arg,
                                    (int length) -> (Object[]) Array.newInstance(field.type(), length), () -> pollTarget(field.type()));
                        } else {
                            parameterTypes[indexOfField] = field.type();
                            args[i] = pollTarget(field.type());
                        }
                    } else {
                        int indexOfType = Lists.indexOf(parameterAnnotations,
                                a -> ParamType.class.isAssignableFrom(a.getClass()));
                        if (indexOfType >= 0)
                            parameterTypes[i] = ((ParamType) parameterAnnotations[indexOfType]).value();

                        if (indexOfLocal >= 0) {
                            args[i] = arg;
                            continue;
                        }

                        int indexOfFilter = Lists.indexOf(parameterAnnotations,
                                a -> a instanceof NestedExpression);
                        if (indexOfFilter >= 0) {
                            if (i == varArg) {
                                if (indexOfType < 0)
                                    parameterTypes[i] = getArrayType(getType());
                                args[i] = getVarArgs((NewArrayInitExpression) arg, (int n) ->
                                        (T[]) Array.newInstance(getType(), n), bsons::poll);
                            } else {
                                if (indexOfType < 0)
                                    parameterTypes[i] = getType();
                                args[i] = bsons.pop();
                            }
                        } else {
                            args[i] = i == varArg ? getVarArgs((NewArrayInitExpression) arg, null, constants::poll)
                                    : constants.pop();
                        }
                    }
                }
            } finally {
                pushContextArguments(currentArguments);
            }

            if (func.passThrough())
                bsons.push((T) args[0]);
            else {
                Method target = func.factory().getMethod(name, parameterTypes);
                bsons.push((T) target.invoke(null, args));
            }
        } else {

            PathInterpreter pathRetriever = new PathInterpreter();
            e.accept(pathRetriever);
            paths.push(pathRetriever.getPath().toString());
        }

        return e;
    }

    protected static Object[] getVarArgs(NewArrayInitExpression nai,
                                         IntFunction<Object[]> creator,
                                         Supplier<Object> source) {
        List<Expression> initializers = nai.getInitializers();
        int size = initializers.size();
        Object[] array = creator != null ? creator.apply(size) : (Object[]) Array.newInstance(nai.getComponentType(), size);
        for (int i = 0; i < size; i++)
            array[i] = source.get();

        Lists.reverse(array);

        return array;
    }

    @Override
    protected List<Expression> visitArguments(List<Expression> original) {
        return original;
    }

    @Override
    protected List<ParameterExpression> visitParameters(List<ParameterExpression> original) {
        return original;
    }

    @Override
    public Expression visit(ConstantExpression e) {
        constants.push(e.getValue());
        return e;
    }
}
