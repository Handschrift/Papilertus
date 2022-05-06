package com.economy.database.models;

import com.economy.init.Economy;

import java.util.concurrent.TimeUnit;

public class EconomyUserInventoryEntry {
    private final float count;
    private final long timeAdded;

    private final long timeFinished;

    public EconomyUserInventoryEntry(float count, long timeAdded) {
        this.count = count;
        this.timeAdded = timeAdded;
        this.timeFinished = timeAdded + TimeUnit.valueOf(Economy.getEconomyConfig().getTimeToGrowUnit()).toMillis(Economy.getEconomyConfig().getTimeToGrowDuration());
    }

    public String getName() {
        if (isGrowing()) {
            return Economy.getEconomyConfig().getCollectableName();
        }
        if (isMature()) {
            return Economy.getEconomyConfig().getCurrencyName();
        }
        if (isDead()) {
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
        return System.currentTimeMillis() - timeAdded < TimeUnit.valueOf(Economy.getEconomyConfig().getTimeToGrowUnit()).toMillis(Economy.getEconomyConfig().getTimeToGrowDuration());
    }

    public boolean isMature() {
        return System.currentTimeMillis() - timeAdded > TimeUnit.valueOf(Economy.getEconomyConfig().getTimeToGrowUnit()).toMillis(Economy.getEconomyConfig().getTimeToGrowDuration())
                && System.currentTimeMillis() - timeAdded < TimeUnit.valueOf(Economy.getEconomyConfig().getTimeDieUnit()).toMillis(Economy.getEconomyConfig().getTimeToDieDuration());
    }

    public long getTimeFinished() {
        return timeFinished;
    }

    public boolean isDead() {
        return System.currentTimeMillis() - timeAdded > TimeUnit.valueOf(Economy.getEconomyConfig().getTimeDieUnit()).toMillis(Economy.getEconomyConfig().getTimeToDieDuration());
    }
}
