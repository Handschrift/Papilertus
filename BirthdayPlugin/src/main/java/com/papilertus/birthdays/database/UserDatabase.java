package com.papilertus.birthdays.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Updates;
import com.openpackagedbot.plugin.PluginDataStore;
import com.papilertus.birthdays.init.Birthdays;
import org.bson.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserDatabase {
    private static final PluginDataStore dataStore = Birthdays.getDataStore();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public BirthdayUser fetchUser(String userId, String guildId) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        final Document result = dataStore.getEntry(document);
        if (result == null) {
            return null;
        }
        return gson.fromJson(result.toJson(), BirthdayUser.class);
    }

    public void addUser(String userId, String guildId, LocalDate date, String timezone, int age) {
        JsonObject object = new JsonObject();
        JsonObject id = new JsonObject();
        id.addProperty("userId", userId);
        id.addProperty("guildId", guildId);
        object.add("_id", id);
        object.addProperty("userId", userId);
        object.addProperty("guildId", guildId);
        object.addProperty("birthday", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        object.add("wishlist", new JsonArray());
        object.addProperty("timezone", timezone);
        object.addProperty("age", age);
        dataStore.addEntry(gson.toJson(object));
    }

    public void addUser(BirthdayUser user) {
        JsonObject object = new JsonObject();
        JsonObject id = new JsonObject();
        id.addProperty("userId", user.getUserId());
        id.addProperty("guildId", user.getGuildId());
        object.add("_id", id);
        object.addProperty("userId", user.getUserId());
        object.addProperty("guildId", user.getUserId());
        object.addProperty("birthday", user.getBirthdayDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        object.add("wishlist", new JsonArray());
        object.addProperty("timezone", user.getTimezone());
        object.addProperty("age", user.getAge());
        dataStore.addEntry(gson.toJson(object));
    }

    public void deleteUser(String userId, String guildId) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        dataStore.modifyEntry(document, Updates.set("birthday", ""));
    }

    public ArrayList<BirthdayUser> getAllAfter(String guildId, LocalDate date) {
        final Document filter = new Document("_id.guildId", guildId);
        final ArrayList<BirthdayUser> results = new ArrayList<>();
        for (Document document : dataStore.getEntries(filter)) {
            final LocalDate localDate = LocalDate.parse(document.getString("birthday"));
            final LocalDate newDate = LocalDate.of(LocalDate.now().getYear(), localDate.getMonth(), localDate.getDayOfMonth());
            if (newDate.isAfter(LocalDate.now())) {
                results.add(gson.fromJson(document.toJson(), BirthdayUser.class));
            }
        }
        return results;
    }

    public ArrayList<BirthdayUser> getByDate(LocalDate date) {
        final LocalDate newDate = LocalDate.of(LocalDate.now().getYear(), date.getMonth(), date.getDayOfMonth());
        final Document filter = new Document("birthday", newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        final ArrayList<BirthdayUser> results = new ArrayList<>();
        for (Document document : dataStore.getEntries(filter)) {
            results.add(gson.fromJson(document.toJson(), BirthdayUser.class));
        }
        return results;
    }

    public void updateUser(String userId, String guildId, String key, Object value) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        dataStore.modifyEntry(document, Updates.set(key, value));
    }
}
