package com.openfunbot.commands.core;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {

    private final CommandClient commandClient;

    public CommandListener(CommandClient commandClient) {
        this.commandClient = commandClient;
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        commandClient.getCommand(event.getName()).execute(event);
    }
}
