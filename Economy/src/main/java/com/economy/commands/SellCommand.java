package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.database.models.EconomyUserInventoryEntry;
import com.economy.game.element.Event;
import com.economy.init.Economy;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SellCommand extends Command {

    public SellCommand() {
        setName(Economy.getEconomyConfig().getConvertCommandName());
        setDescription("Sell or convert your collectables to currency");
        setData(Commands.slash(getName(), getDescription())
                .addOption(OptionType.NUMBER, "amount", "amount", false));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
        final double collectables = slashCommandInteractionEvent.getOption("amount") == null ? user.getCollectables() : slashCommandInteractionEvent.getOption("amount").getAsDouble();

        if (user.getCollectables() < collectables) {
            slashCommandInteractionEvent.reply("You don't have enough " + Economy.getEconomyConfig().getCollectableName()).setEphemeral(true).queue();
            return;
        }

        if (collectables <= 0) {
            slashCommandInteractionEvent.reply("Please enter a valid value").setEphemeral(true).queue();
            return;
        }

        if (user.getInventory().getSize() >= 10) {
            slashCommandInteractionEvent.reply("You cannot have more than 10 entries in your inventory.").setEphemeral(true).queue();
            return;
        }

        user.removeCollectables(collectables);
        user.getInventory().addEntry(new EconomyUserInventoryEntry((float) collectables, System.currentTimeMillis()));
        UserDatabase.updateUser(user);
        if (Math.random() < Economy.getEconomyConfig().getEventProbability() / 100.0) {
            Event.callRandomEvent(slashCommandInteractionEvent.getMember(), slashCommandInteractionEvent.getChannel());
        }
        slashCommandInteractionEvent.reply("You added " + collectables + " " + Economy.getEconomyConfig().getCollectableName()
                + " to your farm!").queue();
    }
}
