package com.economy.init;

import com.economy.commands.ProfileCommand;
import com.economy.commands.ShopCommand;
import com.economy.commands.WorkCommand;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.listeners.BumpListener;
import com.economy.listeners.MessageReceivedListener;
import com.economy.listeners.VoiceJoinListener;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.plugin.Plugin;
import com.openpackagedbot.plugin.PluginConfig;
import com.openpackagedbot.plugin.PluginData;
import com.openpackagedbot.plugin.PluginDataStore;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.List;

public class Economy implements Plugin {
    //singleton for database
    private static PluginDataStore store;
    private static PluginConfig config;
    private final EventWaiter waiter = new EventWaiter();

    @Override
    public void onLoad(PluginData pluginData) {
        store = new PluginDataStore(pluginData);
        config = new PluginConfig(pluginData);
        //<:papilertus:963118768614150224>
        config.addEntry("currency_name", "Plants");
        config.addEntry("currency_icon", ":deciduous_tree:");
        config.addEntry("collectable_name", "Seeds");
        config.addEntry("collectable_icon", ":seedling:");
        config.addEntry("coin_on_message_sent", true);
        config.addEntry("coin_message_cooldown", 20);
        config.addEntry("coin_on_voice_activity", true);
        config.addEntry("base_coin_on_message_amount", 10);
        config.addEntry("coin_voice_cooldown", 20);
        config.addEntry("base_coin_on_voice_activity_amount", 10);
        config.addEntry("whitelist_mode", false);
        config.addEntry("listed_users", new String[]{});
        config.addEntry("listed_roles", new String[]{});
        config.addEntry("enable_work_minigame", true);
        config.addEntry("work_cooldown", 60);
        config.addEntry("base_work_gain", 20);
        config.addEntry("convert_command_name", "plant");
        config.addEntry("upgrades", new GameUpgrade[]{
                new GameUpgrade("Monarch Butterfly", "Upgrades seed gain by voice", IncrementType.VOICE, ":butterfly:", 1.2F, 1.1F),
                new GameUpgrade("Periander Metalmark", "Upgrades seed gain by message", IncrementType.MESSAGE, ":butterfly:", 1.2F, 1.1F),
                new GameUpgrade("Mountain Apollo", "Upgrades seed gain by work", IncrementType.WORK, ":butterfly:", 1.2F, 1.1F),
                new GameUpgrade("Great Purple Hairsteak", "Upgrades seed gain by bump", IncrementType.BUMP, ":butterfly:", 1.2F, 1.1F),
                new GameUpgrade("Southern Dogface", "Upgrades seed gain by treasure", IncrementType.TREASURE, ":butterfly:", 1.2F, 1.1F)
        });
    }

    @Override
    public List<Command> getCommands() {

        final ArrayList<Command> commands = new ArrayList<>();
        commands.add(new ProfileCommand());
        commands.add(new WorkCommand(waiter));
        commands.add(new ShopCommand());
        return commands;
    }

    @Override
    public List<? extends EventListener> getListeners() {
        final ArrayList<EventListener> listeners = new ArrayList<>();
        listeners.add(new MessageReceivedListener());
        listeners.add(new VoiceJoinListener());
        listeners.add(new BumpListener());
        listeners.add(waiter);
        return listeners;
    }

    @Override
    public void onUnload() {

    }

    public static PluginDataStore getDataStore() {
        return store;
    }

    public static PluginConfig getConfig() {
        return config;
    }

}
