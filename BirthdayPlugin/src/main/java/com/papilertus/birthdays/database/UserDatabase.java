package com.papilertus.birthdays.database;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Updates;
import com.papilertus.birthdays.init.Birthdays;
import com.papilertus.plugin.PluginDataStore;
import org.bson.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserDatabase {
    private static final PluginDataStore dataStore = Birthdays.getDataStore();

    public static BirthdayUser fetchUser(String userId, String guildId) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        return dataStore.getEntry(document, BirthdayUser.class);
    }

    public static void addUser(String userId, String guildId, LocalDate date, String timezone, int age, int tries) {
        BirthdayUser user = new BirthdayUser(userId, guildId);
        user.setTries(tries);
        user.setAge(age);
        user.setBirthday(date);
        user.setTimezone(timezone);
        dataStore.addEntry(user);
    }

    public static void addUser(BirthdayUser user) {
        dataStore.addEntry(user);
    }

    public static void deleteUser(String userId, String guildId) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        dataStore.modifyEntry(document, Updates.set("birthday", ""));
    }

    public static ArrayList<BirthdayUser> getAllAfter(String guildId, LocalDate date) {
        final Document filter = new Document("_id.guildId", guildId);
        final ArrayList<BirthdayUser> results = new ArrayList<>();
        for (BirthdayUser user : dataStore.getEntries(filter, BirthdayUser.class)) {
            final LocalDate localDate = LocalDate.parse(user.getBirthday());
            final LocalDate newDate = LocalDate.of(LocalDate.now().getYear(), localDate.getMonth(), localDate.getDayOfMonth());
            if (newDate.isAfter(LocalDate.now())) {
                results.add(user);
            }
        }
        return results;
    }

    public static ArrayList<BirthdayUser> getByDate(LocalDate date) {
        final ArrayList<BirthdayUser> users = new ArrayList<>();
        final LocalDate newDate = LocalDate.of(LocalDate.now().getYear(), date.getMonth(), date.getDayOfMonth());
        final Document filter = new Document("birthday", newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        final FindIterable<Document> documents = dataStore.getIterableList(filter);
        for (Document document : documents) {
            users.add(new Gson().fromJson(document.toJson(), BirthdayUser.class));
        }
        return users;
    }

    public static void updateUser(String userId, String guildId, String key, Object value) {
        final Document document = new Document("_id.userId", userId);
        document.append("_id.guildId", guildId);
        dataStore.modifyEntry(document, Updates.set(key, value));
    }
}
