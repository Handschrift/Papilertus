package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.database.models.EconomyUserInventory;
import com.economy.database.models.EconomyUserInventoryEntry;
import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.Instant;
import java.time.ZoneId;

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

        if (inventory.getEntries().size() == 0) {
            slashCommandInteractionEvent.reply("Your inventory is currently empty!").setEphemeral(true).queue();
            return;
        }

        for (EconomyUserInventoryEntry entry : inventory.getEntries()) {
            builder.addField(entry.getName() + "(" + entry.getCount() + ")", "Added: " + Instant.ofEpochMilli(entry.getTimeAdded()).atZone(ZoneId.systemDefault()).toLocalDate(), false);
        }
        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }
}
