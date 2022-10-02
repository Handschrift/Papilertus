package com.papilertus.gui.modal

object ModalRegistry {
    private val modals = HashMap<String, DiscordModal>()
    @JvmStatic
    fun registerModal(id: String, button: DiscordModal) {
        modals[id] = button
    }

    @JvmStatic
    fun unregisterModal(id: String) {
        modals.remove(id)
    }

    @JvmStatic
    fun getModalById(id: String): DiscordModal? {
        return modals[id]
    }
}