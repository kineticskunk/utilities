package com.kineticskunk.utilities;

import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDataBaseObject {
	
	private MongoClient mongo;
	private MongoDatabase db;

	public MongoDataBaseObject(String mongoDBServer, String dbName) {
		mongo = new MongoClient(mongoDBServer, 27017);
		db = mongo.getDatabase(dbName);
	}
	
	public MongoCollection<Document> getMongoCollectionDocument(String collection) {
		return db.getCollection(collection);

	}
	
	public boolean collectionExists(final String collectionName, final String documentName) {
		MongoCollection<Document> col = getMongoCollectionDocument(collectionName) ;
	/*	col.
	    Set<String> collectionNames = db.getCollection(collectionName);
	    for (final String name : collectionNames) {
	        if (name.equalsIgnoreCase(collectionName)) {
	            return true;
	        }
	    }*/
	    return false;
	}
	

}
