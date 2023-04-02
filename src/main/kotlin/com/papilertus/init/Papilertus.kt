package com.papilertus.init

import com.papilertus.command.CommandClient
import com.papilertus.command.CommandListener
import com.papilertus.command.core.FeedbackCommand
import com.papilertus.command.core.ReloadCommand
import com.papilertus.command.core.UnloadCommand
import com.papilertus.gui.contextMenu.ContextMenuListener
import com.papilertus.gui.contextMenu.core.UserInfoContextMenuEntry
import com.papilertus.gui.modal.ModalListener
import com.papilertus.plugin.PluginLoader
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import dev.minn.jda.ktx.jdabuilder.createJDA
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.requests.GatewayIntent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

private lateinit var config: Config
lateinit var jda: JDA
    private set
val logger: Logger = LoggerFactory.getLogger("Papilertus")
fun main() {
    val configTomlFile = File("config/Papilertus.toml")

    if (!configTomlFile.parentFile.exists()) {
        configTomlFile.parentFile.mkdir()
    }

    if (!configTomlFile.exists()) {
        logger.info("No config file cannot be found... creating one...")
        val r = ClassLoader.getSystemClassLoader().getResourceAsStream(configTomlFile.name)
        if (r == null) {
            logger.error("An error occurred while creating a logging file")
            exitProcess(1)
        }
        Files.copy(r, configTomlFile.toPath())
        exitProcess(0)
    }

    config = ConfigLoaderBuilder.default()
        .addFileSource(configTomlFile)
        .build()
        .loadConfigOrThrow()

    Database.connect(config)


    jda =
        createJDA(config.token, enableCoroutines = true, intents = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))

    val commandClient = CommandClient()

    val loader = PluginLoader(config.pluginDir)
    loader.load()

    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() = runBlocking {
            logger.info("Shutting down Papilertus and unloading plugins...")
            loader.unloadAll()
            if (Database.connection != null)
                Database.connection!!.close()
        }
    })

    if (!config.disableAllCoreInteractions) {
        commandClient.addCommands(
            FeedbackCommand(config),
            UnloadCommand(loader, commandClient),
            ReloadCommand(loader, commandClient)
        )
        commandClient.addContextMenuEntries(
            UserInfoContextMenuEntry()
        )
    }

    commandClient.addCommands(*loader.getCommands().toTypedArray())
    commandClient.addContextMenuEntries(*loader.getContextMenuEntries().toTypedArray())


    jda.addEventListener(*loader.getListeners().toTypedArray())

    jda.addEventListener(CommandListener(commandClient))
    jda.addEventListener(ContextMenuListener(commandClient))
    jda.addEventListener(ModalListener())

    jda.awaitReady()

    jda.updateCommands().addCommands(commandClient.getData()).complete()
    logger.info("Papilertus started successfully")


}