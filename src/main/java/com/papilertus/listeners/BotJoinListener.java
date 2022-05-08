package com.papilertus.listeners;

import com.papilertus.commands.core.CommandClient;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotJoinListener extends ListenerAdapter {
    private final CommandClient commandClient;

    public BotJoinListener(CommandClient commandClient) {
        this.commandClient = commandClient;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        event.getGuild().updateCommands().addCommands(commandClient.getData()).queue();
    }
}
