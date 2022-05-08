package com.economy.commands;

import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class HelpCommand extends Command {

    public HelpCommand() {
        setName("economy-help");
        setDescription("Show a link to the help");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        slashCommandInteractionEvent.reply("Here is the link: https://github.com/Handschrift/Papilertus/blob/master/Economy/README.MD").setEphemeral(true).queue();
    }
}
