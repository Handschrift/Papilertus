package com.papilertus.init;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.papilertus.commands.core.ConfigCommandTicket;
import com.papilertus.misc.ConfigMiscTicket;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Config {

    private static transient Config config = new Config();
    private transient final File file = new File("config/Papilertus.yaml");
    private transient final ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

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

    private ArrayList<String> possibleNotificationMessages = new ArrayList<>() {{
        add("This is an open source bot!");
        add("you can host it yourself");
        add("you can send feedback with feedback");
        add("You can add plugins to the bot");
        add("You can turn off this message in the config");
    }};

    private int probabilityForNotifications = 1; //1%

    static Config getConfig() {
        config = config.read();
        return config;
    }

    public static Config getConfig(ConfigCommandTicket ticket) {
        return getConfig();
    }

    public static Config getConfig(ConfigMiscTicket ticket) {
        return getConfig();
    }

    private void save() {
        try {
            mapper.writeValue(file, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Config read() {
        try {
            return mapper.readValue(file, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public ArrayList<String> getPossibleNotificationMessages() {
        return possibleNotificationMessages;
    }

    public int getProbabilityForNotifications() {
        return probabilityForNotifications;
    }
}
