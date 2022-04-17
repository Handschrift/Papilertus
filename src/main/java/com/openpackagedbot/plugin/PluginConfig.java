package com.openpackagedbot.plugin;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class PluginConfig {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final JsonObject config;
    private PluginData data;
    private File file;

    public PluginConfig(PluginData data) {
        this.data = data;

        file = new File("config/" + data.getName() + ".json");

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
        if (!config.has(name)) {
            config.addProperty(name, value);
            save(file);
        }
    }

    public void addEntry(String name, int value) {
        if (!config.has(name)) {
            config.addProperty(name, value);
            save(file);
        }
    }

    public void addEntry(String name, double value) {
        if (!config.has(name)) {
            config.addProperty(name, value);
            save(file);
        }
    }

    public void addEntry(String name, boolean value) {
        if (!config.has(name)) {
            config.addProperty(name, value);
            save(file);
        }
    }

    public void addEntry(String name, String[] values) {
        if (!config.has(name)) {
            final JsonArray array = new JsonArray();
            for (String s : values) {
                array.add(s);
            }
            config.add(name, array);
            save(file);
        }
    }

    public <T> void addEntry(String name, T[] values) {
        if (!config.has(name)) {
            final JsonArray array = new JsonArray();
            for (T t : values) {
                array.add(JsonParser.parseString(gson.toJson(t)));
            }
            config.add(name, array);
            save(file);
        }
    }

    public String readString(String name) {
        return config.get(name).getAsString();
    }

    public int readInt(String name) {
        return config.get(name).getAsInt();
    }

    public boolean readBoolean(String name) {
        return config.get(name).getAsBoolean();
    }

    public JsonArray readArray(String name) {
        return config.get(name).getAsJsonArray();
    }

    public <T> ArrayList<T> readType(String name, Class<T> c) {
        final ArrayList<T> elements = new ArrayList<>();
        for (JsonElement element : config.get(name).getAsJsonArray()) {
            elements.add(gson.fromJson(element.toString(), c));
        }
        return elements;
    }

    @Override
    public String toString() {
        return gson.toJson(config);
    }
}
