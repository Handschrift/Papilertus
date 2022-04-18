package com.economy.database.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class EconomyUser {
    @SerializedName(value = "_id")
    private final EconomyUserKey id;
    private double coins = 0;
    private final JsonObject upgradeCounts = new JsonObject();

    public EconomyUser(String userId, String guildId) {
        this.id = new EconomyUserKey(userId, guildId);
    }

    private EconomyUserKey getKey() {
        return this.id;
    }

    public String getUserId() {
        return getKey().getUserId();
    }

    public String getGuildId() {
        return getKey().getGuildId();
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public void addCoins(double coins) {
        this.coins += coins;
    }

    public void removeCoins(double coins) {
        this.coins -= coins;
    }

    public String toJson() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    private static final class EconomyUserKey {
        private final String userId;
        private final String guildId;

        public EconomyUserKey(String userId, String guildId) {
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

    public JsonObject getUpgradeCounts() {
        return upgradeCounts;
    }

    public void addUpgrades(String name) {
        if (upgradeCounts.has(name))
            upgradeCounts.addProperty(name, upgradeCounts.get(name).getAsInt() + 1);
        else
            upgradeCounts.addProperty(name, 1);

    }
}
