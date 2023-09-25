package co.streamx.fluent.mongo;

import co.streamx.fluent.extree.expression.BinaryExpression;
import co.streamx.fluent.extree.expression.Expression;
import co.streamx.fluent.extree.expression.LambdaExpression;
import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Function;
import com.mongodb.client.model.mql.MqlDocument;
import com.mongodb.client.model.mql.MqlValue;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class MqlInterpreter extends GenericInterpreter<MqlValue> {

    private final MqlDocument document;

    public MqlInterpreter(MqlDocument document) {
        super();
        this.document = document;
    }

    @Override
    protected Class<MqlValue> getType() {
        return MqlValue.class;
    }

    @Override
    protected Object pollTarget(FieldName targetField) {
        return document.getField(paths.poll());
    }

    @Override
    public Expression visit(BinaryExpression e) {
        return super.visit(e);
    }

    @Override
    public MqlValue getResult(Class<?> resultType) {
        if (!bsons.isEmpty())
            super.getResult(resultType);

//        Class<?> resultType = e.getBody().getResultType();
        String fieldName = paths.pop();
        if (resultType.equals(Boolean.class)) {
            return document.getBoolean(fieldName);
        } else if (resultType.equals(Integer.class) || resultType.equals(Short.class) || resultType.equals(Byte.class)) {
            return document.getInteger(fieldName);
        } else if (Number.class.isAssignableFrom(resultType)) {
            return document.getNumber(fieldName);
        } else if (resultType.equals(String.class)) {
            return document.getString(fieldName);
//            case Array:
//                return document.getArray(paths.pop());
//            case Object.class:
//                return document.getObject(paths.pop());
        }
//        throw new IllegalArgumentException("Unsupported type: " + e.getBody().getResultType());

        return document.getField(fieldName);
    }

    @Override
    @SneakyThrows
    protected MqlValue invokeFunction(Function func, String name, Class<?>[] parameterTypes, Object obj, Object[] args) {

        if (!func.isStatic()) {
            obj = args[0];
            parameterTypes = Arrays.copyOfRange(parameterTypes, 1, parameterTypes.length);
            args = Arrays.copyOfRange(args, 1, args.length);

            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                if (type.isInterface() && type.isAnnotationPresent(FunctionalInterface.class)) {
                    InvocationHandler handler = new LambdaInvocationHandler((LambdaExpression<?>) args[i]);
                    Object proxy = Proxy.newProxyInstance(
                            type.getClassLoader(),
                            new Class[] { type },
                            handler);

                    args[i] = proxy;
                }
            }
        }
        return super.invokeFunction(func, name, parameterTypes, obj, args);
    }
}
