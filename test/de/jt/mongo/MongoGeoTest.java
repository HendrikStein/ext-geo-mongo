package de.jt.mongo;

import java.util.List;

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
        System.out.println(locations.size() + " inserted into " + getGeoLocationCol().getName());
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
    public void testBoundingBox() {
        insertCertainGeoLocations();
        GeoPoint lowerLeft = new GeoPoint(49.74733, 6.63575);// Trier
        GeoPoint upperRight = new GeoPoint(51.53075, 9.07471);// Warburg
        GeoBoundingBox bbox = new GeoBoundingBox(lowerLeft, upperRight);

        // Print Bounding box as gpx output to plot it for example at http://gpso.de/maps/
        System.out.println(GPXUtils.bboxToTrack(bbox));

        MongoGeoService geoService = new MongoGeoService(getGeoLocationCol());
        List<GeoLocation> locationList = geoService.getLocations(bbox);

        // Expected Results are Cologne and Frankfurt
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(SampleGeoLocations.locationFrankfurt));
        Assert.assertTrue(locationList.contains(SampleGeoLocations.locationCologne));
    }

    @Test
    public void testBoundingBoxOverAntimeridianFitsIntoHalfSphere() {
        insertCertainGeoLocations();
        GeoPoint lowerLeft = new GeoPoint(16.67304, 121.11328);// Philippines
        GeoPoint upperRight = new GeoPoint(65.08833, -152.40234);// Alaska
        GeoBoundingBox bbox = new GeoBoundingBox(lowerLeft, upperRight);

        // Print Bounding box as gpx output to plot it for example at http://gpso.de/maps/
        System.out.println(GPXUtils.bboxToTrack(bbox));

        MongoGeoService geoService = new MongoGeoService(getGeoLocationCol());
        List<GeoLocation> locationList = geoService.getLocations(bbox);
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(SampleGeoLocations.locationAleutianIslands));
        Assert.assertTrue(locationList.contains(SampleGeoLocations.locationHiroshima));
    }

    @Test
    public void testBigBoundingBoxOverAntimeridian() {
        List<GeoLocation> europeList = SampleGeoLocations.getSampleForEurope();
        insertLocations(europeList); // This list is not expected in the result set

        int locationCount = 0;
        List<GeoLocation> ozeaniaList = SampleGeoLocations.getSampleForOzeania();
        insertLocations(ozeaniaList);
        locationCount += ozeaniaList.size(); // This list is expected in the result set

        List<GeoLocation> northAmericaList = SampleGeoLocations.getSampleForNorthAmerica();
        insertLocations(northAmericaList);
        locationCount += northAmericaList.size();// This list is expected in the result set

        GeoPoint lowerLeft = new GeoPoint(-54.85448, 56.60156); // Indian Ocean
        GeoPoint upperRight = new GeoPoint(77.73845, -41.75000); // Greenland
        GeoBoundingBox bbox = new GeoBoundingBox(lowerLeft, upperRight);
        MongoGeoService geoService = new MongoGeoService(getGeoLocationCol());
        List<GeoLocation> locationList = geoService.getLocations(bbox);

        Assert.assertEquals(locationCount, locationList.size());
        Assert.assertTrue(locationList.containsAll(SampleGeoLocations.getSampleForOzeania()));
        Assert.assertTrue(locationList.containsAll(SampleGeoLocations.getSampleForNorthAmerica()));
        Assert.assertFalse(locationList.containsAll(SampleGeoLocations.getSampleForEurope()));
    }

}
