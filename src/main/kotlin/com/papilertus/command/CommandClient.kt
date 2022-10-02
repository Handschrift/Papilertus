package com.papilertus.command

import com.papilertus.gui.contextMenu.ContextMenuEntry
import net.dv8tion.jda.api.interactions.commands.build.CommandData

class CommandClient {

    private val commandsMap = mutableMapOf<String, Command>()
    private val commandDataSet = mutableSetOf<CommandData>()

    private val contextMenuEntryMap = mutableMapOf<String, ContextMenuEntry>()

    fun addCommands(vararg commands: Command) {
        for (command in commands) {
            if (!command.enabled)
                continue

            commandsMap[command.name] = command
            commandDataSet.add(command.commandData)
        }
    }

    fun addContextMenuEntries(vararg entries: ContextMenuEntry) {
        for (entry in entries) {

            contextMenuEntryMap[entry.name] = entry
            commandDataSet.add(entry.contextMenuData)
        }
    }

    fun getData(): Collection<CommandData> {
        return commandDataSet
    }

    fun getCommand(name: String): Command? {
        return commandsMap[name]
    }

    fun getContextMenuEntry(name: String): ContextMenuEntry? {
        return contextMenuEntryMap[name]
    }

}