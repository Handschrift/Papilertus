package com.github.bestfriendplugin;

import com.github.bestfriendplugin.commands.*;
import com.github.bestfriendplugin.listeners.MessageListener;
import com.papilertus.commands.core.Command;
import com.papilertus.plugin.Plugin;
import com.papilertus.plugin.PluginConfig;
import com.papilertus.plugin.PluginData;
import com.papilertus.plugin.PluginDataStore;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.List;

public class BestFriend implements Plugin {

    private PluginDataStore dataStore;
    private static PluginConfig config;

    @Override
    public void onLoad(PluginData pluginData) {
        config = new PluginConfig(pluginData);
        config.addEntry("amount_of_images", 165);
        dataStore = new PluginDataStore(pluginData);
    }

    @Override
    public List<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new ZodiacCommand());
        commands.add(new PollCommand());
        commands.add(new LuckCommand());
        commands.add(new UnluckCommand());
        commands.add(new PickupCommand());
        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        final ArrayList<EventListener> listeners = new ArrayList<>();
        listeners.add(new MessageListener());
        return listeners;
    }

    @Override
    public void onUnload() {

    }

    public static PluginConfig getConfig() {
        return config;
    }
}
