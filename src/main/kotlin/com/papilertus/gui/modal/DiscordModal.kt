package com.papilertus.gui.modal

import com.papilertus.gui.modal.ModalRegistry.registerModal
import com.papilertus.gui.modal.ModalRegistry.unregisterModal
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.modals.Modal
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DiscordModal {
    private val id: String
    var userId = ""
        private set
    val modallable: Modallable
    private val inputs: Array<out TextInput>
    private var modal: Modal? = null
    private val title: String

    constructor(
        userId: String,
        title: String,
        modallable: (event: ModalInteractionEvent) -> Unit,
        vararg inputs: TextInput
    ) {
        id = UUID.randomUUID().toString()
        this.userId = userId
        this.modallable = object : Modallable {
            override fun onModalInteract(event: ModalInteractionEvent) {
                modallable(event)
            }
        }
        this.inputs = inputs
        this.title = title
    }

    constructor(title: String, modallable: (event: ModalInteractionEvent) -> Unit, vararg inputs: TextInput) {
        id = UUID.randomUUID().toString()
        this.modallable = object : Modallable {
            override fun onModalInteract(event: ModalInteractionEvent) {
                modallable(event)
            }
        }
        this.inputs = inputs
        this.title = title
    }

    fun buildModal(): Modal {
        val builder = Modal.create(id, title)
        for (input in inputs) {
            builder.addActionRows(ActionRow.of(input))
        }
        modal = builder.build()
        registerModal(id, this)

        //for unregistering
        val executorService = Executors.newSingleThreadScheduledExecutor()
        executorService.schedule({
            unregisterModal(id)
            executorService.shutdown()
        }, 25, TimeUnit.MINUTES)
        return modal!!
    }
}