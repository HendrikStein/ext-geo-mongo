/**
 * This software is property of LAT of America.
 *
 * All rights reserved.
 * Unauthorized copying or transmission prohibited.
 */
package de.jt.db;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.QueryOperators;

import de.jt.model.GeoBoundingBox;
import de.jt.model.GeoPoint;

/**
 * Concrete mongo DB query builder.
 * 
 * @author hendrik.stein
 */
public class MongoQueryBuilder {

    /** One meter in miles. */
    private static final double ONE_METER_IN_MILES = 0.0006213712;

    /** Miles per degree. */
    private static final int MILES_PER_DEGREE = 69;

    /** Geo Within query key. */
    private static final String GEOWITHIN_KEY = "$geoWithin";

    /** Geometry query key. */
    private static final String GEOMETRY_KEY = "$geometry";

    /** Coordinates query key. */
    private static final String COORDINATES_KEY = "coordinates";

    /** The wrapped mongodb QueryBuilder. */
    private com.mongodb.QueryBuilder internalQueryBuilder = new QueryBuilder();

    /**
     * Adds a new key to the query if not present yet. Sets this key as the current key.
     * 
     * @param key MongoDB document key
     * @return the current QueryBuilder
     */
    public MongoQueryBuilder put(String key) {
        internalQueryBuilder.put(key);
        return this;
    }

    /**
     * Equivalent to <code>QueryBuilder.put(key)</code>. Intended for compound query chains to be more readable Example:
     * QueryBuilder.start("a").greaterThan(1).and("b").lessThan(3)
     * 
     * @param key MongoDB document key
     * @return the current QueryBuilder with an appended key operand
     */
    public MongoQueryBuilder and(String key) {
        internalQueryBuilder.and(key);
        return this;
    }

    /**
     * Equivalent to the $gt operator.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended "greater than" query
     */
    public MongoQueryBuilder greaterThan(Object object) {
        internalQueryBuilder.greaterThan(object);
        return this;
    }

    /**
     * Equivalent to the $gte operator.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended "greater than or equals" query
     */
    public MongoQueryBuilder greaterThanEquals(Object object) {
        internalQueryBuilder.greaterThanEquals(object);
        return this;
    }

    /**
     * Equivalent to the $lt operand.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended "less than" query
     */
    public MongoQueryBuilder lessThan(Object object) {
        internalQueryBuilder.lessThan(object);
        return this;
    }

    /**
     * Equivalent to the $lte operand.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended "less than or equals" query
     */
    public MongoQueryBuilder lessThanEquals(Object object) {
        internalQueryBuilder.lessThanEquals(object);
        return this;
    }

    /**
     * Equivalent of the find({key:value}).
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended equality query
     */
    public MongoQueryBuilder is(Object object) {
        internalQueryBuilder.is(object);
        return this;
    }

    /**
     * Equivalent of the $ne operand.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended inequality query
     */
    public MongoQueryBuilder notEquals(Object object) {
        internalQueryBuilder.notEquals(object);
        return this;
    }

    /**
     * Equivalent of the $in operand.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended "in array" query
     */
    public MongoQueryBuilder in(Object object) {
        internalQueryBuilder.in(object);
        return this;
    }

    /**
     * Equivalent of the $nin operand.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended "not in array" query
     */
    public MongoQueryBuilder notIn(Object object) {
        internalQueryBuilder.notIn(object);
        return this;
    }

    /**
     * Equivalent of the $mod operand.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended modulo query
     */
    public MongoQueryBuilder mod(Object object) {
        internalQueryBuilder.mod(object);
        return this;
    }

    /**
     * Equivalent of the $all operand.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended "matches all array contents" query
     */
    public MongoQueryBuilder all(Object object) {
        internalQueryBuilder.all(object);
        return this;
    }

    /**
     * Equivalent of the $size operand.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended size operator
     */
    public MongoQueryBuilder size(Object object) {
        internalQueryBuilder.size(object);
        return this;
    }

    /**
     * Equivalent of the $exists operand.
     * 
     * @param object Value to query
     * @return the current QueryBuilder with an appended exists operator
     */
    public MongoQueryBuilder exists(Object object) {
        internalQueryBuilder.exists(object);
        return this;
    }

    /**
     * Passes a regular expression for a query.
     * 
     * @param regex Regex pattern object
     * @return the current QueryBuilder with an appended regex query
     */
    public MongoQueryBuilder regex(Pattern regex) {
        internalQueryBuilder.regex(regex);
        return this;
    }

    /**
     * Equivalent of the $within operand, used for geospatial operation.
     * 
     * @param point coordinate
     * @param radius radius
     * @return the current QueryBuilder with a geo withinCenter operator
     */
    public MongoQueryBuilder withinCenter(GeoPoint point, double radius) {
        internalQueryBuilder.withinCenter(point.getLongitude(), point.getLatitude(), radius);
        return this;
    }

    /**
     * Equivalent of the $near operand.
     * 
     * @param point coordinate
     * @return the current QueryBuilder with a geo near operator
     */
    public MongoQueryBuilder near(GeoPoint point) {
        internalQueryBuilder.near(point.getLongitude(), point.getLatitude());
        return this;
    }

    /**
     * Equivalent of the $near operand.
     * 
     * @param point coordinate
     * @param maxDistanceInMeter max distance in meter
     * @return the current QueryBuilder with a geo near operator
     */
    public MongoQueryBuilder near(GeoPoint point, double maxDistanceInMeter) {
        double maxDistanceInRadians = metersToRadians(maxDistanceInMeter);
        internalQueryBuilder.near(point.getLongitude(), point.getLatitude(), maxDistanceInRadians);
        return this;
    }

    /**
     * Equivalent of the $nearSphere operand.
     * 
     * @param point coordinate
     * @return the current QueryBuilder with a geo nearSphere operator
     */
    public MongoQueryBuilder nearSphere(GeoPoint point) {
        internalQueryBuilder.nearSphere(point.getLongitude(), point.getLatitude());
        return this;
    }

    /**
     * Equivalent of the $nearSphere operand.
     * 
     * @param point coordinate
     * @param maxDistance max spherical distance
     * @return the current QueryBuilder with a geo nearSphere operator
     */
    public MongoQueryBuilder nearSphere(GeoPoint point, double maxDistance) {
        internalQueryBuilder.nearSphere(point.getLongitude(), point.getLatitude(), maxDistance);
        return this;
    }

    /**
     * Equivalent of the $centerSphere operand mostly intended for queries up to a few hundred miles or km.
     * 
     * @param point coordinate
     * @param maxDistance max spherical distance
     * @return the current QueryBuilder with a geo withinCenterSphere operator
     */
    public MongoQueryBuilder withinCenterSphere(GeoPoint point, double maxDistance) {
        internalQueryBuilder.withinCenterSphere(point.getLongitude(), point.getLatitude(), maxDistance);
        return this;
    }

    /**
     * Equivalent to a $within operand, based on a bounding box using represented by lower left and upper right corner.
     * Use {@link #geoWithinRingPolygon(BoundingBox bbox)} due to anti meridian problems
     * 
     * @param bbox the bounding box
     * @return the current QueryBuilder with a geo withinBox operator
     */
    @Deprecated
    public MongoQueryBuilder withinBox(GeoBoundingBox bbox) {
        GeoPoint lowerLeft = bbox.getLowerLeft();
        GeoPoint upperRight = bbox.getUpperRight();
        internalQueryBuilder.withinBox(lowerLeft.getLongitude(), lowerLeft.getLatitude(), upperRight.getLongitude(),
                upperRight.getLatitude());
        return this;
    }

    /**
     * Equivalent to a $geoWithin($geometry) operand, based on a bounding polygon represented by an {@link BoundingBox}.
     * 
     * NOTE Any geometry specified with GeoJSON to $geoWithin queries, must fit within a single hemisphere. MongoDB
     * interprets geometries larger than half of the sphere as queries for the smaller of the complementary geometries.
     * 
     * @param bbox the bounding box
     * @return the current QueryBuilder
     */
    public MongoQueryBuilder geoWithinRingPolygon(GeoBoundingBox bbox) {
        /**
         * For a polygon with only an exterior ring use following syntax. The $geoWithin operator queries for inclusion
         * in a GeoJSON polygon or a shape defined by legacy coordinate pairs.
         * 
         * <pre>
         * db.<collection>.find( { <location field> :
         *                          { $geoWithin :
         *                             { $geometry :
         *                                { type : "Polygon" ,
         *                                  coordinates : [ [ [ <lng1>, <lat1> ] , [ <lng2>, <lat2> ] ... ] ]
         *                       } } } } )
         * </pre>
         */

        // Create polygon from bounding box coordinates
        BasicDBList polygon = new BasicDBList();
        polygon.addAll(bbox.getPolygonAsRing());

        // This wrapper seems to be senseless but fulfills the GeoJSON Spec at http://geojson.org/geojson-spec.html
        BasicDBList polygonWrapper = new BasicDBList();
        polygonWrapper.add(polygon);

        // For type "Polygon", the "coordinates" member must be an array of LinearRing coordinate arrays. For Polygons
        // with multiple rings, the first must be the exterior ring and any others must be interior rings or holes.
        //
        // A LinearRing is closed LineString with 4 or more positions. The first and last positions are equivalent (they
        // represent equivalent points). Though a LinearRing is not explicitly represented as a GeoJSON geometry type,
        // it is referred to in the Polygon geometry type definition.
        DBObject coordinates = new BasicDBObject(COORDINATES_KEY, polygonWrapper);

        // Create type for $geometry
        DBObject type = new BasicDBObject("type", "Polygon");

        // Add geometry attributes
        DBObject geometryAttributes = new BasicDBObject();
        geometryAttributes.putAll(type);
        geometryAttributes.putAll(coordinates);

        // Create $geometry object
        DBObject geometry = new BasicDBObject(GEOMETRY_KEY, geometryAttributes);

        // Create $geoWithin object
        DBObject geoWithin = new BasicDBObject(GEOWITHIN_KEY, geometry);

        internalQueryBuilder.is(geoWithin);
        return this;
    }

    /**
     * Equivalent to a $within operand, based on a bounding polygon represented by an {@link BoundingBox}.
     * 
     * @param bbox the bounding box
     * @return the current {@link QueryBuilder}
     */
    public MongoQueryBuilder withinPolygon(GeoBoundingBox bbox) {
        internalQueryBuilder.withinPolygon(bbox.getPolygonAsRing());
        return this;
    }

    /**
     * The $box operator specifies a {@link BoundingBox} for a geospatial $geoWithin query.
     * 
     * @param bbox the bounding box
     * @return the current {@link QueryBuilder} with a geoWithin polygon operator
     */
    public MongoQueryBuilder geoWithinBox(GeoBoundingBox bbox) {
        /**
         * <pre>
         * { <location field> : { $geoWithin : { $box :
         *                                        [ [ <bottom left coordinates> ] ,
         *                                          [ <upper right coordinates> ] ] } } }
         * </pre>
         */

        // Create $geoWithin object
        DBObject geoWithin = new BasicDBObject(GEOWITHIN_KEY,
                new BasicDBObject(QueryOperators.BOX, new Object[] { bbox.getLowerLeft().getGeoJSONPoint(),
                        bbox.getUpperRight().getGeoJSONPoint() }));
        internalQueryBuilder.is(geoWithin);
        return this;
    }

    /**
     * Equivalent to a $geoWithin operand, based on a bounding polygon represented by an {@link BoundingBox}.
     * 
     * @param bbox the bounding box
     * @return the current {@link QueryBuilder}
     */
    public MongoQueryBuilder geoWithinPolygon(GeoBoundingBox bbox) {
        /**
         * <pre>
         * { <location field> : { $geoWithin : { $polygon : [ [ <x1> , <y1> ] ,
         *                                                    [ <x2> , <y2> ] ,
         *                                                    [ <x3> , <y3> ] ] } } }
         * </pre>
         */
        // Create $geoWithin object
        DBObject geoWithin = new BasicDBObject(GEOWITHIN_KEY,
                new BasicDBObject(QueryOperators.POLYGON, bbox.getPolygon()));
        internalQueryBuilder.is(geoWithin);
        return this;
    }

    /**
     * Equivalent to a $within operand, based on a bounding polygon represented by an array of points.
     * 
     * @param points an array of Points defining the vertices of the search area
     * @return the current QueryBuilder with a geo withinPolygon operator
     */
    public MongoQueryBuilder withinPolygon(List<GeoPoint> points) {
        List<Double[]> doublePoints = new ArrayList<Double[]>(points.size());
        for (GeoPoint point : points) {
            doublePoints.add(new Double[] { point.getLongitude(), point.getLatitude() });
        }
        internalQueryBuilder.withinPolygon(doublePoints);
        return this;
    }

    /**
     * Equivalent to a $or operand.
     * 
     * @param ors the DBObjects to append using the or operator
     * @return the current QueryBuilder with or appended DBObjects
     */
    public MongoQueryBuilder or(DBObject... ors) {
        internalQueryBuilder.or(ors);
        return this;
    }

    /**
     * Equivalent to an $and operand.
     * 
     * @param ands the DBObjects to append using the and operator
     * @return the current QueryBuilder with and appended DBObjects
     */
    public MongoQueryBuilder and(DBObject... ands) {
        internalQueryBuilder.and(ands);
        return this;
    }

    /**
     * Shortcut for QueryBuilder.put(String key).is(Object object).
     * 
     * @param key MongoDB document key
     * @param object Value to query
     * @return the current QueryBuilder with an appended equality query
     */
    public MongoQueryBuilder equal(String key, Object object) {
        internalQueryBuilder.put(key);
        internalQueryBuilder.is(object);
        return this;
    }

    /**
     * Shortcut for QueryBuilder.put(String key).notEquals(Object object).
     * 
     * @param key MongoDB document key
     * @param object Value to query
     * @return the current QueryBuilder with an appended not equality query
     */
    public MongoQueryBuilder ne(String key, Object object) {
        internalQueryBuilder.put(key);
        internalQueryBuilder.notEquals(object);
        return this;
    }

    /**
     * Shortcut for QueryBuilder.put(String key).greaterThanEquals(Object object).
     * 
     * @param key MongoDB document key
     * @param object Value to query
     * @return the current QueryBuilder with an appended query
     */
    public MongoQueryBuilder gte(String key, Object object) {
        internalQueryBuilder.put(key);
        internalQueryBuilder.greaterThanEquals(object);
        return this;
    }

    /**
     * Shortcut for QueryBuilder.put(String key).lessThanEquals(Object object).
     * 
     * @param key MongoDB document key
     * @param object Value to query
     * @return the current QueryBuilder with an appended query
     */
    public MongoQueryBuilder lte(String key, Object object) {
        internalQueryBuilder.put(key);
        internalQueryBuilder.lessThanEquals(object);
        return this;
    }

    /**
     * Shortcut for QueryBuilder.put(String key).greaterThan(Object object).
     * 
     * @param key MongoDB document key
     * @param object Value to query
     * @return the current QueryBuilder with an appended query
     */
    public MongoQueryBuilder gt(String key, Object object) {
        internalQueryBuilder.put(key);
        internalQueryBuilder.greaterThan(object);
        return this;
    }

    /**
     * Shortcut for QueryBuilder.put(String key).lessThan(Object object).
     * 
     * @param key MongoDB document key
     * @param object Value to query
     * @return the current QueryBuilder with an appended query
     */
    public MongoQueryBuilder lt(String key, Object object) {
        internalQueryBuilder.put(key);
        internalQueryBuilder.lessThan(object);
        return this;
    }

    public DBObject build() {
        return internalQueryBuilder.get();
    }

    /**
     * Converts the distance from meters to radiant.
     * 
     * @param distanceInMeter the distance in meter
     * @return the distance in radians
     */
    private double metersToRadians(double distanceInMeter) {
        /**
         * When using longitude and latitude, which are angular measures, distance is effectively specified in
         * approximate units of "degrees," which vary by position on the globe but can very roughly be converted to
         * distance using 69 miles per degree latitude or longitude. The maximum error in northern or southernmost
         * populated regions is ~2x longitudinally - for many purposes this is acceptable. Spherical queries take the
         * curvature of the earth into account.
         */
        double maxDistance = distanceInMeter * ONE_METER_IN_MILES / MILES_PER_DEGREE;
        return maxDistance;
    }

}
