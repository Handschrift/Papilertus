package com.papilertus.gui.modal

import com.papilertus.gui.modal.ModalRegistry.getModalById
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ModalListener : ListenerAdapter() {
    override fun onModalInteraction(event: ModalInteractionEvent) {
        val modal = getModalById(event.modalId)
        if (modal == null) {
            event.reply("This modal expired! Please execute the command again.").setEphemeral(true).queue()
            return
        }
        if (modal.userId.isNotEmpty() && event.user.id != modal.userId) {
            event.reply("You are not authorized to press this button").setEphemeral(true).queue()
            return
        }
        modal.modallable.onModalInteract(event)
    }
}