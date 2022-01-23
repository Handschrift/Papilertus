package com.papilertus.birthdays.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.papilertus.birthdays.init.Birthdays;
import org.bson.Document;

public class GuildDatabase {
    public void addBirthdayChannel(String id) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final JsonObject object = new JsonObject();
        object.addProperty("_id", id);
        Birthdays.getDataStore().addEntry("guilds", gson.toJson(object));
    }

    public String getBirthdayChannel(String id) {
        final Document document = new Document("_id", id);
        return Birthdays.getDataStore().getEntry("guilds", document).getString("_id");
    }
}
