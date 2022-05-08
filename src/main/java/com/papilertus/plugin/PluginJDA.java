package com.papilertus.plugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class PluginJDA {
    private final JDA jda;

    public PluginJDA(JDA jda) {
        this.jda = jda;
    }

    public TextChannel getTextChannelById(String id) {
        return jda.getTextChannelById(id);
    }

    public User getUserById(String id) {
        return jda.getUserById(id);
    }
}
