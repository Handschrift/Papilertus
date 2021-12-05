package com.randomutils.commands;

import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Random;

public class DiceCommand extends Command {

    public DiceCommand() {
        setName("dice");
        setDescription("Rolls a dice");
        setData(new CommandData(getName(), getDescription())
                .addOption(OptionType.INTEGER, "max", "Maximum of the dice number", false)
        );
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {

        //Set 6 as the default max value if the "max" parameter is not given in the command
        final long max = slashCommandEvent.getOption("max") != null ? slashCommandEvent.getOption("max").getAsLong() : 6;

        if (max > 1000) {
            slashCommandEvent.reply("The maximum number cannot exceed 1000").setEphemeral(true).queue();
            return;
        }

        int random = new Random().nextInt((int) max + 1);

        final EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(slashCommandEvent.getUser().getAsTag(), null, slashCommandEvent.getUser().getEffectiveAvatarUrl())
                .addField("Result", "You rolled a: " + random + "!", true)
                .setColor(Color.WHITE)
                .setTimestamp(LocalDateTime.now());

        slashCommandEvent.replyEmbeds(builder.build()).queue();
    }

}
