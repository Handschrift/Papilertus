package com.economy.database.models;

import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.game.element.ShopButton;
import com.economy.init.Economy;
import com.economy.util.MathUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.papilertus.gui.button.DiscordButton;
import com.papilertus.gui.generator.PapilertusMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.concurrent.TimeUnit;


public class EconomyUser {
    @SerializedName(value = "_id")
    private final EconomyUserKey id;
    private double coins = 0;
    private double collectables = 0;
    private final JsonObject upgradeCounts = new JsonObject();
    private long lastWorkCooldown = 0;

    private float receivedToday = 0;
    private float sentToday = 0;

    private long lastReceived = 0;
    private long lastSent = 0;
    private long lastDaily = 0;

    private float weeklyCurrency = 0;

    private final EconomyUserInventory inventory = new EconomyUserInventory();

    public EconomyUser(String userId, String guildId) {
        this.id = new EconomyUserKey(userId, guildId);
    }

    private EconomyUserKey getKey() {
        return this.id;
    }

    public String getUserId() {
        return getKey().getUserId();
    }

    public String getGuildId() {
        return getKey().getGuildId();
    }

    public double getCoins() {
        return MathUtils.round(coins);
    }

    public void setCoins(float coins) {
        this.coins = coins;
    }

    public void addCoins(double coins) {
        this.weeklyCurrency += MathUtils.round(coins);
        this.coins += MathUtils.round(coins);
    }

    public void alterCoins(double coins) {
        if (coins < 0) {
            removeCoins(coins);
        } else {
            addCoins(coins);
        }
    }

    public void removeCoins(double coins) {
        this.coins -= MathUtils.round(coins);
    }

    public String toJson() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }


    private static final class EconomyUserKey {
        private final String userId;
        private final String guildId;

        public EconomyUserKey(String userId, String guildId) {
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

    public JsonObject getUpgradeCounts() {
        return upgradeCounts;
    }

    public int getUpgradeLevel(String name) {
        return getUpgradeCounts().get(name) == null ? 1 : getUpgradeCounts().get(name).getAsInt();
    }

    public void addUpgrades(String name) {
        if (upgradeCounts.has(name))
            upgradeCounts.addProperty(name, upgradeCounts.get(name).getAsInt() + 1);
        else
            upgradeCounts.addProperty(name, 2); // implicit 1, so set to 2 if it gets upgraded the first time

    }

    public long getLastWorkCooldown() {
        return lastWorkCooldown;
    }

    public boolean canWork() {
        return lastWorkCooldown == 0 || (System.currentTimeMillis() - lastWorkCooldown > TimeUnit.MINUTES.toMillis(Economy.getEconomyConfig().getWorkCooldown()));
    }

    public void setLastWorkCooldown(long lastWorkCooldown) {
        this.lastWorkCooldown = lastWorkCooldown;
    }

    public double getCollectables() {
        return MathUtils.round(collectables);
    }

    public void addCollectables(double collectables) {
        this.collectables += MathUtils.round(collectables);
    }

    public PapilertusMessageBuilder getShopMessageBuilder() {
        final EmbedBuilder shopBuilder = new EmbedBuilder();
        final PapilertusMessageBuilder messageBuilder = new PapilertusMessageBuilder();
        final String collectableName = Economy.getEconomyConfig().getCollectableName();
        final String currencyName = Economy.getEconomyConfig().getCurrencyName();

        for (GameUpgrade upgrade : Economy.getEconomyConfig().getUpgrades()) {
            final int upgradeLevel = getUpgradeLevel(upgrade.getName());
            messageBuilder.addButtons(new DiscordButton(getUserId(), new ShopButton(upgrade.getName(), this), ButtonStyle.PRIMARY, upgrade.getName()));
            shopBuilder.addField(upgrade.getIcon() + " " + upgrade.getName()
                    + " (Level: " + upgradeLevel + ")" + " | " + upgrade.getUpgradePrice(this) + " "
                    + Economy.getEconomyConfig().getCurrencyIcon(), upgrade.getDescription(), false);
        }
        shopBuilder.getDescriptionBuilder().append("Your stats:").append("\n")
                .append("Your ").append(currencyName).append(": ").append(getCoins()).append(Economy.getEconomyConfig().getCurrencyIcon()).append("\n");
        //for the public instance because it is usually not allowed to give rewards after a disboard-bump
        if (Economy.getEconomyConfig().getBaseCollectablesOnBumpGain() > 0) {
            shopBuilder.getDescriptionBuilder().append(collectableName).append(" per bump: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCollectablesOnBumpGain(), this, IncrementType.BUMP)).append("\n");
        }

        shopBuilder.getDescriptionBuilder().append(collectableName).append(" per minute in VoiceChat: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCoinOnVoiceActivityAmount(), this, IncrementType.VOICE)).append("\n")
                .append(collectableName).append(" per message: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCoinOnMessageAmount(), this, IncrementType.MESSAGE)).append("\n")
                .append(collectableName).append(" per daily: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseDailyGain(), this, IncrementType.DAILY)).append("\n")
                .append(currencyName).append(" per work: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseWorkGain(), this, IncrementType.WORK));
        messageBuilder.setEmbeds(shopBuilder.build());
        return messageBuilder;
    }

    public void removeCollectables(double collectables) {
        this.collectables -= MathUtils.round(collectables);
    }

    public void setCollectables(float collectables) {
        this.collectables = collectables;
    }

    public long getLastDaily() {
        return lastDaily;
    }

    public void setLastDaily(long lastDaily) {
        this.lastDaily = lastDaily;
    }

    public boolean canGetDaily() {
        return lastDaily == 0 || (System.currentTimeMillis() - getLastDaily()) > TimeUnit.HOURS.toMillis(24);
    }

    public void giveToUser(EconomyUser user, float coins) {
        this.removeCoins(coins);
        user.addCoins(coins);

        this.addSentToday(coins);
        user.addReceivedToday(coins);

        this.setLastSent(System.currentTimeMillis());
        user.setLastReceived(System.currentTimeMillis());
    }

    public void addReceivedToday(float coins) {
        setReceivedToday(getReceivedToday() + coins);
    }

    public void addSentToday(float coins) {
        setReceivedToday(getSentToday() + coins);
    }


    public float getReceivedToday() {
        if (System.currentTimeMillis() - lastReceived > TimeUnit.HOURS.toMillis(24)) {
            setReceivedToday(0);
            return 0;
        }
        return receivedToday;
    }

    public void setReceivedToday(float receivedToday) {
        this.receivedToday = receivedToday;
    }

    public float getSentToday() {
        if (System.currentTimeMillis() - lastSent > TimeUnit.HOURS.toMillis(24)) {
            setSentToday(0);
            return 0;
        }
        return sentToday;
    }

    public void setSentToday(float sentToday) {
        this.sentToday = sentToday;
    }

    public long getLastReceived() {
        return lastReceived;
    }

    public void setLastReceived(long lastReceived) {
        this.lastReceived = lastReceived;
    }

    public long getLastSent() {
        return lastSent;
    }

    public void setLastSent(long lastSent) {
        this.lastSent = lastSent;
    }

    public boolean canSend(float amount) {
        return getSentToday() + amount <= 1000;
    }

    public boolean canReceive(float amount) {
        return getReceivedToday() + amount < 1000;
    }

    public EconomyUserInventory getInventory() {
        return inventory;
    }

    public float getWeeklyCurrency() {
        return weeklyCurrency;
    }
}
