package com.openpackagedbot.plugin;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.openpackagedbot.init.DatabaseConnection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

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

    public void createCollection(String name) {
        database.createCollection(data.getName() + "_" + name);
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

    public void addEntry(String collectionName, String json) {
        database.getCollection(data.getName() + "_" + collectionName).insertOne(Document.parse(json));
    }

    public void modifyEntry(String collectionName, Bson filter, Bson update) {
        database.getCollection(data.getName() + "_" + collectionName).updateOne(filter, update);
    }

    public Document getEntry(String collectionName, Bson filter) {
        return database.getCollection(data.getName() + "_" + collectionName).find(filter).first();
    }

    public ArrayList<Document> getEntries(Bson filter) {
        final ArrayList<Document> entries = new ArrayList<>();
        final MongoCursor<Document> cursor = database.getCollection(data.getName()).find(filter).cursor();
        while (cursor.hasNext()) {
            entries.add(cursor.next());
        }
        cursor.close();
        return entries;
    }

    public void deleteEntry(Bson filter) {
        database.getCollection(data.getName()).deleteOne(filter);
    }
}
