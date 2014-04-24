package de.jt.model;

/**
 * Geo Constants.
 * 
 * http://upload.wikimedia.org/wikipedia/commons/thumb/6/62/Latitude_and_Longitude_of_the_Earth.svg/1000px-
 * Latitude_and_Longitude_of_the_Earth.svg.png
 * 
 * @author Hendrik Stein
 */
public final class GeoConstants {

    /**
     * Latitude: -90 to 90 degrees
     */
    public static final int LAT = 0;

    /**
     * Latitude in MongoDB order.
     */
    public static final int MONGO_LAT = 1;

    /**
     * Longitude: -180 to 180 degrees
     */
    public static final int LONG = 1;

    /**
     * Longitude in MongoDB order.
     */
    public static final int MONGO_LONG = 0;

    /**
     * Minimum latitude: {@value} .
     */
    public static final double LAT_MIN = -90d;

    /**
     * Maximum latitude: {@value} .
     */
    public static final double LAT_MAX = 90d;

    /**
     * Minimum longitude: {@value} .
     */
    public static final double LONG_MIN = -180d;

    /**
     * Maximum longitude: {@value} .
     */
    public static final double LONG_MAX = 180d;

    /**
     * Utility class.
     */
    private GeoConstants() {
        // utility class
    }

}
