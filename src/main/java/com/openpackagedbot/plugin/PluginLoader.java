package com.openpackagedbot.plugin;

import com.openpackagedbot.commands.core.Command;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class PluginLoader {
    private final File initialPath;
    private final ArrayList<Command> commands = new ArrayList<>();

    public PluginLoader(String path) {
        initialPath = new File(path);
    }

    public void load() {
        for (String s : initialPath.list()) {
            System.out.println("Loading " + s + "...");
            String path = initialPath.getPath() + "/" + s;
            try {
                JarFile file = new JarFile(path);
                URL[] urls = {new URL("jar:file:" + path + "!/")};
                URLClassLoader cl = URLClassLoader.newInstance(urls);
                JarEntry je = file.getJarEntry("plugin.json");

                if (je == null)
                    continue;

                FileInputStream fileStream = new FileInputStream(path);
                JarInputStream jarStream = new JarInputStream(fileStream);
                JarEntry jeTemp;
                PluginData data = null;

                while ((jeTemp = jarStream.getNextJarEntry()) != null) {

                    if (!jeTemp.getName().equals("plugin.json"))
                        continue;

                    byte[] pluginData = new byte[(int) je.getSize()];
                    jarStream.read(pluginData, 0, pluginData.length);
                    data = PluginData.getFromJson(new String(pluginData));

                }

                if (data == null) {
                    System.err.println("Plugin Couldn't be loaded!");
                    System.exit(-1);
                }

                String mainClassName = data.getMainClass().replace(".", "/") + ".class";
                JarEntry mainClass = file.getJarEntry(mainClassName);
                String className = mainClass.getName().substring(0, mainClass.getName().length() - 6);
                className = className.replace('/', '.');
                Class<?> c = cl.loadClass(className);

                Method loadMethod = c.getMethod("onLoad");

                Constructor<?> t = c.getDeclaredConstructor();
                Object instance = t.newInstance();
                loadMethod.invoke(instance);

                Method commandMethod = c.getMethod("getCommands");
                Object commandList = commandMethod.invoke(instance);

                if (commandList instanceof List) {
                    List<Command> currentCommands = (List<Command>) commandList;

                    commands.addAll(currentCommands);
                    System.out.println(s + " loaded!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Command> getRegisteredCommands() {
        return commands;
    }
}
