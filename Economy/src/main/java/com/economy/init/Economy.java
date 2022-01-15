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
    private PluginDataStore store;

    @Override
    public void onLoad(PluginData pluginData) {
        store = new PluginDataStore(pluginData);
        final PluginConfig config = new PluginConfig(pluginData);
        config.addEntry("main", 0);
    }

    @Override
    public List<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new ProfileCommand(store));
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
