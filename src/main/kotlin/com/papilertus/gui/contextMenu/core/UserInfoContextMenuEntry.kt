package com.papilertus.gui.contextMenu.core

import com.papilertus.gui.contextMenu.ContextMenuEntry
import dev.minn.jda.ktx.messages.Embed
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent

class UserInfoContextMenuEntry : ContextMenuEntry("Info") {
    override fun execute(event: UserContextInteractionEvent) {

        val embed = Embed {
            title = event.target.name
            field {
                name = "ID"
                value = event.target.id
            }
        }

        event.replyEmbeds(embed).setEphemeral(true).queue()
    }
}