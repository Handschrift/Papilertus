package com.papilertus.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

abstract class Command(
    val name: String,
    val description: String,
    val guildOnly: Boolean = false,
    val enabled: Boolean = true,
    val nsfw: Boolean = false,
    val adminCommand: Boolean = false
) {
    val commandData: SlashCommandData = Commands.slash(name, description)
        .setGuildOnly(guildOnly)
        .setNSFW(nsfw)
        .setDefaultPermissions(if(adminCommand) DefaultMemberPermissions.DISABLED else DefaultMemberPermissions.ENABLED)


    abstract fun execute(event: SlashCommandInteractionEvent): Boolean

}