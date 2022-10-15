package com.papilertus.plugin

import com.google.gson.Gson
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import java.io.File
import java.nio.file.Files

class PluginData private constructor(val author: String, val mainClass: String, val name: String) {

    companion object {
        @JvmStatic
        fun getFromJson(json: String?): PluginData {
            return Gson().fromJson(json, PluginData::class.java)
        }
    }

    fun <T> registerConfig(config: T) {
        val configFile = File("config/$name.toml")

        if (!configFile.exists()) {
            val r = ClassLoader.getSystemClassLoader().getResourceAsStream(configFile.name)
            if (r != null) {
                Files.copy(r, configFile.toPath())
            }
        }

        return ConfigLoaderBuilder.default()
            .addFileSource(configFile)
            .build()
            .loadConfigOrThrow()
    }
}