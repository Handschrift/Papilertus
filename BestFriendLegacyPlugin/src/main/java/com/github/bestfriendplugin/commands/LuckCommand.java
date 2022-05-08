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

public class LuckCommand extends Command {
    public LuckCommand() {
        setName("glück");
        setDescription("Bekomme einen Glückskeks");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent ButtonInteractionEvent) {
        Random r = new Random();
        try {
            List<String> lines = FileUtils.readLines(new File("luck.txt"), StandardCharsets.UTF_8);
            int size = lines.size();
            ButtonInteractionEvent.reply(lines.get(r.nextInt(size))).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
