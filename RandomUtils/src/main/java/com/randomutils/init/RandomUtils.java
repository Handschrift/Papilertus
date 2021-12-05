package com.randomutils.init;

import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.plugin.Plugin;
import com.openpackagedbot.plugin.PluginData;
import com.randomutils.commands.ChoiceCommand;
import com.randomutils.commands.DiceCommand;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomUtils implements Plugin {

    @Override
    public void onLoad(PluginData pluginData) {

    }

    @Override
    public List<Command> getCommands() {
        List<Command> commands = new ArrayList<>();
        commands.add(new ChoiceCommand());
        commands.add(new DiceCommand());
        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        return Collections.emptyList();
    }

    @Override
    public void onUnload() {

    }
}
