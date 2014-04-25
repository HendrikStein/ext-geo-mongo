package de.jt.mongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.mongodb.WriteConcern;

import de.jt.db.MongoGeoService;
import de.jt.model.GeoBoundingBox;
import de.jt.model.GeoLocation;
import de.jt.model.GeoPoint;
import de.jt.utils.GPXUtils;

/**
 * Mongo geo query tests.
 * 
 * @author Hendrik Stein
 * 
 */
public class MongoGeoTest extends AbstractMongoDBTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        /** Insert 64800(360*180) geo locations on the earth. */
        int locAmount = 0;
        GeoLocation location;
        /** Rotate south to north. */
        for (double lat = -90d; lat <= 90d; lat++) {
            /** Rotate east to west. */
            for (double lon = -180d; lon < 180; lon++) {
                locAmount++;
                location = new GeoLocation(new GeoPoint(lat, lon), "Number: " + locAmount);
                getGeoLocationCol().insert(location.toMongo(), WriteConcern.SAFE);
            }
        }
    }

    @Test
    public void testBoundingBoxRotateAroundPrimeMeridian() {
        // Neglect the earths curve
        double latBBoxDiameter = 10d;
        double lonBBoxDiameter = 10d;

        Map<Integer, QueryResult> resultMap = new HashMap<>();
        /** Shift lower left latitude coordinate by 0.5 degrees. Start South. */
        for (double latLowerLeft = -90.0d; latLowerLeft + latBBoxDiameter < 90d; latLowerLeft += (latBBoxDiameter / 2)) {
            /** Rotate around the prime meridian 360° */
            for (double lonLowerLeft = 0d; lonLowerLeft + lonBBoxDiameter <= 360d; lonLowerLeft += (lonBBoxDiameter / 2)) {
                double lonLowerLeftBBox = lonLowerLeft;
                if (lonLowerLeft > 180) {
                    lonLowerLeftBBox -= 360d;
                }

                double lonUpperRightBBox = lonLowerLeftBBox + lonBBoxDiameter;
                if (lonUpperRightBBox > 180) {
                    lonUpperRightBBox -= 360d;
                }

                /** Valid range is [-180,179.99999] to plot bounding box at http://gpso.de/maps/ */
                if (lonLowerLeftBBox == 180d) {
                    lonLowerLeftBBox = 179.99999;
                }

                if (lonUpperRightBBox == 180d) {
                    lonUpperRightBBox = 179.99999;
                }

                GeoPoint lowerLeft = new GeoPoint(latLowerLeft, lonLowerLeftBBox);
                GeoPoint upperRight = new GeoPoint(latLowerLeft + latBBoxDiameter, lonUpperRightBBox);
                GeoBoundingBox bbox = new GeoBoundingBox(lowerLeft, upperRight);
                MongoGeoService geoService = new MongoGeoService(getGeoLocationCol());
                List<GeoLocation> locationList = geoService.findLocation(bbox);
                int resultSize = locationList.size();
                if (!resultMap.containsKey(resultSize)) {
                    resultMap.put(resultSize, new QueryResult(bbox));
                } else {
                    resultMap.get(resultSize).increaseCount();
                }
            }
        }

        for (Map.Entry<Integer, QueryResult> entry : resultMap.entrySet()) {
            System.out.println("Result: " + entry.getKey());
            System.out.println("Count: " + entry.getValue().getResultCount());
            System.out.println(GPXUtils.bboxToTrack(entry.getValue().getGeoBoundingBox()));
            System.out.println("\n\n");
        }
    }

    @Test
    public void testBoundingBoxRotateSouthNorth() {
        Assert.fail("Needs to be implemented");
    }

}
