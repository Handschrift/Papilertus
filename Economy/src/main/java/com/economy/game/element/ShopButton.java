package com.economy.game.element;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.openpackagedbot.gui.button.Pressable;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.stream.Collectors;

public class ShopButton implements Pressable {

    private final String upgradeName;
    private final EconomyUser user;

    public ShopButton(String upgradeName, EconomyUser user) {
        this.upgradeName = upgradeName;
        this.user = user;
    }

    @Override
    public void onClick(ButtonClickEvent buttonClickEvent) {
        final GameUpgrade upgrade = Economy.getConfig().readType("upgrades", GameUpgrade.class)
                .stream().filter(gameUpgrade -> gameUpgrade.getName().equals(upgradeName)).collect(Collectors.toList()).get(0);
        final int upgradeLevel = user.getUpgradeLevel(upgrade.getName());
        if (upgrade.getBasePrice() * (int) Math.sqrt(upgradeLevel) > user.getCoins()) {
            buttonClickEvent.reply("You don't have enough money to do that!").setEphemeral(true).queue();
            return;
        }
        user.addUpgrades(upgradeName);
        user.removeCoins(upgrade.getBasePrice() * (int) Math.sqrt(upgradeLevel));
        UserDatabase.updateUser(user);
        buttonClickEvent.getMessage().editMessage(user.getShopMessageBuilder().build()).queue();
    }
}
