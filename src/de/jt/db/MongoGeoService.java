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
 * Database service for geo queries.
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
    public List<GeoLocation> findLocation(GeoBoundingBox bbox) {
        MongoQueryBuilder qb = new MongoQueryBuilder();

        if (bbox.fitWithinSphere()) {
            qb.put(GeoLocation.MONGO_GEOPOINT).geoWithinRingPolygon(bbox);
        } else {
            qb.put(GeoLocation.MONGO_GEOPOINT).geoWithinBox(bbox);
        }

        DBCursor cursor = null;
        try {
            cursor = dbCol.find(qb.build());
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
