package com.musicplayer.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

public class SoundManager {
    private static AudioPlayerManager manager;

    public static AudioPlayerManager getManager() {
        if (manager == null) {
            manager = new DefaultAudioPlayerManager();
            AudioSourceManagers.registerRemoteSources(manager);
        }
        return manager;
    }

}
