package com.economy.game.element;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.papilertus.gui.button.Pressable;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class AnswerButton implements Pressable {

    private final boolean answer, trueAnswer;

    public AnswerButton(boolean answer, boolean trueAnswer) {
        this.answer = answer;
        this.trueAnswer = trueAnswer;
    }

    @Override
    public void onClick(ButtonInteractionEvent buttonClickEvent) {
        final EconomyUser user = UserDatabase.fetch(buttonClickEvent.getUser().getId(), buttonClickEvent.getGuild().getId());
        if (answer == trueAnswer) {
            final float coins = GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseWorkGain(), user, IncrementType.WORK);
            user.addCoins(coins);
            buttonClickEvent.reply("You picked " + answer + " and that's right! You got " + coins + " " + Economy.getEconomyConfig().getCurrencyName()).queue();
        } else {
            buttonClickEvent.reply("You picked " + answer + " and that's incorrect... but you gained new knowledge!").queue();
        }
        UserDatabase.updateUser(user);
        buttonClickEvent.getMessage().editMessageComponents().queue();
    }
}
