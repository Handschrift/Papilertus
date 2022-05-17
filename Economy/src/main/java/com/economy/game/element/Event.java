package com.economy.game.element;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.economy.util.MathUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Event {
    public enum Type {
        POSITIVE, NEGATIVE;
    }

    private final String description;
    private final float probability;
    private final float weight;
    private final Type type;

    public Event(String description, float probability, float weight, Type type) {
        this.description = description;
        this.probability = probability;
        this.weight = weight;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public float getProbability() {
        return probability;
    }

    public float getWeight() {
        return weight;
    }

    public Type getType() {
        return type;
    }

    public float getChangeValue(float coins) {
        switch (type) {
            case POSITIVE:
                return coins * weight;
            case NEGATIVE:
                return -coins * weight;
            default:
                //should never happen
                return 0;
        }
    }

    public static void callRandomEvent(Member member, MessageChannel channel) {
        final Event current = getRandom();
        final EconomyUser economyUser = UserDatabase.fetch(member.getId(), member.getGuild().getId());
        channel.sendMessage(current.getDescription() + " You " + (current.type == Type.POSITIVE ? " got " : " lost ") + MathUtils.round(Math.abs(current.getChangeValue((float) economyUser.getCoins())))
                + " " + Economy.getEconomyConfig().getCurrencyName()).queue();
        economyUser.alterCoins(current.getChangeValue((float) economyUser.getCoins()));
        UserDatabase.updateUser(economyUser);
    }

    private static Event getRandom() {
        final ArrayList<Event> events = Economy.getEconomyConfig().getEvents();
        final double p = Math.random();
        double cumulative = 0;
        Collections.shuffle(events);

        for (Event event : events) {
            cumulative += event.probability;
            if (p <= cumulative) {
                return event;
            }
        }

        return events.get(new Random().nextInt(events.size()));
    }
}
