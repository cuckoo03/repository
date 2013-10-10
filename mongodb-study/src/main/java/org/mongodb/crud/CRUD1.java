package org.mongodb.crud;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class CRUD1 {

	public static void main(String[] args) throws UnknownHostException {
		MongoClient mongo = new MongoClient("54.250.217.195", 27017);
		DB db = mongo.getDB("test");
		boolean auth = db.authenticate("test", "test".toCharArray());
		System.out.println(auth);

		DBCollection coll = db.getCollection("test");

		// save
		BasicDBObject document = new BasicDBObject("name", "mongodb").append(
				"sex", "male");
		coll.insert(document);

		// find
		BasicDBObject findQ = new BasicDBObject("name", "mongodb");
		DBObject mydoc = coll.findOne(findQ);
		System.out.println("findOne():" + mydoc);

		// update
		BasicDBObject newDoc = new BasicDBObject("name", "mongodb2");
		BasicDBObject updateObj = new BasicDBObject("$set", newDoc);

		coll.update(findQ, updateObj);
		System.out.println("document updated:" + coll.findOne(newDoc));

		BasicDBObject updateObj2 = new BasicDBObject("$set", new BasicDBObject(
				"name", "mongodb3"));
		coll.update(newDoc, updateObj2);

		// dbcursor
		DBCursor cursor = coll.find();
		System.out.println("cursor");
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} finally {
			cursor.close();
		}

		// remove
		// BasicDBObject query2 = new BasicDBObject("name", "mongodb3");
		// coll.remove(query2);
		// BasicDBObject query = new BasicDBObject("name", "mongodb2");
		// coll.remove(query);
	}
}
