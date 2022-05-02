package com.economy.database.models;

import com.economy.init.Economy;

import java.util.concurrent.TimeUnit;

public class EconomyUserInventoryEntry {
    private final float count;
    private final long timeAdded;

    public EconomyUserInventoryEntry(float count, long timeAdded) {
        this.count = count;
        this.timeAdded = timeAdded;
    }

    public String getName() {
        if(isGrowing()){
            return Economy.getConfig().readString("collectable_name");
        }
        if(isMature()){
            return Economy.getConfig().readString("currency_name");
        }
        if(isDead()){
            return "Dead";
        }
        return "UNKNOWN";
    }

    public float getCount() {
        return count;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public boolean isGrowing() {
        return System.currentTimeMillis() - timeAdded < TimeUnit.DAYS.toMillis(1);
    }

    public boolean isMature() {
        return System.currentTimeMillis() - timeAdded > TimeUnit.DAYS.toMillis(1)
                && System.currentTimeMillis() - timeAdded < TimeUnit.DAYS.toMillis(3);
    }

    public boolean isDead() {
        return System.currentTimeMillis() - timeAdded > TimeUnit.DAYS.toMillis(3);
    }
}
