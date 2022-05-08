package com.papilertus.plugin;

import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.List;

public interface Plugin {
    void onLoad(PluginData pluginData);

    List<Command> getCommands();

    List<? extends EventListener> getListeners();

    void onUnload();
}
