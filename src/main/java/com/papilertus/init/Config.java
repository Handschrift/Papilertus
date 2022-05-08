package com.papilertus.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.*;
import java.util.ArrayList;

public final class Config {

    private static transient Config config = new Config();
    private transient final File file = new File("config/config.json");

    private String token = "";
    private String pluginDir = "plugins/";
    private String databaseUrl = "mongodb://localhost/";
    private String databaseUsername = "admin";
    private String databasePassword = "admin";
    private final ArrayList<CacheFlag> cacheFlags = new ArrayList<>() {
        {
            add(CacheFlag.EMOTE);
            add(CacheFlag.ACTIVITY);
            add(CacheFlag.CLIENT_STATUS);
            add(CacheFlag.ONLINE_STATUS);
        }
    };

    private String feedbackRecipientId = "";

    private String databaseName = "Papilertus";

    static Config getConfig() {
        config = config.read();
        return config;
    }

    private void save() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Config read() {
        final Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {

            final BufferedReader bufferedReader = new BufferedReader(reader);
            return gson.fromJson(bufferedReader, Config.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Config() {
        if (!file.exists()) {
            System.err.println("Config file not found... creating...");
            file.getParentFile().mkdir();
            save();
            System.err.println("Config file created... Shutting down");
            System.err.println("Please enter a token and the database credentials and restart!");
            System.exit(0);
        }
    }

    String getToken() {
        return token;
    }

    String getDatabaseUrl() {
        return databaseUrl;
    }

    String getDatabaseUsername() {
        return databaseUsername;
    }

    String getDatabasePassword() {
        return databasePassword;
    }

    String getPluginDir() {
        return pluginDir;
    }

    ArrayList<CacheFlag> getCacheFlags() {
        return cacheFlags;
    }

    String getFeedbackRecipientId() {
        return feedbackRecipientId;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
