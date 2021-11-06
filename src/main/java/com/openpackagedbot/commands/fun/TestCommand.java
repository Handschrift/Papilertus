package com.openpackagedbot.commands.fun;

import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class TestCommand extends Command {

    public TestCommand() {
        setName("test");
        setDescription("test desc");
        setHelp("HEL!");
        setData(new CommandData(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.reply("TEST").queue();
    }
}
