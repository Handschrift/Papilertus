package com.papilertus.birthdays.init;

import com.papilertus.birthdays.commands.BirthdayCommand;
import com.papilertus.birthdays.commands.WishlistCommand;
import com.papilertus.birthdays.database.BirthdayUser;
import com.papilertus.birthdays.database.GuildDatabase;
import com.papilertus.birthdays.database.UserDatabase;
import com.papilertus.commands.core.Command;
import com.papilertus.init.Papilertus;
import com.papilertus.plugin.Plugin;
import com.papilertus.plugin.PluginData;
import com.papilertus.plugin.PluginDataStore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.EventListener;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Birthdays implements Plugin {
    private static PluginDataStore store;

    @Override
    public void onLoad(PluginData pluginData) {
        store = new PluginDataStore(pluginData);
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        final EmbedBuilder notificationBuilder = new EmbedBuilder();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (BirthdayUser user : UserDatabase.getByDate(LocalDate.now())) {
                    if (Papilertus.getPluginJDA() == null) //jda didn't load yet
                        return;

                    final User discordUser = Papilertus.getPluginJDA().getUserById(user.getUserId());
                    final String channelId = GuildDatabase.getBirthdayGuild(user.getGuildId()).getChannelId();

                    if (channelId != null) {
                        notificationBuilder.setDescription("Happy birthday to " + discordUser.getName() + " \uD83C\uDF89")
                                .setColor(Color.CYAN);
                        final Message notification = new MessageBuilder().setEmbeds(notificationBuilder.build()).setContent(discordUser.getAsMention()).build();
                        Papilertus.getPluginJDA().getTextChannelById(channelId).sendMessage(notification).queue();
                        UserDatabase.updateUser(user.getUserId(), user.getGuildId(), "birthday", user.getBirthdayDate().plusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    @Override
    public List<Command> getCommands() {
        final ArrayList<Command> commands = new ArrayList<>();
        commands.add(new BirthdayCommand());
        commands.add(new WishlistCommand());
        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        return null;
    }

    @Override
    public void onUnload() {

    }

    public static PluginDataStore getDataStore() {
        return store;
    }
}
