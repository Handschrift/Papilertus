package com.economy.init;

import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.plugin.Plugin;
import com.openpackagedbot.plugin.PluginData;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.List;

public class Economy implements Plugin {
    @Override
    public void onLoad(PluginData pluginData) {
        System.out.println("Hello Economy!");
    }

    @Override
    public List<Command> getCommands() {
        return null;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        return null;
    }

    @Override
    public void onUnload() {

    }
}
