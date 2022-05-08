package com.musicplayer.init;

import com.musicplayer.audio.GuildMusicManager;
import com.musicplayer.commands.PlayCommand;
import com.musicplayer.commands.SkipCommand;
import com.musicplayer.commands.StopCommand;
import com.papilertus.commands.core.Command;
import com.papilertus.plugin.Plugin;
import com.papilertus.plugin.PluginData;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MusicPlayer implements Plugin {
    private HashMap<String, GuildMusicManager> managers;

    @Override
    public void onLoad(PluginData pluginData) {
        managers = new HashMap<>();

        //implementation of the timeout check
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (String key : managers.keySet()) {
                    GuildMusicManager manager = managers.get(key);
                    if (manager.getPlayer().getPlayingTrack() != null) {
                        manager.updateTimeout();
                    }
                    if (manager.isTimeOuted()) {
                        manager.close();
                        managers.remove(key);
                    }
                }
            }
        }, 0, 2, TimeUnit.SECONDS);

    }

    @Override
    public List<Command> getCommands() {

        final ArrayList<Command> commands = new ArrayList<>();
        commands.add(new PlayCommand(managers));
        commands.add(new SkipCommand(managers));
        commands.add(new StopCommand(managers));
        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        return null;
    }

    @Override
    public void onUnload() {

    }
}
