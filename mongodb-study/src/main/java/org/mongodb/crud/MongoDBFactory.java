package org.mongodb.crud;

import com.mongodb.DB;

public interface MongoDBFactory {
	DB getDB();
	DB getDB(String dbName);
}
