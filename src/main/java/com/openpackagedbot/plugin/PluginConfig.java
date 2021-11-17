package com.openpackagedbot.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class PluginConfig {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final JsonObject config;
    private PluginData data;

    public PluginConfig(PluginData data) {
        this.data = data;

        File file = new File(data.getName() + ".json");

        if (file.exists()) {
            config = read(file);
        } else {
            config = new JsonObject();
            save(file);
        }

    }

    private void save(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject read(File file) {
        try (FileReader reader = new FileReader(file)) {
            return JsonParser.parseReader(reader).getAsJsonObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public PluginConfig() {
        config = new JsonObject();
    }

    public void addEntry(String name, String value) {
        config.addProperty(name, value);
    }

    public void addEntry(String name, int value) {
        config.addProperty(name, value);
    }

    public void addEntry(String name, double value) {
        config.addProperty(name, value);
    }

    public String readString(String name) {
        return config.get(name).getAsString();
    }

    public int readInt(String name) {
        return config.get(name).getAsInt();
    }

    @Override
    public String toString() {
        return gson.toJson(config);
    }
}
