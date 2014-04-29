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

    /**
     * Insert a list of {@link GeoLocation} to the database.
     * 
     * @param locations the locations
     */
    private void insertLocations(List<GeoLocation> locations) {
        for (GeoLocation location : locations) {
            getGeoLocationCol().insert(location.toMongo(), WriteConcern.SAFE);
        }
    }

    /**
     * Insert certain {@link GeoLocation}
     */
    private void insertCertainGeoLocations() {
        getGeoLocationCol().insert(SampleGeoLocations.locationAleutianIslands.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(SampleGeoLocations.locationHiroshima.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(SampleGeoLocations.locationLosAngeles.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(SampleGeoLocations.locationThailand.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(SampleGeoLocations.locationBerlin.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(SampleGeoLocations.locationCologne.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(SampleGeoLocations.locationFrankfurt.toMongo(), WriteConcern.SAFE);
    }

    @Test
    public void testBoundingBoxRotateWholeWorldAroundPrimeMeridian() {
        // Insert testdata
        insertLocations(SampleGeoLocations.getWholeWorld());
        // Neglect the earths curve
        double latBBoxDiameter = 10d;
        double lonBBoxDiameter = 20d;

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

    @Test
    public void testBoundingBox() {
        insertCertainGeoLocations();
        GeoPoint lowerLeft = new GeoPoint(49.74733, 6.63575);// Trier
        GeoPoint upperRight = new GeoPoint(51.53075, 9.07471);// Warburg
        GeoBoundingBox bbox = new GeoBoundingBox(lowerLeft, upperRight);

        // Print Bounding box as gpx output to plot it for example at http://gpso.de/maps/
        System.out.println(GPXUtils.bboxToTrack(bbox));

        MongoGeoService geoService = new MongoGeoService(getGeoLocationCol());
        List<GeoLocation> locationList = geoService.findLocation(bbox);

        // Expected Results are Cologne and Frankfurt
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(SampleGeoLocations.locationFrankfurt));
        Assert.assertTrue(locationList.contains(SampleGeoLocations.locationCologne));
    }

    @Test
    public void testBoundingBoxOverAntimeridian() {
        insertCertainGeoLocations();
        GeoPoint lowerLeft = new GeoPoint(16.67304, 121.11328);// Philippines
        GeoPoint upperRight = new GeoPoint(65.08833, -152.40234);// Alaska
        GeoBoundingBox bbox = new GeoBoundingBox(lowerLeft, upperRight);

        // Print Bounding box as gpx output to plot it for example at http://gpso.de/maps/
        System.out.println(GPXUtils.bboxToTrack(bbox));

        MongoGeoService geoService = new MongoGeoService(getGeoLocationCol());
        List<GeoLocation> locationList = geoService.findLocation(bbox);
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(SampleGeoLocations.locationAleutianIslands));
        Assert.assertTrue(locationList.contains(SampleGeoLocations.locationHiroshima));
    }
}
