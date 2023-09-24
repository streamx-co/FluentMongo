package co.streamx.fluent.mongo;

import com.mongodb.client.model.mql.MqlDocument;
import com.mongodb.client.model.mql.MqlValue;

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
    protected Object pollTarget(Class<?> type) {
        return document.getField(paths.poll());
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
}
