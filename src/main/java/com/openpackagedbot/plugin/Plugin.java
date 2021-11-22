package com.openpackagedbot.plugin;

import com.openpackagedbot.commands.core.Command;

import java.util.List;

public interface Plugin {
    void onLoad(PluginData pluginData);

    List<Command> getCommands();

    List<Object> getListeners();

    void onUnload();
}
