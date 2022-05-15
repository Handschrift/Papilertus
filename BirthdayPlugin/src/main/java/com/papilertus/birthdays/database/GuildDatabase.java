package com.papilertus.birthdays.database;

import com.papilertus.birthdays.init.Birthdays;
import org.bson.Document;

public class GuildDatabase {
    public static void addBirthdayGuild(String guildId, String channelId) {
        Birthdays.getDataStore().addEntry("guilds", new BirthdayGuild(guildId, channelId));
    }

    public static BirthdayGuild getBirthdayGuild(String guildId) {
        final Document document = new Document("_id", guildId);
        return Birthdays.getDataStore().getEntry("guilds", document, BirthdayGuild.class);
    }
}
