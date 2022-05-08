package com.randomutils.commands;

import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Random;

public class ChoiceCommand extends Command {

    public ChoiceCommand() {
        setName("choice");
        setDescription("Returns a random choice based on a given list. Separate the Elements with a ';'");
        setData(Commands.slash(getName(), getDescription())
                .addOption(OptionType.STRING, "list", "List of elements, separated by a ';'", true)
        );
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final String[] elements = slashCommandInteractionEvent.getOption("list").getAsString().split(";");
        final String choice = elements[new Random().nextInt(elements.length)];

        final EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.BLUE)
                .setAuthor(slashCommandInteractionEvent.getUser().getAsTag(), null, slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                .addField("Result", "My choice is: " + choice, true)
                .setTimestamp(LocalDateTime.now());

        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }
}
