package com.papilertus.plugin

import com.google.gson.Gson
import com.mongodb.client.MongoCollection
import com.papilertus.init.Database
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

    inline fun <reified T : Any> registerConfig(): T {
        val configFile = File("config/$name.toml")

        if (!configFile.exists()) {
            val r = ClassLoader.getSystemClassLoader().getResourceAsStream(configFile.name)
            println(r)
            println(configFile.name)
            if (r != null) {
                Files.copy(r, configFile.toPath())
            }
        }

        return ConfigLoaderBuilder.default()
            .addFileSource(configFile)
            .build()
            .loadConfigOrThrow()
    }

    fun <T> getPluginDataStore(name: String, clazz: Class<T>): MongoCollection<T> {
        return Database.connection!!.getDatabase("Papilertus_${this.name}").getCollection(name ,clazz)
    }
}