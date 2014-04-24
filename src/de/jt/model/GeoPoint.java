package de.jt.model;

/**
 * The geo point consisting of latitude and longitude.
 * 
 * @author Hendrik Stein
 * 
 */
public class GeoPoint {

    /** Geographic latitude (-90 to 90 degrees). */
    private double latitude;

    /** Geographic longitude (-180 to 180 degrees). */
    private double longitude;

    /**
     * Creates an instance with latidude = 0 and longitude = 0.
     */
    public GeoPoint() {
        latitude = 0d;
        longitude = 0d;
    }

    /**
     * Creates an instance;
     * 
     * @param latitude the latitude
     * @param longitude the longitude
     * 
     * @throws IllegalArgumentException if latitude or longitude is out of bounds
     */
    public GeoPoint(double latitude, double longitude) throws IllegalArgumentException {
        if (latitude < GeoConstants.LAT_MIN || latitude > GeoConstants.LAT_MAX) {
            throw new IllegalArgumentException("latitude out of bounds");
        }
        if (longitude < GeoConstants.LONG_MIN || longitude > GeoConstants.LONG_MAX) {
            throw new IllegalArgumentException("longitude out of bounds");
        }

        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Creates an instance;
     * 
     * @param location the location as an array of [latidude, longitude]
     * 
     * @throws IllegalArgumentException if latitude or longitude is out of bounds
     */
    public GeoPoint(double[] location) throws IllegalArgumentException {
        this(location[GeoConstants.LAT], location[GeoConstants.LONG]);
    }

    /**
     * Check, if both latitude and longitude are 0.
     * 
     * @return <tt>true</tt> if both latitude and longitude are 0.
     */
    public boolean isLatLonZero() {
        return latitude == 0d && longitude == 0d;
    }

    /**
     * Returns the point as an array.
     * 
     * @return the point as an array [latidude, longitude].
     */
    public double[] getPoint() {
        double[] location = new double[2];
        location[GeoConstants.LAT] = latitude;
        location[GeoConstants.LONG] = longitude;
        return location;
    }

    /**
     * Returns the point as an array in GeoJSON format. See http://geojson.org and
     * http://geojson.org/geojson-spec.html#positions. This format can be used in a MongoDB context.
     * 
     * @return the point as an array [longitude, latidude] (<b>Attention</b>: reverse order of latidude, longitude).
     */
    public Double[] getGeoJSONPoint() {
        Double[] location = new Double[2];

        location[GeoConstants.MONGO_LONG] = longitude;
        location[GeoConstants.MONGO_LAT] = latitude;

        return location;
    }

    /**
     * Returns the latitude.
     * 
     * @return the latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude.
     * 
     * @return the longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Compare latitude with other point.
     * 
     * @param other the point to compare with
     * @return <tt>true</tt> if latitude of both points are the same
     */
    public boolean isLatEquals(GeoPoint other) {
        return this.latitude == other.latitude;
    }

    /**
     * Compare longitude with other point.
     * 
     * @param other the point to compare with
     * @return <tt>true</tt> if longitude of both points are the same
     */
    public boolean isLonEquals(GeoPoint other) {
        return this.longitude == other.longitude;
    }

    @Override
    public String toString() {
        return "GeoPoint [latitude=" + latitude + ", longitude=" + longitude + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GeoPoint other = (GeoPoint) obj;
        if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
            return false;
        if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
            return false;
        return true;
    }
    
}
