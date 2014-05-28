package de.jt.utils;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import de.jt.model.GeoBoundingBox;
import de.jt.model.GeoConstants;
import de.jt.model.GeoPoint;

/**
 * Tool to generate GPX files.
 * 
 * @author torsten.van.beeck (https://github.com/feueraustreter)
 */
public final class GPXUtils {

    /**
     * Write the GPX header.
     * 
     * @param w the PrintWriter to use
     */
    public static void writeHeader(PrintWriter w) {
        w.format("%s%n", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
        w.format("%s%n", "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"gpxutils\" version=\"1.1\"");
        w.format("%s%n", "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        w.format("%s%n", "     xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 "
                + "http://www.topografix.com/GPX/1/1/gpx.xsd\">");
    }

    /**
     * Write the GPX footer.
     * 
     * @param w the PrintWriter to use
     */
    public static void writeFooter(PrintWriter w) {
        w.format("</gpx>%n");
    }

    /**
     * Write a waypoint (geographic point).
     * 
     * @param w the PrintWriter to use
     * @param lat the latitude of the point
     * @param lon the longitude of the point
     * @param name the name of the point
     */
    public static void writeWaypoint(PrintWriter w, double lat, double lon, String name) {
        w.format(Locale.US, "%s%.8f%s%.8f%s%s%s%n", "  <wpt lat=\"", lat, "\" lon=\"", lon, "\"><name>", name,
                "</name></wpt>");
    }

    /**
     * Write a waypoint (geographic point).
     * 
     * @param w the PrintWriter to use
     * @param loc the point, an array[latitude, longitude]
     * @param name the name of the point
     */
    public static void writeWaypoint(PrintWriter w, double[] loc, String name) {
        double lat = loc[GeoConstants.LAT];
        double lon = loc[GeoConstants.LONG];
        w.format(Locale.US, "%s%.8f%s%.8f%s%s%s%n", "  <wpt lat=\"", lat, "\" lon=\"", lon, "\"><name>", name,
                "</name></wpt>");
    }

    /**
     * Write a waypoint (geographic point).
     * 
     * @param w the PrintWriter to use
     * @param point the point
     * @param name the name of the point
     */
    public static void writeWaypoint(PrintWriter w, GeoPoint point, String name) {
        w.format(Locale.US, "%s%.8f%s%.8f%s%s%s%n", "  <wpt lat=\"", point.getLatitude(), "\" lon=\"",
                point.getLongitude(), "\"><name>", name, "</name></wpt>");
    }

    /**
     * Convert a bounding box to a string in gpx format containing the bounding box as waypoints.
     * 
     * @param bbox the bounding box to convert
     * 
     * @return a string in gpx format
     */
    public static String bboxToTrack(GeoBoundingBox bbox) {
        GeoPoint lowerLeft = bbox.getLowerLeft();
        GeoPoint upperRight = bbox.getUpperRight();
        GeoPoint upperLeft = bbox.getUpperLeft();
        GeoPoint lowerRight = bbox.getLowerRight();

        StringWriter strWriter = new StringWriter(4096);

        PrintWriter out = new PrintWriter(new BufferedWriter(strWriter));
        GPXUtils.writeHeader(out);

        GPXUtils.writeWaypoint(out, lowerLeft, "lowerLeft");
        GPXUtils.writeWaypoint(out, upperLeft, "upperLeft");
        GPXUtils.writeWaypoint(out, upperRight, "upperRight");
        GPXUtils.writeWaypoint(out, lowerRight, "lowerRight");
        GPXUtils.writeWaypoint(out, lowerLeft, "lowerLeft");

        GPXUtils.writeFooter(out);
        out.close();

        return strWriter.toString();
    }

    /**
     * Utility class.
     */
    private GPXUtils() {
        // utility class.
    }

}
