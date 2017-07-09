package com.kineticskunk.utilities;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


import org.json.simple.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import com.mongodb.util.JSON;

public class MongoDataBaseObject {
	
	private MongoClient mongo;
	private DB db;
	private DBCollection collection;
	private DBObject dbObject;
	
	public MongoDataBaseObject(String mongoDBServer, int port, String dbName) throws UnknownHostException {
		this.mongo = new MongoClient(mongoDBServer, port);
		this.db = mongo.getDB(dbName);
		
	}
	
	public void setMondoDBObject(JSONObject jsonObject) {
		
		
		for(Iterator<?> iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
		    String key = (String) iterator.next();
		    if (isCollectionExists(this.db, key)) {
		    	this.db.getCollection(key).drop();
		    	
		    }
		    
		    collection = this.db.getCollection(key);
		    
		    JSONObject j = (JSONObject) jsonObject.get(key);
		    
		    BasicDBObject bdoObject = new BasicDBObject();
		    bdoObject.putAll(convertJSONtoMap(j));
		    collection.insert(bdoObject);
		    
		    System.out.println("Collection name = " + this.db.getCollection(key).getName());
		    
		    DBCursor cursorDoc = collection.find();
			while (cursorDoc.hasNext()) {
				System.out.println(cursorDoc.next());
			}

			System.out.println("Done");
		}
	}
	
	public HashMap<String, Object> convertJSONtoMap(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Iterator<?> iterator = jsonObject.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			map.put(key, value);
		}
		return map;
	}
	
	public static boolean isCollectionExists(DB db, String collectionName) {
		DBCollection table = db.getCollection(collectionName);
		return (table.count()>0)?true:false;
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
