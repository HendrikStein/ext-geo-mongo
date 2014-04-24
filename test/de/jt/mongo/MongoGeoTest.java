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
    final GeoLocation locationCologne = new GeoLocation(new GeoPoint(50.91147, 6.94336), "Cologne");
    final GeoLocation locationFrankfurt = new GeoLocation(new GeoPoint(50.08689, 8.67920), "Frankfurt");
    final GeoLocation locationBerlin = new GeoLocation(new GeoPoint(52.49094, 13.38135), "Berlin");
    final GeoLocation locationAleutianIslands = new GeoLocation(new GeoPoint(51.90658, -176.70410),
            "Aleutian Islands");
    final GeoLocation locationHiroshima = new GeoLocation(new GeoPoint(34.11124, 133.81348), "Hiroshima");
    final GeoLocation locationLosAngeles = new GeoLocation(new GeoPoint(34.27878, -118.21289), "Los Angeles");
    final GeoLocation locationThailand = new GeoLocation(new GeoPoint(17.57858, 100.37109), "Thailand");

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Insert test location
        getGeoLocationCol().insert(locationCologne.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(locationFrankfurt.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(locationBerlin.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(locationAleutianIslands.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(locationHiroshima.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(locationLosAngeles.toMongo(), WriteConcern.SAFE);
        getGeoLocationCol().insert(locationThailand.toMongo(), WriteConcern.SAFE);

        // Print waypoints to plot on a map for example http://gpso.de/maps/
        // StringWriter strWriter = new StringWriter(4096);
        // PrintWriter out = new PrintWriter(new BufferedWriter(strWriter));
        // GPXUtils.writeHeader(out);
        // GPXUtils.writeWaypoint(out, locationCologne.getGeoPoint(), locationCologne.getDescription());
        // GPXUtils.writeWaypoint(out, locationFrankfurt.getGeoPoint(), locationFrankfurt.getDescription());
        // GPXUtils.writeWaypoint(out, locationBerlin.getGeoPoint(), locationBerlin.getDescription());
        // GPXUtils.writeWaypoint(out, locationAleutianIslands.getGeoPoint(), locationAleutianIslands.getDescription());
        // GPXUtils.writeWaypoint(out, locationHiroshima.getGeoPoint(), locationHiroshima.getDescription());
        // GPXUtils.writeWaypoint(out, locationLosAngeles.getGeoPoint(), locationLosAngeles.getDescription());
        // GPXUtils.writeWaypoint(out, locationThailand.getGeoPoint(), locationThailand.getDescription());
        // GPXUtils.writeFooter(out);
        // out.close();
        // System.out.println(strWriter.toString());
    }

    @Test
    public void testBoundingBox() {
        GeoPoint lowerLeft = new GeoPoint(49.74733, 6.63575);// Trier
        GeoPoint upperRight = new GeoPoint(51.53075, 9.07471);// Warburg
        GeoBoundingBox bbox = new GeoBoundingBox(lowerLeft, upperRight);

        // Print Bounding box as gpx output to plot it for example at http://gpso.de/maps/
        System.out.println(GPXUtils.bboxToTrack(bbox));

        MongoGeoService geoService = new MongoGeoService(getGeoLocationCol());
        List<GeoLocation> locationList = geoService.findLocation(bbox);

        // Expected Results are Cologne and Frankfurt
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(locationFrankfurt));
        Assert.assertTrue(locationList.contains(locationCologne));
    }

    @Test
    public void testBoundingBoxOverAntimeridian() {
        GeoPoint lowerLeft = new GeoPoint(16.67304, 121.11328);// Philippines
        GeoPoint upperRight = new GeoPoint(65.08833, -152.40234);// Alaska
        GeoBoundingBox bbox = new GeoBoundingBox(lowerLeft, upperRight);

        // Print Bounding box as gpx output to plot it for example at http://gpso.de/maps/
        System.out.println(GPXUtils.bboxToTrack(bbox));

        MongoGeoService geoService = new MongoGeoService(getGeoLocationCol());
        List<GeoLocation> locationList = geoService.findLocation(bbox);
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(locationAleutianIslands));
        Assert.assertTrue(locationList.contains(locationHiroshima));
    }
    
}
