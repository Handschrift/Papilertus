package com.github.bestfriendplugin.commands;

import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class UnluckCommand extends Command {

    public UnluckCommand() {
        setName("pech");
        setDescription("Gibt dir einen Pechkeks");
        setData(new CommandData(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        Random r = new Random();
        try {
            List<String> lines = FileUtils.readLines(new File("unluck.txt"), StandardCharsets.UTF_8);
            int size = lines.size();
            slashCommandEvent.reply(lines.get(r.nextInt(size))).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
