package com.papilertus.birthdays.database;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BirthdayUser {
    @SerializedName(value = "_id")
    private final BirthdayUserKey id;
    private int age;
    private String timezone;
    private String birthday;
    private int tries;
    private final ArrayList<String> wishlist;

    private static final class BirthdayUserKey {
        private final String userId;
        private final String guildId;

        public BirthdayUserKey(String userId, String guildId) {
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

    public BirthdayUser(String userId, String guildId) {
        this.id = new BirthdayUserKey(userId, guildId);
        wishlist = new ArrayList<>();
    }

    public String getUserId() {
        return this.id.userId;
    }

    public String getGuildId() {
        return this.id.guildId;
    }

    public String getBirthday() {
        return birthday;
    }

    public LocalDate getBirthdayDate() {
        return LocalDate.parse(birthday);
    }

    public ArrayList<String> getWishlist() {
        return wishlist;
    }

    public void addWish(String item) {
        wishlist.add(item);
        UserDatabase.updateUser(getUserId(), getGuildId(), "wishlist", wishlist);
    }

    public void removeWish(String item) {
        wishlist.remove(item);
        UserDatabase.updateUser(getUserId(), getGuildId(), "wishlist", wishlist);
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        UserDatabase.updateUser(getUserId(), getGuildId(), "birthday", birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        UserDatabase.updateUser(getUserId(), getGuildId(), "age", age);

    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
        UserDatabase.updateUser(getUserId(), getGuildId(), "timezone", timezone);
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
        UserDatabase.updateUser(getUserId(), getGuildId(), "tries", tries);
    }
}
