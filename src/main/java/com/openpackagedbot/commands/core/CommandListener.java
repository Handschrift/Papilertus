package com.openpackagedbot.commands.core;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public final class CommandListener extends ListenerAdapter {

    private final CommandClient commandClient;

    public CommandListener(CommandClient commandClient) {
        this.commandClient = commandClient;
    }


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getChannel().getType() == ChannelType.PRIVATE) {
            if (commandClient.getCommand(event.getName()).isPrivateCommand()) {
                commandClient.getCommand(event.getName()).execute(event);
            } else {
                event.reply("This command is only usable on servers!").setEphemeral(true).queue();
            }
        } else {
            commandClient.getCommand(event.getName()).execute(event);
        }
    }
}
