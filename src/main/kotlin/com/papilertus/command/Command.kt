package com.papilertus.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

abstract class Command(
    val name: String,
    val description: String,
    val privateCommand: Boolean = false,
    val enabled: Boolean = true
) {
    val commandData: SlashCommandData = Commands.slash(name, description)

    public abstract fun execute(event: SlashCommandInteractionEvent)

}