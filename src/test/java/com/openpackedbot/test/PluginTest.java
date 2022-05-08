package com.openpackedbot.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.papilertus.plugin.PluginConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PluginTest {

    @Test
    public void testEmptyConfig() {
        PluginConfig config = new PluginConfig();
        Assertions.assertEquals("{}", config.toString());
    }

    @Test
    public void testConfigWithEntry() {
        PluginConfig config = new PluginConfig();
        config.addEntry("min", 23);

        JsonObject object = new JsonObject();
        object.addProperty("min", 23);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Assertions.assertEquals(gson.toJson(object), config.toString());
    }
}
