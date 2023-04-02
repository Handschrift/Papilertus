package com.papilertus.command.core

import com.papilertus.command.Command
import com.papilertus.command.CommandClient
import com.papilertus.plugin.PluginLoader
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType

class ReloadCommand(private val loader: PluginLoader, private val commandClient: CommandClient) :
    Command("reload", "Reloads a plugin", false, true, false, true) {
    init {
        this.commandData
            .addOption(OptionType.STRING, "plugin", "Plugin to unload", true)
    }

    override fun execute(event: SlashCommandInteractionEvent): Boolean {
        val name = event.getOption("plugin")!!.asString
        loader.reload(name, commandClient)
        event.reply("Plugin has been reloaded!").queue()
        return true
    }
}