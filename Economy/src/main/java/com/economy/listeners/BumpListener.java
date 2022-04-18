package com.economy.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BumpListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getMessage().getInteraction() == null) return;
        final Message.Interaction bumpInteraction = event.getMessage().getInteraction();
        //check if it is the disboard bot and if the command executed was a bump command
        if (event.getAuthor().getId().equals("302050872383242240")
                && bumpInteraction.getName().equals("bump")) {
            //I don't know if this is needed, but it is an extra verification step for a successful bump
            if (event.getMessage().getEmbeds().get(0).getImage().getUrl().equals("https://disboard.org/images/bot-command-image-bump.png")) {
                event.getChannel().sendMessage(bumpInteraction.getUser().getAsMention() + " bumped!").queue();
            }
        }

    }
}
