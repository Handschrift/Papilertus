package com.papilertus.birthdays.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.papilertus.birthdays.init.Birthdays;
import org.bson.Document;

public class GuildDatabase {
    public void addBirthdayChannel(String guildId, String channelId) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final JsonObject object = new JsonObject();
        object.addProperty("_id", guildId);
        object.addProperty("channelId", channelId);
        Birthdays.getDataStore().addEntry("guilds", gson.toJson(object));
    }

 /*   public String getBirthdayChannel(String guildId) {
        final Document document = new Document("_id", guildId);
        System.out.println(guildId + "gd");
        return Birthdays.getDataStore().getEntry("guilds", document).getString("channelId");
    }*/
}
