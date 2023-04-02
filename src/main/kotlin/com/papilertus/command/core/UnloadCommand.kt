package com.papilertus.command.core

import com.papilertus.command.Command
import com.papilertus.command.CommandClient
import com.papilertus.plugin.PluginLoader
import com.papilertus.plugin.PluginUnloadResult
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType

class UnloadCommand(
    private val pluginLoader: PluginLoader,
    private val jda: JDA,
    private val commandClient: CommandClient
) :
    Command("unload", "Unloads a plugin", false, true, false, true) {

    init {
        this.commandData
            .addOption(OptionType.STRING, "plugin", "Plugin to unload", true)
            .addOption(OptionType.STRING, "guild", "Guild of unloaded plugin", false)
    }

    override fun execute(event: SlashCommandInteractionEvent): Boolean {
        val name = event.getOption("plugin")!!.asString
        val guild = event.getOption("guild")?.asString

        val result = if (guild == null) pluginLoader.unload(name, jda, commandClient) else
            pluginLoader.unload(
                name,
                jda,
                guild
            )


        when (result) {

            PluginUnloadResult.Success ->
                event.reply("The plugin $name has been unloaded successfully.").queue()

            is PluginUnloadResult.Error.PluginNotFound -> event.reply("The plugin $name doesn't exist.").queue()
            is PluginUnloadResult.Error.GuildNotFound -> event.reply("The guild $guild doesn't exist.")
        }

        return true
    }
}