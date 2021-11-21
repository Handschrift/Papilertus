package com.randomutils.commands;

import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Random;

public class ChoiceCommand extends Command {

    public ChoiceCommand() {
        setName("choice");
        setDescription("Returns a random choice based on a given list. Separate the Elements with a ';'");
        setData(new CommandData(getName(), getDescription())
                .addOption(OptionType.STRING, "list", "List of elements, separated by a ';'", true)
        );
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        final String[] elements = slashCommandEvent.getOption("list").getAsString().split(";");
        final String choice = elements[new Random().nextInt(elements.length)];

        final EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.BLUE)
                .setAuthor(slashCommandEvent.getUser().getAsTag(), null, slashCommandEvent.getUser().getEffectiveAvatarUrl())
                .addField("Result", "My choice is: " + choice, true)
                .setTimestamp(LocalDateTime.now());

        slashCommandEvent.replyEmbeds(builder.build()).queue();
    }
}
