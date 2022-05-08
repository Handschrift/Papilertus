package com.papilertus.init;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.papilertus.plugin.PluginDataStore;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

public final class DatabaseConnection {

    private static MongoClient client;
    private static final String databaseName = Config.getConfig().getDatabaseName();

    static MongoClient getConnection() {
        final Config config = Config.getConfig();
        if (client == null) {
            final MongoCredential credential = MongoCredential.createCredential(config.getDatabaseUsername(), "admin", config.getDatabasePassword().toCharArray());
            final MongoClientSettings.Builder settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(config.getDatabaseUrl()))
                    .codecRegistry(CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry()
                            , CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())));
            if (!config.getDatabaseUsername().isEmpty() && !config.getDatabasePassword().isEmpty()) {
                settings.credential(credential);
            }
            client = MongoClients.create(settings.build());
        }
        return client;
    }

    public static MongoClient getConnection(PluginDataStore.DatabaseSignature signature) {
        return getConnection();
    }

    public static MongoDatabase getBotDatabase(PluginDataStore.DatabaseSignature signature) {
        if (client == null)
            client = getConnection();
        return client.getDatabase(databaseName);
    }
}
