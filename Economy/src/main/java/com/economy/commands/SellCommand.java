package com.economy.commands;

import com.economy.init.Economy;
import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SellCommand extends Command {

    public SellCommand() {
        setName(Economy.getConfig().readString("convert_command_name"));
        setDescription("Sell or convert your collectables to currency");
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {

    }
}
