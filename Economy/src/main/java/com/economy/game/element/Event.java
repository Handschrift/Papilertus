package com.economy.game.element;

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

    public float getValue(float coins) {
        switch (type) {
            case POSITIVE:
                return coins + (coins * weight);
            case NEGATIVE:
                return coins - (coins * weight);
            default:
                //should never happen
                return coins;
        }
    }
}
