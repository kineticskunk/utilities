package com.kineticskunk.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	
	private String mongoHost;
	private int mongoPort;
	private String mongoDB;
	
	private MongoClient mongo;
	private DB db;
	private DBCollection collection;
	private DBObject dbObject;
	
	private String collectionName;
	
	private static MongoDataBaseObject mgbo;
	
	/**
	 * Singleton Constructor
	 * @throws UnknownHostException 
	 */
	public static MongoDataBaseObject getInstance() throws UnknownHostException {
		if (mgbo == null ) {
			synchronized (MongoDataBaseObject.class) {
				if (mgbo == null) {
					mgbo = new MongoDataBaseObject();
				}
			}
		}
		return mgbo;
	}
	
	public MongoDataBaseObject() throws UnknownHostException {
		this.mongoHost = null;
		this.mongoPort = 0;
		this.mongoDB = null;
		this.db = null;
		this.collection = null;
		this.dbObject = null;
		this.collectionName = null;
	}
	
	public MongoDataBaseObject(String mongoHost, int port) throws UnknownHostException {
		this();
		this.mongo = new MongoClient(mongoHost, port);
	}
	
	public MongoDataBaseObject(String mongoHost, int mongoPort, String mongoDB) throws UnknownHostException {
		this(mongoHost, mongoPort);
		this.db = mongo.getDB(mongoDB);
	}
	
	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}
	
	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}
	
	public void setMongoDB(String mongoDB) {
		this.mongoDB = mongoDB;
	}
	
	public void setMongoClient() {
		try {
			this.mongo = new MongoClient(this.mongoHost, this.mongoPort);
		} catch (UnknownHostException e) {
			this.mongo = null;
			e.printStackTrace();
		}
	}
	
	public void setMongoDBObject() {
		this.db = mongo.getDB(this.mongoDB);
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
	
	public void setCollection(String collectionName) {
		this.collectionName = collectionName;
	}
	
	public DBCollection getCollection() {
		if (doesCollectionExist(this.db, this.collectionName)) {
			return this.db.getCollection(this.collectionName);
	    }
		this.logger.warn(MONGODBOBJECT, String.format("Collection %s does not exist.", (char)34 + collectionName + (char)34));
		return null;
	}
	
	public String getFieldValue(String key) {
		DBCursor cursor = null;
		BasicDBObject query = new BasicDBObject();
		String value = null;
		cursor = this.getCollection().find(query);
		while (cursor.hasNext()) {
			value = cursor.next().get(key).toString();
		}
		return value;
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
	
	public void insertValueIntoColletion(String collection, String key, String value) {
		BasicDBObject dbObj = new BasicDBObject();
		dbObj.put(key, value);
		this.setCollection(collection);
		this.getCollection().insert(dbObj);
	}
	
	/**
	 * 
	 * @param mongoTable
	 * @param tableFile
	 */
	public void writeFileToMongoDBTable(BasicDBObject mongoTable, String tableFile) {
		String  thisLine = null;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(tableFile));
			try{
				while ((thisLine = br.readLine()) != null) {
					String[] array = thisLine.split(",");
					mongoTable.put(array[0].toString(), array[1].toString().replaceAll("\"", ""));
					collection.save(mongoTable);
				}
				br.close();

			}catch(Exception e){
				logger.fatal("File not found exception occured");
			}

		} catch (FileNotFoundException fnfe) {
			logger.fatal("File not found exception occured");
		}
	}
	
	public void startMongoDB(String dbPath) {
		CommandLineInteractor cli = new CommandLineInteractor();
		cli.executeRuntimeCommand("mongodb --dbpath " + dbPath, true);
	}
	
}
