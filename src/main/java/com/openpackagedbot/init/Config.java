package com.openpackagedbot.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public final class Config {

    private static transient Config config = new Config();
    private transient final File file = new File("config/config.json");

    private String token = "";
    private String pluginDir = "plugins/";
    private String databaseUrl = "mongodb://localhost/";
    private String databaseUsername = "admin";
    private String databasePassword = "admin";

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
}
