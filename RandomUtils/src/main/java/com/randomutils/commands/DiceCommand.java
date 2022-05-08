package com.randomutils.commands;

import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Random;

public class DiceCommand extends Command {

    public DiceCommand() {
        setName("dice");
        setDescription("Rolls a dice");
        setData(Commands.slash(getName(), getDescription())
                .addOption(OptionType.INTEGER, "max", "Maximum of the dice number", false)
        );
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {

        //Set 6 as the default max value if the "max" parameter is not given in the command
        final long max = slashCommandInteractionEvent.getOption("max") != null ? slashCommandInteractionEvent.getOption("max").getAsLong() : 6;

        if (max > 1000) {
            slashCommandInteractionEvent.reply("The maximum number cannot exceed 1000").setEphemeral(true).queue();
            return;
        }

        int random = new Random().nextInt((int) max + 1);

        final EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(slashCommandInteractionEvent.getUser().getAsTag(), null, slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                .addField("Result", "You rolled a: " + random + "!", true)
                .setColor(Color.WHITE)
                .setTimestamp(LocalDateTime.now());

        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }

}
