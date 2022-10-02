package com.papilertus.gui.contextMenu

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

abstract class ContextMenuEntry(val name: String) {
    val contextMenuData: CommandData = Commands.user(name)

    abstract fun execute(event: UserContextInteractionEvent);
}