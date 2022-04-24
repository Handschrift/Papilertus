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
import com.openpackagedbot.gui.button.DiscordButton;
import com.openpackagedbot.gui.generator.PapilertusMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.util.concurrent.TimeUnit;


public class EconomyUser {
    @SerializedName(value = "_id")
    private final EconomyUserKey id;
    private double coins = 0;
    private double collectables = 0;
    private final JsonObject upgradeCounts = new JsonObject();
    private long lastWorkCooldown = 0;

    private long lastDaily = 0;

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
        this.coins += MathUtils.round(coins);
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
        return lastWorkCooldown == 0 || (System.currentTimeMillis() - lastWorkCooldown > TimeUnit.MINUTES.toMillis(Economy.getConfig().readInt("work_cooldown")));
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
        final String collectableName = Economy.getConfig().readString("collectable_name");
        final String currencyName = Economy.getConfig().readString("currency_name");

        for (GameUpgrade upgrade : Economy.getConfig().readType("upgrades", GameUpgrade.class)) {
            final int upgradeLevel = getUpgradeLevel(upgrade.getName());
            messageBuilder.addButtons(new DiscordButton(getUserId(), new ShopButton(upgrade.getName(), this), ButtonStyle.PRIMARY, upgrade.getName()));
            shopBuilder.addField(upgrade.getIcon() + " " + upgrade.getName()
                    + " (Level: " + upgradeLevel + ")" + " | " + upgrade.getUpgradePrice(this) + " "
                    + Economy.getConfig().readString("currency_icon"), upgrade.getDescription(), false);
        }
        shopBuilder.getDescriptionBuilder().append("Your stats:").append("\n")
                .append("Your ").append(currencyName).append(": ").append(getCoins()).append(Economy.getConfig().readString("currency_icon")).append("\n")
                .append(collectableName).append(" per bump: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_collectables_on_bump_gain"), this, IncrementType.BUMP)).append("\n")
                .append(collectableName).append(" per minute in VoiceChat: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_coin_on_voice_activity_amount"), this, IncrementType.VOICE)).append("\n")
                .append(collectableName).append(" per message: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_coin_on_message_amount"), this, IncrementType.MESSAGE)).append("\n")
                .append(collectableName).append(" per daily: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_daily_gain"), this, IncrementType.DAILY)).append("\n")
                .append(currencyName).append(" per work: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_work_gain"), this, IncrementType.WORK));
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

}
