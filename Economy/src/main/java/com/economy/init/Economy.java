package com.economy.init;

import com.economy.commands.ProfileCommand;
import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.plugin.Plugin;
import com.openpackagedbot.plugin.PluginConfig;
import com.openpackagedbot.plugin.PluginData;
import com.openpackagedbot.plugin.PluginDataStore;
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
        config.addEntry("currency_name", "Crypcoin");
        config.addEntry("currency_icon", "");
        config.addEntry("coin_on_message_sent", true);
        config.addEntry("coin_on_voice_activity", true);
        config.addEntry("coin_on_message_amount", 10);
        config.addEntry("coin_on_voice_activity_amount", 10);
        config.addEntry("whitelist_mode", false);
        config.addEntry("listed_users", new String[]{});
        config.addEntry("listed_roles", new String[]{});
    }

    @Override
    public List<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new ProfileCommand());
        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        return null;
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
