package com.funplugin.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class SpiritUser {
    @JsonProperty("_id")
    private final UserId _id;
    //Using this to provide extensibility, currently just the first element is used and added
    private final ArrayList<String> favoriteIds;

    public SpiritUser(String userId, String guildId, ArrayList<String> list) {
        this._id = new UserId(userId, guildId);
        this.favoriteIds = list;
    }

    public String getUserId() {
        return _id.getUserId();
    }

    public String getGuildId() {
        return _id.getGuildId();
    }


    public ArrayList<String> getFavoriteIds() {
        return favoriteIds;
    }

    private final static class UserId {
        final String userId;
        final String guildId;

        public UserId(String userId, String guildId) {
            this.userId = userId;
            this.guildId = guildId;
        }

        public String getUserId() {
            return userId;
        }

        public String getGuildId() {
            return guildId;
        }
    }
}
