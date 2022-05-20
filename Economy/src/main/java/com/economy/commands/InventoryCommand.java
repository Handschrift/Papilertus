package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.database.models.EconomyUserInventory;
import com.economy.database.models.EconomyUserInventoryEntry;
import com.economy.init.Economy;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class InventoryCommand extends Command {

    public InventoryCommand() {
        setName("inventory");
        setDescription("shows your inventory");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
        final EconomyUserInventory inventory = user.getInventory();
        final EmbedBuilder builder = new EmbedBuilder();
        String footerString = "You have to wait for " + Economy.getEconomyConfig().getTimeToGrowDuration() + " " + getProperTimeUnitName(Economy.getEconomyConfig().getTimeToGrowUnit(), Economy.getEconomyConfig().getTimeToGrowDuration())
                + " for the seeds to grow.";

        if (inventory.getEntries().size() == 0) {
            slashCommandInteractionEvent.reply("Your inventory is currently empty!").setEphemeral(true).queue();
            return;
        }

        for (EconomyUserInventoryEntry entry : inventory.getEntries()) {
            builder.addField(entry.getName() + " (" + entry.getCount() + ")", "Added: " + Instant.ofEpochMilli(entry.getTimeAdded()).atZone(ZoneId.systemDefault()).toLocalDate() + "\n"
                    + "Will grow: <t:" + TimeUnit.MILLISECONDS.toSeconds(entry.getTimeFinished()) + ":R>", false);
        }


        if (Economy.getEconomyConfig().isAutoCollect()) {
            footerString = MessageFormat.format("{0} The {1} will be collected automatically.", footerString, Economy.getEconomyConfig().getCurrencyName());
        } else {
            footerString = MessageFormat.format("{0} After {1} {2} the plants will rot.\n Collect them with /collect!", footerString, Economy.getEconomyConfig().getTimeToDieDuration(), getProperTimeUnitName(Economy.getEconomyConfig().getTimeDieUnit(), Economy.getEconomyConfig().getTimeToDieDuration()));

        }
        builder.setFooter(footerString);

        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }

    private String getProperTimeUnitName(String unitName, long amount) {
        return amount == 1 ? unitName.toLowerCase().substring(0, unitName.length() - 1) : unitName.toLowerCase();
    }

}
