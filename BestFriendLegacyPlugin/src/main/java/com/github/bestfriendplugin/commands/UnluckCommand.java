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

public class UnluckCommand extends Command {

    public UnluckCommand() {
        setName("pech");
        setDescription("Gibt dir einen Pechkeks");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        Random r = new Random();
        try {
            List<String> lines = FileUtils.readLines(new File("unluck.txt"), StandardCharsets.UTF_8);
            int size = lines.size();
            slashCommandInteractionEvent.reply(lines.get(r.nextInt(size))).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
