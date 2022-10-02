package com.papilertus.gui.modal

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

interface Modallable {
    fun onModalInteract(event: ModalInteractionEvent)
}