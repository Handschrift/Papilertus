package com.economy.database.databases;

import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Updates;
import com.openpackagedbot.plugin.PluginDataStore;
import org.bson.Document;

public class UserDatabase {
    private static final PluginDataStore dataStore = Economy.getDataStore();;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public EconomyUser fetchUser(String userId, String guildId) {
        JsonObject id = new JsonObject();
        id.addProperty("userId", userId);
        id.addProperty("guildId", guildId);
        Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        return gson.fromJson(dataStore.getEntry(document).toJson(), EconomyUser.class);
    }

    public void addUser(String userId, String guildId) {
        JsonObject object = new JsonObject();
        JsonObject id = new JsonObject();
        id.addProperty("userId", userId);
        id.addProperty("guildId", guildId);
        object.add("_id", id);
        object.addProperty("coins", 0);
        dataStore.addEntry(gson.toJson(object));
    }
    public void updateUser(String userId, String guildId, String key, Object value){
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        dataStore.modifyEntry(document, Updates.set(key, value));
    }
}
