package co.streamx.fluent.mongo.grammar;

import java.util.Collection;

import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Geometry;

import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Function;
import co.streamx.fluent.mongo.notation.NestedExpression;

@Function(factory = Indexes.class)
public interface FluentIndexes {

    @SafeVarargs
    static <TItem> Index ascending(@FieldName TItem... fields) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    static <TItem> Index descending(@FieldName TItem... fields) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    static <TItem extends Geometry> Index geo2dsphere(@FieldName TItem... fields) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> Index geo2d(@FieldName TItem field) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> Index geoHaystack(@FieldName TItem field,
                                                      final Index additional) {
        throw new UnsupportedOperationException();
    }

    static Index text(@FieldName String field) {
        throw new UnsupportedOperationException();
    }

    static Index text(@FieldName Collection<String> field) {
        throw new UnsupportedOperationException();
    }

    static Index text() {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Comparable<? extends TItem>> Index hashed(@FieldName TItem field) {
        throw new UnsupportedOperationException();
    }

    static Index compoundIndex(@NestedExpression Index... indexes) {
        throw new UnsupportedOperationException();
    }
}
