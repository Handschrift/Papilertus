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
    private static final File initialPath = new File("plugins/");
    private static final ArrayList<Command> commands = new ArrayList<>();

    public static void load() {
        for (String s : initialPath.list()) {
            System.out.println(s);
            String path = "plugins/" + s;
            try {
                JarFile file = new JarFile(path);
                URL[] urls = {new URL("jar:file:" + path + "!/")};
                URLClassLoader cl = URLClassLoader.newInstance(urls);
                JarEntry je = file.getJarEntry("plugin.json");
                FileInputStream fileStream = new FileInputStream(path);
                JarInputStream jarStream = new JarInputStream(fileStream);
                JarEntry jeTemp;
                PluginData data = null;

                while ((jeTemp = jarStream.getNextJarEntry()) != null && jeTemp.getName().equals("plugin.json")) {
                    byte[] pluginData = new byte[(int) je.getSize()];
                    jarStream.read(pluginData, 0, pluginData.length);
                    data = PluginData.getFromJson(new String(pluginData));

                }

                if (data == null) {
                    System.err.println("PluginCouldn't be loaded!");
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

                List<Command> currentCommands = (List<Command>) commandMethod.invoke(instance);

                commands.addAll(currentCommands);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Command> getRegisteredCommands() {
        return commands;
    }
}
