package com.economy.listeners;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.init.Economy;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MessageReceivedListener extends ListenerAdapter {
    private final HashMap<String, Long> cooldowns = new HashMap<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMember() == null)
            return;
        if (event.getMember().getUser().isBot())
            return;

        final EconomyUser user = UserDatabase.fetch(event.getAuthor().getId(), event.getGuild().getId());
        final float coins = GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCoinOnMessageAmount(), user, IncrementType.MESSAGE);
        if (!cooldowns.containsKey(user.getUserId())) {
            user.addCollectables(coins);
            cooldowns.put(user.getUserId(), System.currentTimeMillis());
            UserDatabase.updateUser(user);
            return;
        }

        if (System.currentTimeMillis() - cooldowns.get(user.getUserId()) > TimeUnit.SECONDS.toMillis(Economy.getEconomyConfig().getCoinMessageCooldown())) {
            user.addCollectables(coins);
            UserDatabase.updateUser(user);
        }


    }
}
