package com.openpackagedbot.plugin;

import com.google.gson.Gson;

public final class PluginData {
    private final String author;
    private final String mainClass;

    public static PluginData getFromJson(String json) {
        return new Gson().fromJson(json, PluginData.class);
    }

    private PluginData(String author, String mainClass) {
        this.author = author;
        this.mainClass = mainClass;
    }

    public String getAuthor() {
        return author;
    }

    public String getMainClass() {
        return mainClass;
    }
}
