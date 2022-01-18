package com.economy.init;

import com.economy.commands.ProfileCommand;
import com.economy.listeners.MessageReceivedListener;
import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.plugin.Plugin;
import com.openpackagedbot.plugin.PluginConfig;
import com.openpackagedbot.plugin.PluginData;
import com.openpackagedbot.plugin.PluginDataStore;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.List;

public class Economy implements Plugin {
    //singleton for database
    private static PluginDataStore store;
    private static PluginConfig config;

    @Override
    public void onLoad(PluginData pluginData) {
        store = new PluginDataStore(pluginData);
        config = new PluginConfig(pluginData);
        config.addEntry("currency_name", "Boostgems");
        config.addEntry("currency_icon", "<:boost:843963506217844747>");
        config.addEntry("coin_on_message_sent", true);
        config.addEntry("coin_message_cooldown", 20);
        config.addEntry("coin_on_voice_activity", true);
        config.addEntry("coin_on_message_amount", 10);
        config.addEntry("coin_voice_cooldown", 20);
        config.addEntry("coin_on_voice_activity_amount", 10);
        config.addEntry("whitelist_mode", false);
        config.addEntry("listed_users", new String[]{});
        config.addEntry("listed_roles", new String[]{});
    }

    @Override
    public List<Command> getCommands() {
        final ArrayList<Command> commands = new ArrayList<>();
        commands.add(new ProfileCommand());
        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        final ArrayList<EventListener> listeners = new ArrayList<>();
        listeners.add(new MessageReceivedListener());
        return listeners;
    }

    @Override
    public void onUnload() {

    }

    public static PluginDataStore getDataStore() {
        return store;
    }

    public static PluginConfig getConfig() {
        return config;
    }

}
