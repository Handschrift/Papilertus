package com.papilertus.birthdays.database;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BirthdayUser {
    private final String userId;
    private final String guildId;
    private int age;
    private String timezone;
    private String birthday;
    private int tries;
    private final ArrayList<String> wishlist;

    public BirthdayUser(String userId, String guildId) {
        this.userId = userId;
        this.guildId = guildId;
        wishlist = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getGuildId() {
        return guildId;
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
        final UserDatabase database = new UserDatabase();
        database.updateUser(getUserId(), getGuildId(), "wishlist", wishlist);
    }

    public void removeWish(String item) {
        wishlist.remove(item);
        final UserDatabase database = new UserDatabase();
        database.updateUser(getUserId(), getGuildId(), "wishlist", wishlist);
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final UserDatabase database = new UserDatabase();
        database.updateUser(getUserId(), getGuildId(), "birthday", birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        final UserDatabase database = new UserDatabase();
        database.updateUser(getUserId(), getGuildId(), "age", age);

    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
        final UserDatabase database = new UserDatabase();
        database.updateUser(getUserId(), getGuildId(), "timezone", timezone);
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
        final UserDatabase database = new UserDatabase();
    }
}
