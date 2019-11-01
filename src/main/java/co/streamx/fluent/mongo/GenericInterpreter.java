package co.streamx.fluent.mongo;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.IntFunction;

import org.bson.conversions.Bson;

import co.streamx.fluent.extree.expression.ConstantExpression;
import co.streamx.fluent.extree.expression.Expression;
import co.streamx.fluent.extree.expression.InvocationExpression;
import co.streamx.fluent.extree.expression.MemberExpression;
import co.streamx.fluent.extree.expression.NewArrayInitExpression;
import co.streamx.fluent.extree.expression.ParameterExpression;
import co.streamx.fluent.extree.expression.SimpleExpressionVisitor;
import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.NestedExpression;
import co.streamx.fluent.mongo.notation.Function;
import lombok.SneakyThrows;

class GenericInterpreter extends SimpleExpressionVisitor {

    public Bson popResult() {
        return bsons.pop();
    }

    protected final Deque<Bson> bsons = new ArrayDeque<>();

    protected final Deque<String> paths = new ArrayDeque<>();
    protected final Deque<Object> constants = new ArrayDeque<>();

    private List<Expression> currentArguments;

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

            int varArg = method.isVarArgs() ? parametersAnnotations.length - 1 : -1;
            for (int i = 0; i < parametersAnnotations.length; i++) {

                Expression arg = this.currentArguments.get(i);
                arg.accept(this);

                Annotation[] parameterAnnotations = parametersAnnotations[i];
                int indexOfField = Lists.indexOf(parameterAnnotations,
                        a -> FieldName.class.isAssignableFrom(a.getClass()));
                if (indexOfField >= 0) {
                    if (i == varArg) {
                        parameterTypes[i] = String[].class;
                        args[i] = getVarArgs((NewArrayInitExpression) arg, String[]::new, paths);
                    } else {
                        parameterTypes[indexOfField] = String.class;
                        args[i] = paths.pop();
                    }
                } else {
                    int indexOfFilter = Lists.indexOf(parameterAnnotations,
                            a -> NestedExpression.class.isAssignableFrom(a.getClass()));
                    if (indexOfFilter >= 0) {
                        if (i == varArg) {
                            parameterTypes[i] = Bson[].class;
                            args[i] = getVarArgs((NewArrayInitExpression) arg, Bson[]::new, bsons);
                        } else {
                            parameterTypes[i] = Bson.class;
                            args[i] = bsons.pop();
                        }
                    } else {
                        args[i] = i == varArg ? getVarArgs((NewArrayInitExpression) arg, null, constants)
                                : constants.pop();
                    }
                }
            }

            if (func.passThrough())
                bsons.push((Bson) args[0]);
            else {
                Method target = func.factory().getMethod(name, parameterTypes);
                bsons.push((Bson) target.invoke(null, args));
            }
        } else {

            PathInterpreter pathRetriever = new PathInterpreter();
            e.accept(pathRetriever);
            paths.push(pathRetriever.getPath().toString());
        }

        return e;
    }

    private static <T> T[] getVarArgs(NewArrayInitExpression nai,
                                      IntFunction<T[]> creator,
                                      Deque<T> source) {
        List<Expression> initializers = nai.getInitializers();
        int size = initializers.size();
        @SuppressWarnings("unchecked")
        T[] array = creator != null ? creator.apply(size) : (T[]) Array.newInstance(nai.getComponentType(), size);
        for (int i = 0; i < size; i++)
            array[i] = source.pop();

        Lists.reverse(array);

        return array;
    }

    @Override
    public Expression visit(InvocationExpression e) {
        List<Expression> currentArguments = this.currentArguments;
        this.currentArguments = e.getArguments();
        try {
            return super.visit(e);
        } finally {
            this.currentArguments = currentArguments;
        }
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
