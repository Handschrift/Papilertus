package com.openpackagedbot.init;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.openpackagedbot.plugin.PluginDataStore;

public final class DatabaseConnection {

    private static MongoClient client;

    static MongoClient getConnection() {
        Config config = Config.getConfig();
        if (client == null) {
            final MongoCredential credential = MongoCredential.createCredential(config.getDatabaseUsername(), "Papilertus", config.getDatabasePassword().toCharArray());
            final MongoClientSettings.Builder settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(config.getDatabaseUrl()));
            if(!config.getDatabaseUsername().isEmpty() || !config.getDatabasePassword().isEmpty()){
                settings.credential(credential);
            }
            client = MongoClients.create(settings.build());
        }
        return client;
    }

    public static MongoClient getConnection(PluginDataStore.DatabaseSignature signature) {
        return getConnection();
    }
}
