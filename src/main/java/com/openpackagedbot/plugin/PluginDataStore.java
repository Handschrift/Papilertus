package com.openpackagedbot.plugin;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
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

    public <T> void addEntry(T t) {
        database.getCollection(data.getName()).insertOne(Document.parse(new Gson().toJson(t)));
    }

    public void modifyEntry(Bson filter, Bson update) {
        database.getCollection(data.getName()).updateOne(filter, update);
    }

    public <T> void modifyEntry(Bson filter, T t) {
        database.getCollection(data.getName()).findOneAndReplace(filter, Document.parse(new Gson().toJson(t)));
    }

    public <T> T getEntry(Bson filter, Class<T> c) {
        final Document result = database.getCollection(data.getName()).find(filter).first();
        return result == null ? null : new Gson().fromJson(result.toJson(), c);
    }

    public <T> void addEntry(String collectionName, T t) {
        database.getCollection(data.getName() + "_" + collectionName).insertOne(Document.parse(new Gson().toJson(t)));
    }

    public <T> void modifyEntry(String collectionName, Bson filter, T t) {
        database.getCollection(data.getName() + "_" + collectionName).findOneAndReplace(filter, Document.parse(new Gson().toJson(t)));
    }

    public <T> T getEntry(String collectionName, Bson filter, Class<T> c) {
        return database.getCollection(data.getName() + "_" + collectionName, c).find(filter).first();
    }

    @Deprecated
    public ArrayList<Document> getEntries(Bson filter) {
        final ArrayList<Document> entries = new ArrayList<>();
        final MongoCursor<Document> cursor = database.getCollection(data.getName()).find(filter).cursor();
        while (cursor.hasNext()) {
            entries.add(cursor.next());
        }
        cursor.close();
        return entries;
    }

    public <T> FindIterable<T> getEntries(Bson filter, Class<T> c) {
        return database.getCollection(data.getName(), c).find();
    }

    public void deleteEntry(Bson filter) {
        database.getCollection(data.getName()).deleteOne(filter);
    }
}
