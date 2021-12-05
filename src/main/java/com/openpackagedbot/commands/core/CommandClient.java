package com.openpackagedbot.commands.core;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class CommandClient {
    private final HashMap<String, Command> commandHashMap = new HashMap<>();
    private final Set<CommandData> commandDataSet = new HashSet<>();

    public void addCommands(Command... commands) {
        for (Command command : commands) {
            if (command.isEnabled()) {
                commandDataSet.add(command.getData());
                commandHashMap.put(command.getName(), command);
            }
        }
    }

    public Command getCommand(String name) {
        return commandHashMap.get(name);
    }

    public Set<CommandData> getData() {
        return this.commandDataSet;
    }


}
