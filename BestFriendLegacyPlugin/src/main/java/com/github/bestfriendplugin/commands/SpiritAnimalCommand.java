package com.github.bestfriendplugin.commands;

import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.File;
import java.time.LocalDate;
import java.util.Random;

public class SpiritAnimalCommand extends Command {

    public SpiritAnimalCommand() {
        setName("spirit-animal");
        setDescription("Shows your current spirit animal");
        setData(new CommandData(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        long userId = slashCommandEvent.getUser().getIdLong();
        int sumOfDays = LocalDate.now().getDayOfMonth() + LocalDate.now().getYear() + LocalDate.now().getMonthValue();
        int random = new Random(userId / sumOfDays).nextInt(165);
        final EmbedBuilder builder = new EmbedBuilder().setImage("attachment://" + random + ".png")
                .setAuthor(slashCommandEvent.getUser().getAsTag(), null, slashCommandEvent.getUser().getEffectiveAvatarUrl())
                .setDescription("And your today's spirit animal is...")
                .setFooter("Execute the command tomorrow to see your next spirit animal!");
        slashCommandEvent.replyEmbeds(builder.build()).addFile(new File("spirit_animals/" + random + ".png")).queue();
    }
}
