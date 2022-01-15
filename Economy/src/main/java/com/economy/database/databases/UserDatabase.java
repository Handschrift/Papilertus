package com.economy.database.databases;

import com.economy.database.models.EconomyUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.openpackagedbot.plugin.PluginDataStore;
import org.bson.Document;

public class UserDatabase {
    private final PluginDataStore dataStore;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UserDatabase(PluginDataStore dataStore) {
        this.dataStore = dataStore;
    }

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
}
