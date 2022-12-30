package com.papilertus.init

import com.papilertus.command.CommandClient
import com.papilertus.command.CommandListener
import com.papilertus.command.core.FeedbackCommand
import com.papilertus.gui.contextMenu.ContextMenuListener
import com.papilertus.gui.contextMenu.core.UserInfoContextMenuEntry
import com.papilertus.gui.modal.ModalListener
import com.papilertus.plugin.PluginLoader
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import dev.minn.jda.ktx.jdabuilder.createJDA
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.requests.GatewayIntent
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

private lateinit var config: Config;
lateinit var jda: JDA;

fun main() {
    val configTomlFile = File("config/Papilertus.toml")

    if (!configTomlFile.parentFile.exists()) {
        configTomlFile.parentFile.mkdir()
    }

    if (!configTomlFile.exists()) {
        println("No config file cannot be found... creating one...")
        val r = ClassLoader.getSystemClassLoader().getResourceAsStream(configTomlFile.name)
        if (r == null) {
            System.err.println("There was an error creating the config file...");
            exitProcess(1)
        }
        Files.copy(r, configTomlFile.toPath());
        exitProcess(0)
    }

    config = ConfigLoaderBuilder.default()
        .addFileSource(configTomlFile)
        .build()
        .loadConfigOrThrow()

    connectDatabase(config)


    jda =
        createJDA(config.token, enableCoroutines = true, intents = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))

    val commandClient = CommandClient()

    val loader = PluginLoader(config.pluginDir)
    loader.load()

    if (!config.disableAllCoreInteractions) {
        commandClient.addCommands(
            FeedbackCommand(config)
        )
        commandClient.addContextMenuEntries(
            UserInfoContextMenuEntry()
        )
    }

    commandClient.addCommands(*loader.commands.toTypedArray())


    jda.addEventListener(CommandListener(commandClient))
    jda.addEventListener(ContextMenuListener(commandClient))
    jda.addEventListener(ModalListener())
    jda.addEventListener(*loader.eventListeners.toTypedArray())

    jda.awaitReady()

    jda.updateCommands().addCommands(commandClient.getData()).complete()

}