package com.papilertus.birthdays.database;

import com.google.gson.annotations.SerializedName;

public class BirthdayGuild {
    @SerializedName(value = "_id")
    private final String id;
    private final String channelId;

    public BirthdayGuild(String id, String channelId) {
        this.id = id;
        this.channelId = channelId;
    }

    public String getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }
}
