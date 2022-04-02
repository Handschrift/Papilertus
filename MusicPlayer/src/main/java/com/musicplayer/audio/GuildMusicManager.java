package com.musicplayer.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.managers.AudioManager;

public class GuildMusicManager {

    private final AudioPlayer player;

    private final TrackScheduler scheduler;

    private final AudioManager audioManager;

    private long lastUsed;

    public GuildMusicManager(AudioPlayerManager manager, AudioManager audioManager) {
        this.player = manager.createPlayer();
        this.scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        lastUsed = System.currentTimeMillis();
        this.audioManager = audioManager;
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }

    public void updateTimeout() {
        this.lastUsed = System.currentTimeMillis();
    }

    public boolean isTimeOuted() {
        return (System.currentTimeMillis() - lastUsed) > 300000; // 5 Minutes
    }

    public void close() {
        player.stopTrack();
        scheduler.clearQueue();
        audioManager.closeAudioConnection();
    }
}
