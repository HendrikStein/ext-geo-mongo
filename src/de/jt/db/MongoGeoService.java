package de.jt.db;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import de.jt.model.GeoBoundingBox;
import de.jt.model.GeoLocation;

/**
 * Mongo database service for geo queries.
 * 
 * @author Hendrik Stein
 * 
 */
public class MongoGeoService {

    /** Mongo DB collection. */
    private final DBCollection dbCol;

    /**
     * Creates an instance.
     * 
     * @param dbCol the database collection
     */
    public MongoGeoService(DBCollection dbCol) {
        this.dbCol = dbCol;
    }

    /**
     * Find geographical locations for a bounding box.
     * 
     * @param bbox the bounding box
     * @return the list of geo locations
     */
    public List<GeoLocation> getLocations(GeoBoundingBox bbox) {
        List<GeoLocation> resultGeoList = new ArrayList<>();
        if (bbox.isOverAntimeridian()) {
            List<GeoBoundingBox> boxes = bbox.splitByAntimeridian();
            resultGeoList.addAll(findByBBox(boxes.get(0)));
            resultGeoList.addAll(findByBBox(boxes.get(1)));
        } else {
            resultGeoList = findByBBox(bbox);
        }

        return resultGeoList;
    }

    /**
     * Find geographical locations for a bounding box.
     * 
     * @param bbox the {@link GeoBoundingBox}
     * @return the list of {@link GeoLocation}
     */
    private List<GeoLocation> findByBBox(GeoBoundingBox bbox) {
        MongoQueryBuilder builder = new MongoQueryBuilder();
        builder.put(GeoLocation.MONGO_GEOPOINT).geoWithinBox(bbox);

        DBCursor cursor = null;
        try {
            cursor = dbCol.find(builder.build());
            List<GeoLocation> locationList = new ArrayList<>();

            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                GeoLocation geoLocation = new GeoLocation((BasicDBObject) document);
                locationList.add(geoLocation);
            }
            return locationList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
