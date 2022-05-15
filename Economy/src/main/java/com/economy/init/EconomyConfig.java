package com.economy.init;

import com.economy.game.element.Event;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;

import java.util.ArrayList;

public class EconomyConfig {
    private String currencyName = "Plants";
    private String currencyIcon = ":deciduous_tree:";
    private String collectableName = "Seeds";
    private String collectableIcon = ":seedling:";
    private boolean coinOnMessageSent = true;
    private int coinMessageCooldown = 20;
    private boolean coinOnVoiceActivity = true;
    private float baseCoinOnMessageAmount = 4;
    private float baseCoinOnVoiceActivityAmount = 0.2f;
    private boolean enableWorkMinigame = true;
    private int workCooldown = 60;
    private float baseWorkGain = 6;
    private String convertCommandName = "plant";
    private float baseCollectablesOnBumpGain = 110;
    private int decimals = 0;
    private float baseDailyGain = 50;
    private ArrayList<GameUpgrade> upgrades = new ArrayList<>() {
        {
            add(new GameUpgrade("Monarch Butterfly", "Upgrades seed gain by voice", IncrementType.VOICE, ":butterfly:", 1.1F, 54.0F));
            add(new GameUpgrade("Periander Metalmark", "Upgrades seed gain by message", IncrementType.MESSAGE, ":butterfly:", 1.1F, 18.2F));
            add(new GameUpgrade("Mountain Apollo", "Upgrades seed gain by work", IncrementType.WORK, ":butterfly:", 1.1F, 51.3F));
            add(new GameUpgrade("Great Purple Hairstreak", "Upgrades seed gain by bump", IncrementType.BUMP, ":butterfly:", 1.8F, 100.4F));
            add(new GameUpgrade("Southern Dogface", "Upgrades seed gain by treasure", IncrementType.TREASURE, ":butterfly:", 1.5F, 30.2F));
            add(new GameUpgrade("Essex Skipper", "Upgrades seed gain per daily", IncrementType.DAILY, ":butterfly:", 1.1f, 35.0f));

        }
    };

    private boolean autoCollect = false;

    private int timeToGrowDuration = 1;
    private String timeToGrowUnit = "DAYS";
    private int timeToDieDuration = 3;
    private String timeDieUnit = "DAYS";

    private ArrayList<String> forecastNames = new ArrayList<>() {
        {
            add(":fire: Dry");
            add(":sunny: Sunny");
            add(":cloud_rain: Heavy rain");
        }
    };

    private ArrayList<Event> events = new ArrayList<>() {
        {
            add(new Event("A human burned your plants.", 0.5f, 0.05f, Event.Type.NEGATIVE));
            add(new Event("Tourists are planting seeds for you.", 0.5f, 0.05f, Event.Type.POSITIVE));
            add(new Event("Your forest is burning!", 0.01f, 0.25f, Event.Type.NEGATIVE));
            add(new Event("Overall pollution is deteriorating", 0.05f, 0.10f, Event.Type.POSITIVE));
        }
    };

    private int eventProbability = 5;

    public EconomyConfig() {

    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencyIcon() {
        return currencyIcon;
    }

    public String getCollectableName() {
        return collectableName;
    }

    public String getCollectableIcon() {
        return collectableIcon;
    }

    public boolean isCoinOnMessageSent() {
        return coinOnMessageSent;
    }

    public int getCoinMessageCooldown() {
        return coinMessageCooldown;
    }

    public boolean isCoinOnVoiceActivity() {
        return coinOnVoiceActivity;
    }

    public float getBaseCoinOnMessageAmount() {
        return baseCoinOnMessageAmount;
    }

    public float getBaseCoinOnVoiceActivityAmount() {
        return baseCoinOnVoiceActivityAmount;
    }

    public boolean isEnableWorkMinigame() {
        return enableWorkMinigame;
    }

    public int getWorkCooldown() {
        return workCooldown;
    }

    public float getBaseWorkGain() {
        return baseWorkGain;
    }

    public String getConvertCommandName() {
        return convertCommandName;
    }

    public float getBaseCollectablesOnBumpGain() {
        return baseCollectablesOnBumpGain;
    }

    public int getDecimals() {
        return decimals;
    }

    public float getBaseDailyGain() {
        return baseDailyGain;
    }

    public ArrayList<GameUpgrade> getUpgrades() {
        return upgrades;
    }

    public boolean isAutoCollect() {
        return autoCollect;
    }

    public int getTimeToGrowDuration() {
        return timeToGrowDuration;
    }

    public String getTimeToGrowUnit() {
        return timeToGrowUnit;
    }

    public int getTimeToDieDuration() {
        return timeToDieDuration;
    }

    public String getTimeDieUnit() {
        return timeDieUnit;
    }

    public ArrayList<String> getForecastNames() {
        return forecastNames;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public int getEventProbability() {
        return eventProbability;
    }
}
