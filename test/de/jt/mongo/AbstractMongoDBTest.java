package de.jt.mongo;

import junit.framework.TestCase;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import de.jt.db.MongoConstants;
import de.jt.model.GeoLocation;

/**
 * The Mongo base test to setup and tear down embedded mongo.
 * 
 * @author Hendrik Stein
 * 
 */
public abstract class AbstractMongoDBTest extends TestCase {

    /**
     * please store Starter or RuntimeConfig in a static final field if you want to use artifact store caching (or else
     * disable caching)
     */
    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    /** Mongo port. */
    private static final int port = 12345;

    /** Mongo executable deamon. */
    private MongodExecutable mongodExe;

    /** Mongo deamon. */
    private MongodProcess mongod;

    /** The Mongo client. */
    private MongoClient mongo;
    private static final String testDB = "testDB";

    @Override
    protected void setUp() throws Exception {
        mongodExe = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(port, Network.localhostIsIPv6()))
                .build());

        mongod = mongodExe.start();
        super.setUp();
        mongo = new MongoClient("localhost", port);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mongod.stop();
        mongodExe.stop();
    }

    /**
     * Get the 2d indexed geo {@link DBCollection}.
     * 
     * @return the geo indexed collection
     */
    public DBCollection getGeoLocationCol() {
        DB db = getMongo().getDB(testDB);
        DBCollection col = db.getCollection("geoCol");

        BasicDBObject ptIndex = new BasicDBObject();
        ptIndex.append(GeoLocation.MONGO_GEOPOINT, MongoConstants.INDEX_GEO);
        col.createIndex(ptIndex);

        return col;
    }

    /**
     * Get the {@link Mongo} client.
     * 
     * @return the mongo client
     */
    private Mongo getMongo() {
        return mongo;
    }

}