package co.streamx.fluent.mongo.grammar;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

public class ArrayOperators {
    public static Bson arrayElemAt(String fieldName, int index) {
        return new Document("$arrayElemAt", Arrays.asList("$" + fieldName, index));
    }
}
