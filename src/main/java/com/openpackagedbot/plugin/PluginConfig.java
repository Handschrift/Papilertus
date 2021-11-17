package com.openpackagedbot.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class PluginConfig {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final JsonObject config = new JsonObject();
    private PluginData data;

    public PluginConfig(PluginData data) {
        this.data = data;
    }

    public PluginConfig() {

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
        return "";
    }

    @Override
    public String toString() {
        return gson.toJson(config);
    }
}
