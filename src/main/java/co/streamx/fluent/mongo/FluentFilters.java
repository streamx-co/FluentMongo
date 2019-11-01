package co.streamx.fluent.mongo;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.BsonType;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.Point;

import co.streamx.fluent.mongo.functions.Function1;
import co.streamx.fluent.mongo.notation.FieldName;
import co.streamx.fluent.mongo.notation.Filter;
import co.streamx.fluent.mongo.notation.Function;

@Function(factory = Filters.class)
public interface FluentFilters {
    static <TItem> boolean eq(@FieldName final TItem field,
                              final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean ne(@FieldName final TItem field,
                              final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean gt(@FieldName final TItem field,
                              final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean lt(@FieldName final TItem field,
                              final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean gte(@FieldName final TItem field,
                               final TItem value) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean lte(@FieldName final TItem field,
                               final TItem value) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    static <TItem> boolean in(@FieldName final TItem field,
                              final TItem... values) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean in(@FieldName final TItem field,
                              final Iterable<TItem> values) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    static <TItem> boolean nin(@FieldName final TItem field,
                               final TItem... values) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean nin(@FieldName final TItem field,
                               final Iterable<TItem> values) {
        throw new UnsupportedOperationException();
    }

    static boolean and(@Filter final boolean... filters) {
        throw new UnsupportedOperationException();
    }

    static boolean or(@Filter final boolean... filters) {
        throw new UnsupportedOperationException();
    }

    static boolean not(@Filter final boolean filter) {
        throw new UnsupportedOperationException();
    }

    static boolean nor(@Filter final boolean... filters) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean exists(@FieldName final TItem field) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean exists(@FieldName final TItem field,
                                  final boolean exists) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean type(@FieldName final TItem field,
                                final BsonType type) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean type(@FieldName final TItem field,
                                final String type) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> boolean mod(@FieldName final TItem field,
                                              final long divisor,
                                              final long remainder) {
        throw new UnsupportedOperationException();
    }

    static boolean regex(@FieldName final String field,
                         final String pattern) {
        throw new UnsupportedOperationException();
    }

    static boolean regex(@FieldName final String field,
                         final String pattern,
                         final String options) {
        throw new UnsupportedOperationException();
    }

    static boolean regex(@FieldName final String field,
                         final Pattern pattern) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    static <TItem> boolean all(@FieldName final Collection<TItem> field,
                               final TItem... values) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean all(@FieldName final Collection<TItem> field,
                               final Iterable<TItem> values) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean elemMatch(@FieldName final Collection<TItem> field,
                                     @Filter final Function1<TItem, Boolean> filter) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean elemMatch(@FieldName final Collection<TItem> field,
                                     final Bson filter) {
        throw new UnsupportedOperationException();
    }

    static <TItem> boolean size(@FieldName final Collection<TItem> field,
                                final int size) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> boolean bitsAllClear(@FieldName final TItem field,
                                                       final long bitmask) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> boolean bitsAllSet(@FieldName final TItem field,
                                                     final long bitmask) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> boolean bitsAnyClear(@FieldName final TItem field,
                                                       final long bitmask) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Number> boolean bitsAnySet(@FieldName final TItem field,
                                                     final long bitmask) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean geoWithin(@FieldName final TItem field,
                                                      final Geometry geometry) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean geoWithin(@FieldName final TItem field,
                                                      final Bson geometry) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean geoWithinBox(@FieldName final TItem field,
                                                         final double lowerLeftX,
                                                         final double lowerLeftY,
                                                         final double upperRightX,
                                                         final double upperRightY) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean geoWithinPolygon(@FieldName final TItem field,
                                                             final List<List<Double>> points) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean geoWithinCenter(@FieldName final TItem field,
                                                            final double x,
                                                            final double y,
                                                            final double radius) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean geoWithinCenterSphere(@FieldName final TItem field,
                                                                  final double x,
                                                                  final double y,
                                                                  final double radius) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean geoIntersects(@FieldName final TItem field,
                                                          final Bson geometry) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean geoIntersects(@FieldName final TItem field,
                                                          final Geometry geometry) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean near(@FieldName final TItem field,
                                                 final Point geometry,
                                                 final Double maxDistance,
                                                 final Double minDistance) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean near(@FieldName final TItem field,
                                                 final Bson geometry,
                                                 final Double maxDistance,
                                                 final Double minDistance) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean near(@FieldName final TItem field,
                                                 final double x,
                                                 final double y,
                                                 final Double maxDistance,
                                                 final Double minDistance) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean nearSphere(@FieldName final TItem field,
                                                       final Point geometry,
                                                       final Double maxDistance,
                                                       final Double minDistance) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean nearSphere(@FieldName final TItem field,
                                                       final Bson geometry,
                                                       final Double maxDistance,
                                                       final Double minDistance) {
        throw new UnsupportedOperationException();
    }

    static <TItem extends Geometry> boolean nearSphere(@FieldName final TItem field,
                                                       final double x,
                                                       final double y,
                                                       final Double maxDistance,
                                                       final Double minDistance) {
        throw new UnsupportedOperationException();
    }

    static boolean jsonSchema(final Bson schema) {
        throw new UnsupportedOperationException();
    }

    @Function(factory = Object.class, passThrough = true)
    static boolean filter(final Bson filter) {
        throw new UnsupportedOperationException();
    }
}
