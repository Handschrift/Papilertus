package com.economy.listeners;

import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceivedListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final EconomyUser user = EconomyUser.fetch(event.getAuthor().getId(), event.getGuild().getId());
        user.addCoins(Economy.getConfig().readInt("coin_on_message_amount"));

    }
}
