package co.streamx.fluent.mongo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.streamx.fluent.extree.expression.ConstantExpression;
import co.streamx.fluent.extree.expression.Expression;
import co.streamx.fluent.extree.expression.ExpressionType;
import co.streamx.fluent.extree.expression.InvocableExpression;
import co.streamx.fluent.extree.expression.InvocationExpression;
import co.streamx.fluent.extree.expression.LambdaExpression;
import co.streamx.fluent.extree.expression.MemberExpression;
import co.streamx.fluent.extree.expression.ParameterExpression;
import co.streamx.fluent.extree.expression.SimpleExpressionVisitor;
import co.streamx.fluent.extree.expression.UnaryExpression;
import co.streamx.fluent.mongo.notation.Function;
import co.streamx.fluent.mongo.notation.Local;

final class Normalizer extends SimpleExpressionVisitor {

    private final List<Expression> localLambdas = new ArrayList<>();
    private final LambdaExpressionFinder lambdaExpressionFinder = new LambdaExpressionFinder();

    public static Normalizer get() {
        return new Normalizer();
    }

    private static boolean isFunctional(Class<?> clazz) {
        if (clazz.isSynthetic())
            return true;

        for (Class<?> i : clazz.getInterfaces())
            if (i.isAnnotationPresent(FunctionalInterface.class))
                return true;

        return false;
    }

    private Object[] contextArgumentsArray(List<Expression> args) {
        if (args.isEmpty())
            return null;
        List<Expression> contextArguments = getContextArguments();
        return args.stream().map(e -> {
            Expression x = removeCast(e);
            if (x instanceof ParameterExpression)
                x = contextArguments.get(((ParameterExpression) x).getIndex());

            if (!(x instanceof ConstantExpression))
                throw TranslationError.REQUIRES_EXTERNAL_PARAMETER.getError(x);

            return ((ConstantExpression) x).getValue();
        }).toArray();
    }

    private static Expression removeCast(Expression x) {
        return (x != null && x.getExpressionType() == ExpressionType.Convert)
                ? removeCast(((UnaryExpression) x).getFirst())
                : x;
    }

    private static boolean isSameOrDerived(Executable base,
                                           Executable derived) {
        return Modifier.isStatic(base.getModifiers()) == Modifier.isStatic(derived.getModifiers())
                && base.getDeclaringClass().isAssignableFrom(derived.getDeclaringClass())
                && base.getName().equals(derived.getName()) && base.getParameterCount() == derived.getParameterCount();
    }

    @Override
    public Expression visit(InvocationExpression e) {
        InvocableExpression target = e.getTarget();
        if (!(target instanceof MemberExpression))
            return super.visit(e);

        MemberExpression memberExpression = (MemberExpression) target;
        Member member = memberExpression.getMember();
        if (!(member instanceof Executable))
            return super.visit(e);

        Executable method = (Executable) member;

        if (method.isAnnotationPresent(Local.class)) {
            Object result = LambdaExpression.compile(e).apply(contextArgumentsArray(e.getArguments()));
            boolean isSynthetic = result != null && isFunctional(result.getClass());
            if (isSynthetic) {
                LambdaExpression<?> parsed = LambdaExpression.parse(result);
                return visit(parsed);
            }

            return Expression.constant(result);
        }

        Map<String, Map<Executable, LambdaExpression<?>>> substitutions = MongoConfig.getSubstitutions();
        Map<Executable, LambdaExpression<?>> subByName = substitutions.get(method.getName());
        if (subByName != null) {
            for (Executable m : subByName.keySet()) {
                if (isSameOrDerived(m, method)) {
                    LambdaExpression<?> substition = subByName.get(m);
                    Expression instance = memberExpression.getInstance();
                    List<Expression> args = e.getArguments();
                    if (instance != null) {
                        List<Expression> newArgs = new ArrayList<>();
                        newArgs.add(instance);
                        newArgs.addAll(args);
                        args = newArgs;
                    }

                    e = Expression.invoke(substition, args);
                    break;
                }
            }
        }

        boolean isVisitingFunction = method.isAnnotationPresent(Function.class);

        if (!isVisitingFunction)
            isVisitingFunction = method.getDeclaringClass().isAnnotationPresent(Function.class);

        if (isVisitingFunction) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] parameterAnnotation = parameterAnnotations[i];
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof Local) {
                        e.getArguments().get(i).accept(lambdaExpressionFinder);
                    }
                }
            }
        }

        return super.visit(e);
    }

    @Override
    public Expression visit(LambdaExpression<?> e) {
        return localLambdas.contains(e) ?
                super.visit(e) :
                super.visit(e.parseMethodRef());
    }

    private class LambdaExpressionFinder extends SimpleExpressionVisitor {

        @Override
        public Expression visit(LambdaExpression<?> e) {
            localLambdas.add(e);
            return super.visit(e);
        }
    }
}
