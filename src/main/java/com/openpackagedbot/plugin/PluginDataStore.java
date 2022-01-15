package com.openpackagedbot.plugin;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.openpackagedbot.init.DatabaseConnection;
import org.bson.Document;
import org.bson.conversions.Bson;

public final class PluginDataStore {

    public static final class DatabaseSignature {
        private DatabaseSignature() {

        }
    }

    private final DatabaseSignature signature = new DatabaseSignature();
    private final PluginData data;
    private final MongoClient client = DatabaseConnection.getConnection(signature);
    private final MongoDatabase database = client.getDatabase("Papilertus");

    public PluginDataStore(PluginData data) {
        this.data = data;
    }

    public void createCollection() {
        database.createCollection(data.getName());
    }

    public void addEntry(String json) {
        database.getCollection(data.getName()).insertOne(Document.parse(json));
    }

    public void modifyEntry(Bson filter, Bson update) {
        database.getCollection(data.getName()).updateOne(filter, update);
    }

    public Document getEntry(Bson filter) {
        return database.getCollection(data.getName()).find(filter).first();
    }

    public void deleteEntry(Bson filter) {
        database.getCollection(data.getName()).deleteOne(filter);
    }
}
