package de.jt.model;

import java.util.Arrays;
import java.util.List;

/**
 * A bounding box consisting of a lower left, upper right, upper left and lower right coordinate.
 * 
 * @author Hendrik Stein
 */
public class GeoBoundingBox {
    /** Lower left coordinate of the bounding box. */
    private GeoPoint lowerLeft;

    /** Upper right coordinate of the bounding box. */
    private GeoPoint upperRight;

    /** Upper left coordinate of the bounding box. */
    private GeoPoint upperLeft;

    /** Lower right coordinate of the bounding box. */
    private GeoPoint lowerRight;

    /**
     * Creates an instance of a bounding box.
     * 
     * @param lowerLeft the lower left coordinate of the bounding box
     * @param upperRight the upper right coordinate of the bounding box
     * 
     * @throws IllegalArgumentException if coordinates have same latitude or longitude or the box is a single point or a
     *         parameter is {@code null}
     */
    public GeoBoundingBox(GeoPoint lowerLeft, GeoPoint upperRight) throws IllegalArgumentException {
        if (lowerLeft == null || upperRight == null) {
            throw new IllegalArgumentException("GeoBoundingBox: null parameter");
        }

        // check if box is a point
        if (lowerLeft.equals(upperRight)) {
            throw new IllegalArgumentException("GeoBoundingBox is a single point");
        }

        // check for same latitude or longitude
        if (lowerLeft.isLatEquals(upperRight) || lowerLeft.isLonEquals(upperRight)) {
            throw new IllegalArgumentException("GeoBoundingBox: latitude or longitude of both points are the same");
        }

        // check if coordinates are all zero
        if (lowerLeft.isLatLonZero() && upperRight.isLatLonZero()) {
            throw new IllegalArgumentException("GeoBoundingBox: both points are 0.0");
        }

        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        this.upperLeft = new GeoPoint(upperRight.getLatitude(), lowerLeft.getLongitude());
        this.lowerRight = new GeoPoint(lowerLeft.getLatitude(), upperRight.getLongitude());
    }

    /**
     * Returns the lower left point.
     * 
     * @return the lower left point
     */
    public GeoPoint getLowerLeft() {
        return lowerLeft;
    }

    /**
     * Returns the upper right point.
     * 
     * @return the upper right point
     */
    public GeoPoint getUpperRight() {
        return upperRight;
    }

    /**
     * Returns the upper left point.
     * 
     * @return the upper left point
     */
    public GeoPoint getUpperLeft() {
        return upperLeft;
    }

    /**
     * Returns the lower right point.
     * 
     * @return the lower right point
     */
    public GeoPoint getLowerRight() {
        return lowerRight;
    }

    /**
     * Any geometry specified with GeoJSON to $geoWithin queries, must fit within a single hemisphere. MongoDB
     * interprets geometries larger than half of the sphere as queries for the smaller of the complementary geometries.
     * 
     * @return <code>true</code> if fits within sphere else <code>false</code>
     */
    public boolean fitWithinSphere() {
        // The max latitude/longitude difference for the eastern/western hemisphere that mongo can handle for geoWithin
        // queries.
        // The max latitude possible for this hemisphere are 180 degrees. (180/2)
        double maxLatEastWestSphereDiff = 90d;
        // The max longitude possible for this hemisphere are 180 degrees. (180/2)
        double maxLonEastWestSphereDiff = 90d;

        // The max latitude/longitude difference for the northern/southern hemisphere that mongo can handle for
        // geoWithin queries.
        // The max latitude possible for this hemisphere are 360 degrees.(360/2)
        final double maxLatNorthSouthSphereDiff = 180d;
        // The max longitude possible for this hemisphere are 90 degrees. (90/2)
        final double maxLonNorthSouthSphereDiff = 45d;

        // The latitude break even point to switch the other way round
        final double latBreakEven = 180d;

        // The longitude break even point to swith the other way round
        final double lonBreakEven = 90d;

        double latDiff = calculateDiff(lowerLeft.getLatitude(), upperLeft.getLatitude());
        if (latDiff > latBreakEven) {
            latDiff = calulateAbsDiff(lowerLeft.getLatitude(), upperLeft.getLatitude());
        }

        double lonDiff = calculateDiff(lowerLeft.getLongitude(), lowerRight.getLongitude());
        if (lonDiff > lonBreakEven) {
            lonDiff = calulateAbsDiff(lowerLeft.getLongitude(), lowerRight.getLongitude());
        }

        // Check if lat/lon difference fits into sphere
        if ((latDiff < maxLatEastWestSphereDiff) && (lonDiff < maxLonEastWestSphereDiff)) {
            return true;
        } else if ((latDiff < maxLatNorthSouthSphereDiff) && (lonDiff < maxLonNorthSouthSphereDiff)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the absolute double difference value of two double values.
     * 
     * @param x the first double
     * @param y the second double
     * @return the difference
     */
    private double calulateAbsDiff(double x, double y) {
        double absX = Math.abs(x);
        double absY = Math.abs(y);
        if (Math.max(absX, absY) == absX) {
            return absX - absY;
        } else {
            return absY - absX;
        }
    }

    /**
     * Returns the difference value of two doubles.
     * 
     * @param x the first double
     * @param y the second double
     * @return the difference
     */
    private double calculateDiff(double x, double y) {
        if (Math.max(x, y) == x) {
            return x - y;
        } else {
            return y - x;
        }
    }

    /**
     * Returns the polygon representation of the bounding box as a ring.
     * 
     * @return the list of geo points
     */
    public List<Double[]> getPolygonAsRing() {
        return Arrays.asList(lowerLeft.getGeoJSONPoint(), upperLeft.getGeoJSONPoint(), upperRight.getGeoJSONPoint(),
                lowerRight.getGeoJSONPoint(), lowerLeft.getGeoJSONPoint());
    }

    /**
     * Returns the polygon representation of the bounding box.
     * 
     * @return the list of geo points
     */
    public List<Double[]> getPolygon() {
        return Arrays.asList(lowerLeft.getGeoJSONPoint(), upperLeft.getGeoJSONPoint(), upperRight.getGeoJSONPoint(),
                lowerRight.getGeoJSONPoint());
    }

    @Override
    public String toString() {
        return "BoundingBox[lowerLeft=" + lowerLeft + ", upperRight=" + upperRight + ", upperLeft=" + upperLeft
                + ", lowerRight=" + lowerRight + "]";
    }

}
