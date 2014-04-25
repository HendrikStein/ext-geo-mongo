package de.jt.mongo;

import de.jt.model.GeoBoundingBox;

/**
 * Wrap the query result.
 * 
 * @author Hendrik Stein
 * 
 */
public class QueryResult {
    /** The query result count. */
    private int resultCount = 1;

    /** Example bounding box for query result. */
    private GeoBoundingBox geoBoundingBox;

    /**
     * Create an instance.
     * 
     * @param geoBoundingBox the example geobounding box.
     */
    public QueryResult(GeoBoundingBox geoBoundingBox) {
        this.geoBoundingBox = geoBoundingBox;
    }

    /**
     * Increase count
     */
    public void increaseCount() {
        resultCount++;
    }

    /**
     * Get result count.
     * 
     * @return the result count
     */
    public int getResultCount() {
        return resultCount;
    }

    /**
     * Return the example geobounding box for this result
     * 
     * @return the geobounding box
     */
    public GeoBoundingBox getGeoBoundingBox() {
        return geoBoundingBox;
    }

}
