package com.github.bestfriendplugin.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final ArrayList<String> words = new ArrayList<>();
        words.add("instagram");
        words.add("tiktok");
        words.add("tik tok");
        words.add("facebook");
        words.add("lucas");
        words.add("windows");
        words.add("microsoft");
        words.add("league of legends");
        for (String word : words) {
            if (event.getMessage().getContentRaw().toLowerCase().contains(word)) {
                event.getMessage().addReaction(":lucaswatching:936962214491979796").queue();
                break;
            }
        }
    }
}
