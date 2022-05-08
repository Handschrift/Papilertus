package com.papilertus.plugin;

import com.google.gson.Gson;

public final class PluginData {
    private final String author;
    private final String name;
    private final String mainClass;

    static PluginData getFromJson(String json) {
        return new Gson().fromJson(json, PluginData.class);
    }

    private PluginData(String author, String mainClass, String name) {
        this.author = author;
        this.mainClass = mainClass;
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getName() {
        return name;
    }
}
