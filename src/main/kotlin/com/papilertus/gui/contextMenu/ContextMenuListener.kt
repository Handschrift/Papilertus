package com.papilertus.gui.contextMenu

import com.papilertus.command.CommandClient
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ContextMenuListener(private val commandClient: CommandClient) : ListenerAdapter() {
    override fun onUserContextInteraction(event: UserContextInteractionEvent) {
        if(event.isAcknowledged)
            return
        commandClient.getContextMenuEntry(event.name)!!.execute(event)
    }
}