package com.papilertus.plugin

import net.dv8tion.jda.api.hooks.EventListener

interface Plugin {
    fun onLoad()

    fun getCommands(): List<Void>

    fun getListeners(): List<EventListener?>

    fun onUnload()
}