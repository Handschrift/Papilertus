package com.papilertus.command

import net.dv8tion.jda.api.interactions.commands.build.CommandData

class CommandClient {

    private val commandsMap = mutableMapOf<String, Command>()
    private val commandDataSet = mutableSetOf<CommandData>()

    fun addCommands(vararg commands: Command) {
        for (command in commands) {
            if (!command.enabled)
                continue

            commandsMap[command.name] = command
            commandDataSet.add(command.commandData)
        }
    }

    fun getData(): Collection<CommandData> {
        return commandDataSet
    }

    fun getCommand(name: String): Command? {
        return commandsMap[name]
    }

}