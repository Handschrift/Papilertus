package com.github.bestfriendplugin.commands;

import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class PickupCommand extends Command {

    public PickupCommand() {
        setName("pickup");
        setDescription("Du bekommst einen Anmachspruch...viel Glück");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        try {
            List<String> lines = FileUtils.readLines(new File("pickup.txt"), StandardCharsets.UTF_8);
            slashCommandInteractionEvent.reply(lines.get(new Random().nextInt(lines.size()))).queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
