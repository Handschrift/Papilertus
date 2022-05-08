package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.database.models.EconomyUserInventory;
import com.economy.database.models.EconomyUserInventoryEntry;
import com.economy.game.element.Forecast;
import com.economy.init.Economy;
import com.economy.util.MathUtils;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ListIterator;

public class CollectCommand extends Command {

    public CollectCommand() {
        setName("collect");
        setDescription("collects your inventory");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
        final EconomyUserInventory inventory = user.getInventory();
        double coins = 0;
        int deadEntries = 0;
        final Forecast forecast = Forecast.getForecast();
        final ListIterator<EconomyUserInventoryEntry> iterator = inventory.getEntries().listIterator();

        while (iterator.hasNext()){
            EconomyUserInventoryEntry entry = iterator.next();
            if (entry.isMature()) {
                coins += forecast.getData()[0].getValue() * entry.getCount();
                iterator.remove();
            } else if (entry.isDead()) {
                iterator.remove();
                deadEntries++;
            }
        }

        if (deadEntries == 0 && coins == 0) {
            slashCommandInteractionEvent.reply("There is nothing to do.").setEphemeral(true).queue();
            return;
        }

        coins = MathUtils.round(coins);
        user.addCoins(coins);
        UserDatabase.updateUser(user);

        slashCommandInteractionEvent.reply("You got " + coins + " " + Economy.getEconomyConfig().getCurrencyName()).queue();
    }
}
