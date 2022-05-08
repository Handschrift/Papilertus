package com.economy.init;

import com.economy.commands.*;
import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.database.models.EconomyUserInventoryEntry;
import com.economy.game.element.Forecast;
import com.economy.listeners.BumpListener;
import com.economy.listeners.MessageReceivedListener;
import com.economy.listeners.VoiceJoinListener;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.papilertus.commands.core.Command;
import com.papilertus.plugin.Plugin;
import com.papilertus.plugin.PluginConfig;
import com.papilertus.plugin.PluginData;
import com.papilertus.plugin.PluginDataStore;
import net.dv8tion.jda.api.hooks.EventListener;
import org.bson.Document;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Economy implements Plugin {
    //singleton for database
    private static PluginDataStore store;
    private static PluginConfig config;

    private static EconomyConfig economyConfig;

    @Override
    public void onLoad(PluginData pluginData) {
        store = new PluginDataStore(pluginData);
        config = new PluginConfig(pluginData);
        //<:papilertus:963118768614150224>

        config.fromObject(new EconomyConfig());
        economyConfig = config.toObject(EconomyConfig.class);

        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                final LocalDate today = LocalDate.now();

                if (!today.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                    return;
                }

                store.modifyEntries(new Document(), Updates.set("weeklyCurrency", 0));
            }
        }, 24, 24, TimeUnit.HOURS);

        //This code is quite messy because of the performance
        if (economyConfig.isAutoCollect()) {
            service.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    final FindIterable<Document> results = store.getIterableList(Filters.empty());

                    for (Document current : results) {

                        final EconomyUser user = new Gson().fromJson(current.toJson(), EconomyUser.class);
                        boolean modified = false;
                        final ListIterator<EconomyUserInventoryEntry> iterator = user.getInventory().getEntries().listIterator();

                        while (iterator.hasNext()) {
                            final EconomyUserInventoryEntry entry = iterator.next();
                            if (entry.isMature()) {
                                iterator.remove();
                                double coins = Forecast.getForecast().getData()[0].getValue() * entry.getCount();
                                user.addCoins(coins);
                                modified = true;
                            }
                        }
                        if (modified) {
                            UserDatabase.updateUser(user);
                        }
                    }
                }
            }, 0, 1, TimeUnit.HOURS);
        }

    }

    @Override
    public List<Command> getCommands() {

        final ArrayList<Command> commands = new ArrayList<>();
        commands.add(new ProfileCommand());
        commands.add(new WorkCommand());
        commands.add(new ShopCommand());
        commands.add(new SellCommand());
        commands.add(new DailyCommand());
        commands.add(new LeaderboardCommand());
        commands.add(new GiveCommand());
        commands.add(new ForecastCommand());
        commands.add(new InventoryCommand());
        commands.add(new HelpCommand());
        if (!economyConfig.isAutoCollect())
            commands.add(new CollectCommand());

        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        final ArrayList<EventListener> listeners = new ArrayList<>();
        listeners.add(new MessageReceivedListener());
        listeners.add(new VoiceJoinListener());
        listeners.add(new BumpListener());
        return listeners;
    }

    @Override
    public void onUnload() {

    }

    public static PluginDataStore getDataStore() {
        return store;
    }

    @Deprecated
    public static PluginConfig getConfig() {
        return config;
    }

    public static EconomyConfig getEconomyConfig() {
        return economyConfig;
    }

}
