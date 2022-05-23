package com.funplugin.init;

import com.funplugin.commands.MemeCommand;
import com.funplugin.commands.SpiritAnimalCommand;
import com.papilertus.commands.core.Command;
import com.papilertus.plugin.Plugin;
import com.papilertus.plugin.PluginConfig;
import com.papilertus.plugin.PluginData;
import com.papilertus.plugin.PluginDataStore;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.List;

public class FunPlugin implements Plugin {

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
        final ArrayList<Command> commands = new ArrayList<>();
        commands.add(new MemeCommand());
        commands.add(new SpiritAnimalCommand(dataStore));
        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        return null;
    }

    @Override
    public void onUnload() {

    }

    public PluginDataStore getDataStore() {
        return dataStore;
    }

    public static PluginConfig getConfig() {
        return config;
    }
}
