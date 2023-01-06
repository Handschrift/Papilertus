package com.papilertus.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener(private val commandClient: CommandClient) : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if(event.isAcknowledged)
            return
        commandClient.getCommand(event.name)!!.execute(event)
    }
}