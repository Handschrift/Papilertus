package com.papilertus.plugin

import com.papilertus.command.Command
import com.papilertus.gui.contextMenu.ContextMenuEntry
import net.dv8tion.jda.api.hooks.EventListener

interface Plugin {
    fun onLoad(data: PluginData)

    fun getCommands(): List<Command>
    fun getContextMenuEntries(): List<ContextMenuEntry>

    fun getListeners(): List<EventListener>

    fun onUnload()
}