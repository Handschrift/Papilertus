package com.economy.database.databases;

import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.papilertus.plugin.PluginDataStore;
import org.bson.Document;

import java.util.ArrayList;

public class UserDatabase {
    private static final PluginDataStore dataStore = Economy.getDataStore();

    public static EconomyUser fetch(String userId, String guildId) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        final EconomyUser result = dataStore.getEntry(document, EconomyUser.class);
        //Add User if it does not exist
        if (result == null) {
            addUser(new EconomyUser(userId, guildId));
            return new EconomyUser(userId, guildId);
        }
        return result;
    }

    public static void addUser(EconomyUser user) {
        dataStore.addEntry(user);
    }

    public static void updateUser(String userId, String guildId, String key, Object value) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        dataStore.modifyEntry(document, Updates.set(key, value));
    }

    public static void updateUser(String userId, String guildId, EconomyUser user) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        dataStore.modifyEntry(document, user);
    }

    public static void updateUser(EconomyUser user) {
        final Document document = new Document("_id.userId", user.getUserId());
        document.append("_id.guildId", user.getGuildId());
        dataStore.modifyEntry(document, user);
    }

    public static ArrayList<EconomyUser> getUsers(String guildId) {
        final Document filter = new Document("_id.guildId", guildId);
        return dataStore.getEntries(filter, EconomyUser.class, 10, Sorts.descending("weeklyCurrency"));
    }
}
