package co.streamx.fluent.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.MongoClientSettings;

public interface CommonTest {

    final CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    default void assertQuery(Bson bson,
                             String expected) {

        String json = print(bson);
        assertEquals(expected, json);
    }

    static String print(Bson bson) {
        if (bson == null)
            return null;
        String json = bson.toBsonDocument(null, pojoCodecRegistry).toJson();
        System.out.println(json);
        return json;
    }
}
