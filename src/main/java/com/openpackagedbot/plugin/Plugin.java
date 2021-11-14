package com.openpackagedbot.plugin;

import com.openpackagedbot.commands.core.Command;

import java.util.List;

public interface Plugin {
    void onLoad();
    List<Command> getCommands();
    void onUnload();
}
