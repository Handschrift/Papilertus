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
    private static final PluginDataStore dataStore = Economy.getDataStore();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public EconomyUser fetchUser(String userId, String guildId) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        final Document result = dataStore.getEntry(document);
        //Add User if it does not exist
        if (result == null) {
            addUser(new EconomyUser(userId, guildId));
            return new EconomyUser(userId, guildId);
        }
        return gson.fromJson(result.toJson(), EconomyUser.class);
    }

    public void addUser(EconomyUser user) {
        final JsonObject object = gson.fromJson(gson.toJson(user), JsonObject.class);
        dataStore.addEntry(user);
    }

    public void updateUser(String userId, String guildId, String key, Object value) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        dataStore.modifyEntry(document, Updates.set(key, value));
    }
}
