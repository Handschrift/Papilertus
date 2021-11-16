package com.openpackagedbot.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.openpackagedbot.config.Config;

public class DatabaseConnection {

    private static MongoClient client;

    public static MongoClient getConnection() {
        Config config = Config.getConfig();
        if (client == null) {
            MongoCredential credential = MongoCredential.createCredential(config.getDatabaseUsername(), "admin", config.getDatabasePassword().toCharArray());
            client = MongoClients.create(MongoClientSettings.builder().credential(credential)
                    .applyConnectionString(new ConnectionString(config.getDatabaseUrl()))
                    .build());
        }
        return client;
    }
}
