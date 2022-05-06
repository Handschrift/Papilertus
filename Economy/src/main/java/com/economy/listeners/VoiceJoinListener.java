package com.economy.listeners;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.init.Economy;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VoiceJoinListener extends ListenerAdapter {

    final HashMap<String, Long> voiceTimes = new HashMap<>();

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        if (event.getMember().getUser().isBot())
            return;

        if (voiceTimes.containsKey(event.getMember().getId())) {
            return;
        }
        voiceTimes.put(event.getMember().getId(), System.currentTimeMillis());


    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {

        if (event.getMember().getUser().isBot())
            return;

        if (!voiceTimes.containsKey(event.getMember().getId())) {
            return;
        }
        long timeDiff = System.currentTimeMillis() - voiceTimes.get(event.getMember().getId());
        voiceTimes.remove(event.getMember().getId());
        //user spent less than a minute in VC => give nothing
        if (timeDiff < TimeUnit.MINUTES.toMillis(1)) {
            return;
        }

        final EconomyUser user = UserDatabase.fetch(event.getMember().getId(), event.getGuild().getId());
        double addedCollectables = TimeUnit.MILLISECONDS.toMinutes(timeDiff) * GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCoinOnVoiceActivityAmount()
                , user, IncrementType.VOICE);
        user.addCollectables(addedCollectables);
        UserDatabase.updateUser(user);

    }


}
