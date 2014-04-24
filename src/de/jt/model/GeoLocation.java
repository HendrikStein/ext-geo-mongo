package de.jt.model;

import org.bson.types.BasicBSONList;

import com.mongodb.BasicDBObject;

/**
 * Represents a geographical location.
 * 
 * @author Hendrik Stein
 */
public class GeoLocation {

    /**
     * Mongo key for the geo location point stored as a 2d index array [lon, lat] which is used for geospatial mongo
     * queries.
     */
    public static final String MONGO_GEOPOINT = "gl";

    /** Mongo key for the description. */
    public static final String MONGO_DESCRIPTION = "d";

    /** Textual description of a given location */
    private String description;

    /** The geographical point of this location. */
    private GeoPoint geoPoint;

    /**
     * Creates an instance.
     * 
     * @param point the geographic point.
     * @param description the description
     */
    public GeoLocation(GeoPoint geoPoint, String description) {
        this.geoPoint = geoPoint;
        this.description = description;
    }

    /**
     * Create an instance from a BasicDBObject.
     * 
     * @param basicDBObject the basic db object
     */
    public GeoLocation(BasicDBObject basicDBObject) {
        fromMongo(basicDBObject);
    }

    /**
     * Get the geographic description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the geographical point
     * 
     * @return the geographical point
     */
    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    /**
     * Creates an instance based in the BSON representation read from MongoDB.
     * 
     * @param mongoObj the BSON representation to convert.
     * 
     */
    private void fromMongo(BasicDBObject mongoObj) {
        BasicBSONList loc = (BasicBSONList) mongoObj.get(MONGO_GEOPOINT);
        if (loc != null) {
            double latitude = (Double) loc.get(GeoConstants.MONGO_LAT);
            double longitude = (Double) loc.get(GeoConstants.MONGO_LONG);
            this.geoPoint = new GeoPoint(latitude, longitude);
        }

        this.description = mongoObj.getString(MONGO_DESCRIPTION);
    }

    /**
     * Converts the internal state of the instance to a BSON representation which can be stored in MongoDB.
     * 
     * @return the BSON representation.
     */
    public BasicDBObject toMongo() {
        BasicDBObject mongoObj = new BasicDBObject();

        // location will be persisted as GeoJSON ([LON,LAT]) for geo indexing.
        mongoObj.put(MONGO_GEOPOINT, geoPoint.getGeoJSONPoint());
        mongoObj.put(MONGO_DESCRIPTION, description);
        return mongoObj;
    }

    @Override
    public String toString() {
        return "GeoLocation [description=" + description + ", geoPoint=" + geoPoint + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((geoPoint == null) ? 0 : geoPoint.hashCode());
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
        GeoLocation other = (GeoLocation) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (geoPoint == null) {
            if (other.geoPoint != null)
                return false;
        } else if (!geoPoint.equals(other.geoPoint))
            return false;
        return true;
    }

}
