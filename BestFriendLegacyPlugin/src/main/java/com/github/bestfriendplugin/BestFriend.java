package com.github.bestfriendplugin;

import com.github.bestfriendplugin.commands.*;
import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.plugin.Plugin;
import com.openpackagedbot.plugin.PluginData;

import java.util.ArrayList;
import java.util.List;

public class BestFriend implements Plugin {


    @Override
    public void onLoad(PluginData pluginData) {
        System.out.println("Hello World!");
    }

    @Override
    public List<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new ZodiacCommand());
        commands.add(new PollCommand());
        commands.add(new LuckCommand());
        commands.add(new UnluckCommand());
        commands.add(new PickupCommand());
        return commands;
    }

    @Override
    public void onUnload() {

    }
}
