package com.economy.database.models;

import com.economy.database.databases.UserDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;


class EconomyUserKey {
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

public class EconomyUser {
    @SerializedName(value = "_id")
    private final EconomyUserKey id;
    private double coins = 0;

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

    public static EconomyUser fetch(String userId, String guildId){
        final UserDatabase database = new UserDatabase();
        return database.fetchUser(userId, guildId);
    }

    public void setCoins(double coins) {
        final UserDatabase database = new UserDatabase();
        database.updateUser(getUserId(), getGuildId(), "coins", coins);
        this.coins = coins;
    }

    public void addCoins(double coins) {
        final UserDatabase database = new UserDatabase();
        database.updateUser(getUserId(), getGuildId(), "coins", getCoins() + coins);
        this.coins += coins;
    }

    public void removeCoins(double coins) {
        final UserDatabase database = new UserDatabase();
        database.updateUser(getUserId(), getGuildId(), "coins", getCoins() - coins);
        this.coins -= coins;
    }

    public String toJson() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
