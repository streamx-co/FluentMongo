package co.streamx.fluent.mongo;

import org.bson.conversions.Bson;

public class BsonGenericInterpreter extends GenericInterpreter<Bson> {
    @Override
    protected Class<Bson> getType() {
        return Bson.class;
    }
}
