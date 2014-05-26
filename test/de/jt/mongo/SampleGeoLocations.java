package de.jt.mongo;

import java.util.ArrayList;
import java.util.List;

import de.jt.model.GeoBoundingBox;
import de.jt.model.GeoLocation;
import de.jt.model.GeoPoint;

/**
 * Sample {@link GeoLocation}
 * 
 * @author Hendrik Stein
 * 
 */
public final class SampleGeoLocations {
    public static final GeoLocation locationCologne = new GeoLocation(new GeoPoint(50.91147, 6.94336), "Cologne");
    public static final GeoLocation locationFrankfurt = new GeoLocation(new GeoPoint(50.08689, 8.67920), "Frankfurt");
    public static final GeoLocation locationBerlin = new GeoLocation(new GeoPoint(52.49094, 13.38135), "Berlin");
    public static final GeoLocation locationAleutianIslands = new GeoLocation(new GeoPoint(51.90658, -176.70410),
            "Aleutian Islands");
    public static final GeoLocation locationHiroshima = new GeoLocation(new GeoPoint(34.11124, 133.81348), "Hiroshima");
    public static final GeoLocation locationLosAngeles = new GeoLocation(new GeoPoint(34.27878, -118.21289),
            "Los Angeles");
    public static final GeoLocation locationThailand = new GeoLocation(new GeoPoint(17.57858, 100.37109), "Thailand");

    private SampleGeoLocations() {
        // Utility class
    }

    /**
     * Get a sample list of {@link GeoLocation} for Europe.
     * 
     * This sample list does't raise claim to cover the whole continent.
     * 
     * @return a list of geo locations
     */
    public static List<GeoLocation> getSampleForEurope() {
        GeoPoint lowerLeft = new GeoPoint(37.87626, -7.08417);
        GeoPoint upperRight = new GeoPoint(68.21849, 32.34375);
        return getSampleForBoundingBox(new GeoBoundingBox(lowerLeft, upperRight));
    }

    /**
     * Get a sample list of {@link GeoLocation} for South America.
     * 
     * This sample list does't raise claim to cover the whole continent.
     * 
     * @return a list of geo locations
     */
    public static List<GeoLocation> getSampleForSouthAmerica() {
        GeoPoint lowerLeft = new GeoPoint(-53.20274, -73.12500);
        GeoPoint upperRight = new GeoPoint(2.32297, -53.08593);
        return getSampleForBoundingBox(new GeoBoundingBox(lowerLeft, upperRight));
    }

    /**
     * Get a sample list of {@link GeoLocation} for North America
     * 
     * This sample list does't raise claim to cover the whole continent.
     * 
     * @return a list of geo locations
     */
    public static List<GeoLocation> getSampleForNorthAmerica() {
        GeoPoint lowerLeftCentral = new GeoPoint(37.03906, -123.09980);
        GeoPoint upperRightCentral = new GeoPoint(65.45598, -89.29687);
        List<GeoLocation> sampleList = getSampleForBoundingBox(new GeoBoundingBox(lowerLeftCentral, upperRightCentral));

        GeoPoint lowerLeftAlaska = new GeoPoint(60.33871, -164.93573);
        GeoPoint upperRightAlaska = new GeoPoint(69.73143, -143.43750);
        sampleList.addAll(getSampleForBoundingBox(new GeoBoundingBox(lowerLeftAlaska, upperRightAlaska)));

        return sampleList;
    }

    /**
     * Get a sample list of {@link GeoLocation} for Australia.
     * 
     * This sample list does't raise claim to cover the whole continent.
     * 
     * @return a list of geo locations
     */
    public static List<GeoLocation> getSampleForOzeania() {
        GeoPoint lowerLeftAustralia = new GeoPoint(-34.43263, 116.31427);
        GeoPoint upperRightAustralia = new GeoPoint(-18.44313, 143.43750);
        List<GeoLocation> sampleList = getSampleForBoundingBox(new GeoBoundingBox(lowerLeftAustralia,
                upperRightAustralia));

        GeoPoint lowerLeftNZ = new GeoPoint(-45.68959, 167.64239);
        GeoPoint upperRightNZ = new GeoPoint(-36.42570, 175.78125);
        sampleList.addAll(getSampleForBoundingBox(new GeoBoundingBox(lowerLeftNZ, upperRightNZ)));

        return sampleList;
    }

    /**
     * Get a sample list of {@link GeoLocation} for Africa.
     * 
     * This sample list does't raise claim to cover the whole continent.
     * 
     * @return a list of geo locations
     */
    public static List<GeoLocation> getSampleForAfrica() {
        GeoPoint lowerLeft = new GeoPoint(-24.34547, 15.06427);
        GeoPoint upperRight = new GeoPoint(27.56186, 30.93751);
        return getSampleForBoundingBox(new GeoBoundingBox(lowerLeft, upperRight));
    }

    /**
     * Get a sample list of {@link GeoLocation} for Antarctica.
     * 
     * This sample list does't raise claim to cover the whole continent.
     * 
     * @return a list of geo locations
     */
    public static List<GeoLocation> getSampleForAntarctica() {
        GeoPoint lowerLeft = new GeoPoint(-84.72006, -51.02947);
        GeoPoint upperRight = new GeoPoint(-71.00445, 143.43751);
        return getSampleForBoundingBox(new GeoBoundingBox(lowerLeft, upperRight));
    }

    /**
     * Get a sample list of {@link GeoLocation} for Asia.
     * 
     * This sample list does't raise claim to cover the whole continent.
     * 
     * @return a list of geo locations
     */
    public static List<GeoLocation> getSampleForAsia() {
        GeoPoint lowerLeftIndonesia = new GeoPoint(-7.86162, 106.47053);
        GeoPoint upperRightIndonesia = new GeoPoint(4.07798, 117.42190);
        List<GeoLocation> sampleList = getSampleForBoundingBox(new GeoBoundingBox(lowerLeftIndonesia,
                upperRightIndonesia));

        GeoPoint lowerLeftRussia = new GeoPoint(49.05344, 84.67366);
        GeoPoint upperRightRussia = new GeoPoint(68.60652, 179.29690);
        sampleList.addAll(getSampleForBoundingBox(new GeoBoundingBox(lowerLeftRussia, upperRightRussia)));

        return sampleList;
    }

    /**
     * Get a list of {@link GeoLocation} for the whole world. Add 64800(360*180) geo locations.
     * 
     * @return a list of geo locations
     */
    public static List<GeoLocation> getWholeWorld() {
        GeoPoint lowerLeft = new GeoPoint(-90d, -180d);
        GeoPoint upperRight = new GeoPoint(90d, 180d);
        return getSampleForBoundingBox(new GeoBoundingBox(lowerLeft, upperRight));
    }

    /**
     * Create a random list of {@link GeoLocation} for a given {@link GeoBoundingBox}
     * 
     * @param bbox the bounding box
     * @return a list of geo locations
     */
    private static List<GeoLocation> getSampleForBoundingBox(GeoBoundingBox bbox) {
        List<GeoLocation> locations = new ArrayList<>();
        int locAmount = 0;
        GeoLocation location;
        /** Rotate south to north. */
        for (double lat = bbox.getLowerLeft().getLatitude(); lat <= bbox.getUpperRight().getLatitude(); lat++) {
            /** Rotate east to west. */
            for (double lon = bbox.getLowerLeft().getLongitude(); lon <= bbox.getUpperRight().getLongitude(); lon++) {
                locAmount++;
                location = new GeoLocation(new GeoPoint(lat, lon), "Number: " + locAmount);
                locations.add(location);
            }
        }
        return locations;
    }
}
