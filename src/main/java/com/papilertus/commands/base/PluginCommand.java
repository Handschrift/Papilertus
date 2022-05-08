package com.papilertus.commands.base;

import com.papilertus.commands.core.Command;
import com.papilertus.plugin.PluginData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;

public class PluginCommand extends Command {

    final ArrayList<PluginData> data;

    public PluginCommand(ArrayList<PluginData> data) {
        this.data = data;
        setName("plugins");
        setDescription("Lists all installed Plugins");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent event) {
        final EmbedBuilder builder = new EmbedBuilder();
        for (PluginData currentData : data) {
            builder.addField(currentData.getName(), "Made by: " + currentData.getAuthor(), true);
        }
        event.replyEmbeds(builder.build()).queue();
    }
}
