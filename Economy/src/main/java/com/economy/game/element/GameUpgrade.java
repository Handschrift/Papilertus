package com.economy.game.element;

import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.economy.util.MathUtils;

public class GameUpgrade {
    private final String name;
    private final String description;
    private final IncrementType incrementType;
    private final String icon;
    private final float incrementScale;
    private final float basePrice;

    public GameUpgrade(String name, String description, IncrementType incrementType, String icon, float incrementScale, float basePrice) {
        this.name = name;
        this.description = description;
        this.incrementType = incrementType;
        this.icon = icon;
        this.incrementScale = incrementScale;
        this.basePrice = basePrice;
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

    public float getBasePrice() {
        return basePrice;
    }

    public float getIncrementScale() {
        return incrementScale;
    }

    public static float getAggregatedUpgradeCoefficient(EconomyUser user, IncrementType type) {
        float coefficient = 1;
        for (GameUpgrade upgrade : Economy.getConfig().readType("upgrades", GameUpgrade.class)) {
            if (upgrade.getIncrementType().equals(type)) {
                coefficient += (upgrade.getIncrementScale() * user.getUpgradeLevel(upgrade.getName()));
            }
        }
        return MathUtils.round(coefficient); //rounding to two decimal places
    }

    public float getUpgradeCoefficient(EconomyUser user) {
        final float coefficient = (float) (getBasePrice() * Math.sqrt(user.getUpgradeLevel(getName())));
        return MathUtils.round(coefficient);
    }
}
