package com.economy.game.element;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.papilertus.gui.button.Pressable;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.stream.Collectors;

public class ShopButton implements Pressable {

    private final String upgradeName;
    private final EconomyUser user;

    public ShopButton(String upgradeName, EconomyUser user) {
        this.upgradeName = upgradeName;
        this.user = user;
    }

    @Override
    public void onClick(ButtonInteractionEvent buttonClickEvent) {
        final GameUpgrade upgrade = Economy.getEconomyConfig().getUpgrades()
                .stream().filter(gameUpgrade -> gameUpgrade.getName().equals(upgradeName)).collect(Collectors.toList()).get(0);
        if (upgrade.getUpgradePrice(user) > user.getCoins()) {
            buttonClickEvent.reply("You don't have enough money to do that!").setEphemeral(true).queue();
            return;
        }
        user.removeCoins(upgrade.getUpgradePrice(user));
        user.addUpgrades(upgradeName);
        UserDatabase.updateUser(user);
        buttonClickEvent.getMessage().editMessage(user.getShopMessageBuilder().build()).queue();
        buttonClickEvent.deferEdit().queue();
    }
}
