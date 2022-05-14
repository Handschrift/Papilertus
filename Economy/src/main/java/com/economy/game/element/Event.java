package com.economy.game.element;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
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
        final ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("VOll schlecht!", 0.6f, 0.05f, Type.NEGATIVE));
        events.add(new Event("VOll gut!", 0.6f, 0.5f, Type.POSITIVE));
        final Event current = events.get(new Random().nextInt(events.size()));
        channel.sendMessage(current.getDescription()).queue();
        final EconomyUser economyUser = UserDatabase.fetch(member.getId(), member.getGuild().getId());
        economyUser.alterCoins(current.getChangeValue((float) economyUser.getCoins()));
        UserDatabase.updateUser(economyUser);
    }
}
