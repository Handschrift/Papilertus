package com.openpackagedbot.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class DatabaseConnection {

    private static MongoClient client;

    public static MongoClient getConnection() {
        if (client == null) {
            client = MongoClients.create(MongoClientSettings.builder().credential(MongoCredential.createCredential("test","admin", "omega".toCharArray())).applyConnectionString(new ConnectionString("mongodb://localhost/")).build());
        }
        return client;
    }
}
