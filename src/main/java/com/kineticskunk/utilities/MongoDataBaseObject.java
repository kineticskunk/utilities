package com.kineticskunk.utilities;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.json.simple.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDataBaseObject {
	
	private final Logger logger = LogManager.getLogger(MongoDataBaseObject.class.getName());
	private final Marker MONGODBOBJECT = MarkerManager.getMarker("MONGODBOBJECT");
	
	private MongoClient mongo;
	private DB db;
	private DBCollection collection;
	private DBObject dbObject;
	
	private static MongoDataBaseObject mgbo;
	
	/**
	 * Singleton Constructor
	 * @throws UnknownHostException 
	 */
	public static MongoDataBaseObject getInstance(String mongoDBServer, int port, String dbName) throws UnknownHostException {
		if (mgbo == null ) {
			synchronized (MongoDataBaseObject.class) {
				if (mgbo == null) {
					mgbo = new MongoDataBaseObject(mongoDBServer, port, dbName);
				}
			}
		}
		return mgbo;
	}
	
	public MongoDataBaseObject(String mongoDBServer, int port, String dbName) throws UnknownHostException {
		this.mongo = new MongoClient(mongoDBServer, port);
		this.db = mongo.getDB(dbName);
	}
	
	public DB getMongoDB() {
		return this.db;
	}
	
	public void setMondoDBObject(JSONObject jsonObject) {
		for(Iterator<?> iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
		    String collectionName = (String) iterator.next();
		    if (doesCollectionExist(this.db, collectionName)) {
		    	this.db.getCollection(collectionName).drop();
		    }
		    this.collection = this.db.getCollection(collectionName);
		    JSONObject j = (JSONObject) jsonObject.get(collectionName);
		    BasicDBObject bdoObject = new BasicDBObject();
		    this.logger.debug(MONGODBOBJECT, String.format("Converting collection %s into a HashMap", (char)34 + collectionName + (char)34));
		    bdoObject.putAll(convertJSONtoMap(j));
		    this.collection.insert(bdoObject);
		    this.logger.debug(MONGODBOBJECT, String.format("Loading mongoDB collection %s",  (char)34 + collectionName + (char)34));
		}
	}
	
	public HashMap<String, Object> convertJSONtoMap(JSONObject jsonObject) {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Iterator<?> iterator = jsonObject.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				map.put(key, value);
			}
			return map;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean doesCollectionExist(DB db, String collectionName) {
		DBCollection table = db.getCollection(collectionName);
		return (table.count()>0)?true:false;
	}
	
	public DBCollection getCollection(String collectionName) {
		if (doesCollectionExist(this.db, collectionName)) {
			return this.db.getCollection(collectionName);
	    }
		this.logger.warn(MONGODBOBJECT, String.format("Collection %s does not exist.", (char)34 + collectionName + (char)34));
		return null;
	}
	
	public String getFieldValue(String collection, String key) {
		DBCursor cursor = null;
		BasicDBObject query = new BasicDBObject();
		DBCollection coll = db.getCollection(collection);
		
		String value = null;
		cursor = coll.find(query);
		while (cursor.hasNext()) {
			value = cursor.next().get(key).toString();
		}
		return value;
	}
	
	public DBObject getMondoDBObject() {
		return this.dbObject;
	}
	
}
