package com.musicplayer.commands;

import com.musicplayer.audio.GuildMusicManager;
import com.musicplayer.audio.SoundManager;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public abstract class MusicCommand extends Command {
    private HashMap<String, GuildMusicManager> managerMap;

    protected GuildMusicManager getGuildAudioPlayer(Guild guild) {
        GuildMusicManager guildManager;
        if (!managerMap.containsKey(guild.getId())) {
            guildManager = new GuildMusicManager(SoundManager.getManager(), guild.getAudioManager());
            managerMap.put(guild.getId(), guildManager);
        } else {
            guildManager = managerMap.get(guild.getId());
            guildManager.updateTimeout();
        }

        guild.getAudioManager().setSendingHandler(guildManager.getSendHandler());
        return guildManager;
    }

    protected void setManagerMap(HashMap<String, GuildMusicManager> managerMap) {
        this.managerMap = managerMap;
    }

    protected HashMap<String, GuildMusicManager> getManagerMap() {
        return managerMap;
    }
}
