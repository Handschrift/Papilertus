package com.randomutils.init;

import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.plugin.Plugin;
import com.openpackagedbot.plugin.PluginData;
import com.randomutils.commands.ChoiceCommand;
import com.randomutils.commands.DiceCommand;

import java.util.ArrayList;
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
    public void onUnload() {

    }
}
