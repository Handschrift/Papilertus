package com.papilertus.plugin

import com.papilertus.command.Command
import com.papilertus.gui.contextMenu.ContextMenuEntry
import com.papilertus.plugin.PluginData.Companion.getFromJson
import net.dv8tion.jda.api.hooks.EventListener
import java.io.File
import java.io.FileInputStream
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarInputStream
import kotlin.system.exitProcess

class PluginLoader(initialPath: String) {
    private val initialPath = File(initialPath)
    private val loadedPlugins = mutableListOf<Class<*>>()
    val commands = mutableListOf<Command>()
    val eventListeners = mutableListOf<EventListener>()
    val contextMenuEntries = mutableListOf<ContextMenuEntry>()

    fun load() {
        if (!initialPath.exists()) {
            initialPath.mkdir()
            return
        }
        for (s in initialPath.list()!!) {
            println("Loading $s...")
            val path = initialPath.path + "/" + s

            val file = JarFile(path)
            val urls = arrayOf(URL("jar:file:$path!/"))
            val cl = URLClassLoader.newInstance(urls)
            val je = file.getJarEntry("plugin.json") ?: continue

            val fileStream = FileInputStream(path)
            val jarStream = JarInputStream(fileStream)
            var jeTemp: JarEntry?
            var data: PluginData? = null

            while (jarStream.nextJarEntry.also { jeTemp = it } != null) {
                if (jeTemp!!.name != "plugin.json") continue
                val pluginData = ByteArray(je.size.toInt())
                jarStream.read(pluginData, 0, pluginData.size)
                data = getFromJson(String(pluginData))
            }

            if (data == null || data.name.isEmpty() || data.mainClass.isEmpty()) {
                System.err.println("Plugin Couldn't be loaded!")
                exitProcess(-1)
            }

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

            if (listenerList is List<*>) {
                for (o in listenerList) {
                    eventListeners.add(o as EventListener)
                }
            }

            if (commandList is List<*>) {
                for (o in commandList) {
                    commands.add(o as Command)
                }
            }

            if (contextMenuEntryList is List<*>) {
                for (o in contextMenuEntryList) {
                    contextMenuEntries.add(o as ContextMenuEntry)
                }
            }

            loadedPlugins.add(c)

            println("$s loaded!")

            fileStream.close()
            jarStream.close()

        }
    }
}