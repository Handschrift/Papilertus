package com.papilertus.init

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import dev.minn.jda.ktx.jdabuilder.createJDA
import net.dv8tion.jda.api.requests.GatewayIntent
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

lateinit var config: Config;
fun main() {
    val configTomlFile = File("Papilertus.toml")

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

    val jda =
        createJDA(config.token, enableCoroutines = true, intents = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))

}