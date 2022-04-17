package com.economy.game.element;

public class GameUpgrade {
    final String name;
    final String description;
    final IncrementType incrementType;
    final String icon;
    final float incrementScale;

    public GameUpgrade(String name, String description, IncrementType incrementType, String icon, float incrementScale) {
        this.name = name;
        this.description = description;
        this.incrementType = incrementType;
        this.icon = icon;
        this.incrementScale = incrementScale;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public IncrementType getIncrementType() {
        return incrementType;
    }

    public String getIcon() {
        return icon;
    }
}
