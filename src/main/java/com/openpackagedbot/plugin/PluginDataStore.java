package com.openpackagedbot.plugin;

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

    @Deprecated
    public void addEntry(String s) {
        database.getCollection(data.getName()).insertOne(Document.parse(s));
    }

    public <T> void addEntry(T t, Class<T> c) {
        database.getCollection(data.getName(), c).insertOne(t);
    }

    @Deprecated
    public void modifyEntry(Bson filter, Bson update) {
        database.getCollection(data.getName()).updateOne(filter, update);
    }

    public <T> void modifyEntry(Bson filter, T t, Class<T> c) {
        database.getCollection(data.getName(), c).findOneAndReplace(filter, t);
    }

    @Deprecated
    public Document getEntry(Bson filter) {
        return database.getCollection(data.getName()).find(filter).first();
    }

    public <T> T getEntry(Bson filter, Class<T> c) {
        return database.getCollection(data.getName(), c).find(filter).first();
    }

    @Deprecated
    public void addEntry(String collectionName, String json) {
        database.getCollection(data.getName() + "_" + collectionName).insertOne(Document.parse(json));
    }

    public <T> void addEntry(String collectionName, T t, Class<T> c) {
        database.getCollection(data.getName() + "_" + collectionName, c).insertOne(t);
    }

    @Deprecated
    public void modifyEntry(String collectionName, Bson filter, Bson update) {
        database.getCollection(data.getName() + "_" + collectionName).updateOne(filter, update);
    }

    public <T> void modifyEntry(String collectionName, Bson filter, T t, Class<T> c) {
        database.getCollection(data.getName() + "_" + collectionName, c).findOneAndReplace(filter, t);
    }

    @Deprecated
    public Document getEntry(String collectionName, Bson filter) {
        return database.getCollection(data.getName() + "_" + collectionName).find(filter).first();
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
