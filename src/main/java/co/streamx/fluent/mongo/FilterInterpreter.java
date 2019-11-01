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

import com.mongodb.client.model.Filters;

import co.streamx.fluent.extree.expression.BinaryExpression;
import co.streamx.fluent.extree.expression.ConstantExpression;
import co.streamx.fluent.extree.expression.Expression;
import co.streamx.fluent.extree.expression.ExpressionType;
import co.streamx.fluent.extree.expression.InvocationExpression;
import co.streamx.fluent.extree.expression.MemberExpression;
import co.streamx.fluent.extree.expression.NewArrayInitExpression;
import co.streamx.fluent.extree.expression.ParameterExpression;
import co.streamx.fluent.extree.expression.SimpleExpressionVisitor;
import co.streamx.fluent.extree.expression.UnaryExpression;
import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Filter;
import co.streamx.fluent.mongo.notation.Function;
import lombok.SneakyThrows;

final class FilterInterpreter extends SimpleExpressionVisitor {

    public Bson popResult() {
        return bsons.pop();
    }

    private Deque<Bson> bsons = new ArrayDeque<>();

    private Deque<String> paths = new ArrayDeque<>();
    private Deque<Object> constants = new ArrayDeque<>();

    private List<Expression> currentArguments;

    @Override
    public Expression visit(BinaryExpression e) {

        super.visit(e);

        switch (e.getExpressionType()) {
        case ExpressionType.LogicalAnd:
            Bson b2a = bsons.pop();
            bsons.push(Filters.and(bsons.pop(), b2a));

            break;
        case ExpressionType.LogicalOr:
            Bson b2o = bsons.pop();
            bsons.push(Filters.or(bsons.pop(), b2o));

            break;

        case ExpressionType.Equal:
            bsons.push(Filters.eq(paths.pop(), constants.pop()));
            break;
        case ExpressionType.NotEqual:
            bsons.push(Filters.ne(paths.pop(), constants.pop()));
            break;

        case ExpressionType.GreaterThan:
            bsons.push(Filters.gt(paths.pop(), constants.pop()));
            break;
        case ExpressionType.GreaterThanOrEqual:
            bsons.push(Filters.gte(paths.pop(), constants.pop()));
            break;
        case ExpressionType.LessThan:
            bsons.push(Filters.lt(paths.pop(), constants.pop()));
            break;
        case ExpressionType.LessThanOrEqual:
            bsons.push(Filters.lte(paths.pop(), constants.pop()));
            break;
        default:
            throw new IllegalArgumentException(
                    TranslationError.UNSUPPORTED_EXPRESSION_TYPE.getError(getOperatorSign(e.getExpressionType())));
        }

        return e;
    }

    @Override
    public Expression visit(UnaryExpression e) {
        super.visit(e);

        switch (e.getExpressionType()) {
        case ExpressionType.IsNull:
            bsons.push(Filters.eq(paths.pop(), null));
            break;
        case ExpressionType.IsNonNull:
            bsons.push(Filters.ne(paths.pop(), null));
            break;
        case ExpressionType.Convert:
            break;

        case ExpressionType.LogicalNot:
            bsons.push(Filters.not(bsons.pop()));
            break;
        default:
            throw new IllegalArgumentException(
                    TranslationError.UNSUPPORTED_EXPRESSION_TYPE.getError(getOperatorSign(e.getExpressionType())));
        }

        return e;
    }

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

    private static String getOperatorSign(int expressionType) {
        switch (expressionType) {
        case ExpressionType.LogicalAnd:
            return "AND";
        case ExpressionType.LogicalOr:
            return "OR";
        case ExpressionType.NotEqual:
            return "<>";
        default:
            return ExpressionType.toString(expressionType);
        }
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
                    parameterTypes[indexOfField] = String.class;
                    args[i] = paths.pop();
                } else {
                    int indexOfFilter = Lists.indexOf(parameterAnnotations,
                            a -> Filter.class.isAssignableFrom(a.getClass()));
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
