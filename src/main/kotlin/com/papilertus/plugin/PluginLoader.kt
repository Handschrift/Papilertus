package com.papilertus.plugin

import com.papilertus.command.Command
import com.papilertus.command.CommandClient
import com.papilertus.gui.contextMenu.ContextMenuEntry
import com.papilertus.init.jda
import com.papilertus.init.logger
import com.papilertus.plugin.PluginData.Companion.getFromJson
import net.dv8tion.jda.api.hooks.EventListener
import java.io.FileInputStream
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarInputStream
import kotlin.io.path.*
import kotlin.system.exitProcess

private data class LoadedPlugin(
    val pluginData: PluginData,
    val rawClass: Class<*>,
    val commands: List<Command>,
    val contextMenuEntries: List<ContextMenuEntry>,
    val listeners: List<EventListener>
) {
    fun unload() {
        val unloadMethod = rawClass.getMethod("onUnload")

        val t = rawClass.getDeclaredConstructor()
        val instance = t.newInstance()

        unloadMethod.invoke(instance)
    }
}

sealed class PluginUnloadResult {
    object Success : PluginUnloadResult()
    sealed class Error : PluginUnloadResult() {
        data class PluginNotFound(val name: String) : Error()
        data class GuildNotFound(val guildId: String) : Error()
    }
}

class PluginLoader(initialPath: String) {
    private val initialPath = Path(initialPath)
    private val loadedPlugins = mutableListOf<LoadedPlugin>()

    fun load() {
        if (initialPath.notExists()) {
            initialPath.createDirectory()
            return
        }
        for (s in initialPath.listDirectoryEntries()) {
            logger.info("Loading ${s.name}...")
            val path = s.pathString

            val data: PluginData? = getPluginDataByPath(path)

            if (data == null || data.name.isEmpty() || data.mainClass.isEmpty()) {
                System.err.println("Plugin Couldn't be loaded!")
                exitProcess(-1)
            }

            loadedPlugins.add(
                getLoadedPluginFromData(data, path)
            )

            logger.info("${s.name} loaded!")
        }
    }

    fun unload(name: String, commandClient: CommandClient): PluginUnloadResult {
        val plugin = this.loadedPlugins.find { it.pluginData.name == name }
        return if (plugin != null) {
            this.loadedPlugins.remove(plugin)

            jda.removeEventListener(plugin.listeners)
            jda.updateCommands().addCommands(plugin.commands.map { it.commandData }).queue()
            jda.updateCommands().addCommands(plugin.contextMenuEntries.map { it.contextMenuData }).queue()
            commandClient.removeCommands(plugin.commands)
            commandClient.removeContextMenuEntries(plugin.contextMenuEntries)
            plugin.unload()
            PluginUnloadResult.Success
        } else {
            PluginUnloadResult.Error.PluginNotFound(name)
        }
    }

    fun unload(name: String, guildId: String): PluginUnloadResult {
        val plugin = this.loadedPlugins.find { it.pluginData.name == name }
        if (plugin != null) {

            val guild = jda.getGuildById(guildId) ?: return PluginUnloadResult.Error.GuildNotFound(guildId)

            guild.updateCommands().addCommands(plugin.commands.map { it.commandData }).queue()
            guild.updateCommands().addCommands(plugin.contextMenuEntries.map { it.contextMenuData })
                .queue()
            return PluginUnloadResult.Success
        } else {
            return PluginUnloadResult.Error.PluginNotFound(name)
        }
    }

    fun reload(name: String, commandClient: CommandClient) {
        unload(name, commandClient)
        loadPluginByNameRT(name, commandClient)

    }

    fun unloadAll() {
        for (c in loadedPlugins) {
            c.unload()
        }
    }

    fun loadPluginByNameRT(name: String, commandClient: CommandClient) {
        if (initialPath.notExists()) {
            initialPath.createDirectory()
            return
        }
        for (s in initialPath.listDirectoryEntries()) {
            val path = s.pathString

            val data = getPluginDataByPath(path)

            if (data == null || data.name.isEmpty() || data.mainClass.isEmpty())
                continue

            if (data.name != name)
                continue

            loadedPlugins.add(
                getLoadedPluginFromData(data, path)
            )
            commandClient.addCommands(*getCommands().toTypedArray())
            commandClient.addContextMenuEntries(*getContextMenuEntries().toTypedArray())
            jda.addEventListener(*getListeners().toTypedArray())


            logger.info("$s loaded!")

        }
    }

    fun getCommands(): List<Command> {
        return loadedPlugins.map { it.commands }.flatten()
    }

    fun getContextMenuEntries(): List<ContextMenuEntry> {

        return loadedPlugins.map { it.contextMenuEntries }.flatten()
    }

    fun getListeners(): List<EventListener> {

        return loadedPlugins.map { it.listeners }.flatten()
    }

    private fun getPluginDataByPath(path: String): PluginData? {
        val file = JarFile(path)
        val je = file.getJarEntry("plugin.json") ?: return null

        val fileStream = FileInputStream(path)
        val jarStream = JarInputStream(fileStream)
        var jeTemp: JarEntry?
        var data: PluginData? = null

        while (jarStream.nextJarEntry.also { jeTemp = it } != null) {
            if (jeTemp!!.name != "plugin.json") continue
            val pluginData = ByteArray(je.size.toInt())
            jarStream.read(pluginData, 0, pluginData.size)
            data = getFromJson(String(pluginData))
            break
        }
        fileStream.close()
        jarStream.close()
        return data
    }

    private fun getLoadedPluginFromData(data: PluginData, path: String): LoadedPlugin {

        val file = JarFile(path)
        val urls = arrayOf(URI.create("jar:file:$path!/").toURL())
        val cl = URLClassLoader.newInstance(urls)

        val mainClassName = data.mainClass.replace(".", "/") + ".class"
        val mainClass = file.getJarEntry(mainClassName)
        var className = mainClass.name.substring(0, mainClass.name.length - 6)
        className = className.replace('/', '.')
        val c = cl.loadClass(className)

        val loadMethod = c.getMethod("onLoad", PluginData::class.java)

        val t = c.getDeclaredConstructor()
        val instance = t.newInstance()
        loadMethod.invoke(instance, data)

        val commandMethod = c.getMethod("getCommands")
        val listenerMethod = c.getMethod("getListeners")
        val contextMenuEntryMethod = c.getMethod("getContextMenuEntries")

        val commandList = commandMethod.invoke(instance)
        val listenerList = listenerMethod.invoke(instance)
        val contextMenuEntryList = contextMenuEntryMethod.invoke(instance)

        return LoadedPlugin(
            data,
            c,
            commandList as List<Command>,
            contextMenuEntryList as List<ContextMenuEntry>,
            listenerList as List<EventListener>
        )
    }
}