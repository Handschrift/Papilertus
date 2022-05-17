package com.funplugin.init;

import com.funplugin.commands.MemeCommand;
import com.papilertus.commands.core.Command;
import com.papilertus.plugin.Plugin;
import com.papilertus.plugin.PluginData;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.List;

public class FunPlugin implements Plugin {
    @Override
    public void onLoad(PluginData pluginData) {

    }

    @Override
    public List<Command> getCommands() {
        final ArrayList<Command> commands = new ArrayList<>();
        commands.add(new MemeCommand());
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
