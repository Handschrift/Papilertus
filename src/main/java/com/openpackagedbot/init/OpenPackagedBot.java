package com.openpackagedbot.init;

import com.openpackagedbot.commands.base.PluginCommand;
import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.commands.core.CommandClient;
import com.openpackagedbot.commands.core.CommandListener;
import com.openpackagedbot.listeners.BotJoinListener;
import com.openpackagedbot.plugin.PluginLoader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public final class OpenPackagedBot {
    public static void main(String[] args) throws LoginException, InterruptedException {
        final Config config = Config.getConfig();
        final PluginLoader loader = new PluginLoader(config.getPluginDir());
        loader.load();

        final CommandClient commandClient = new CommandClient();

        //adding plugin commands
        commandClient.addCommands(loader.getRegisteredCommands().toArray(new Command[0]));

        //adding inbuild commands
        commandClient.addCommands(new PluginCommand(loader.getLoadedPlugins()));

        final JDABuilder builder = JDABuilder.create(config.getToken(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));

        //General listener for the commands
        builder.addEventListeners(new CommandListener(commandClient));

        builder.addEventListeners(new BotJoinListener(commandClient));

        //Add Plugin event listeners
        if (loader.getRegisteredEventListeners().size() > 0) {
            for (Object listener : loader.getRegisteredEventListeners()) {
                builder.addEventListeners(listener);
            }
        }

        builder.setStatus(OnlineStatus.ONLINE);

        builder.disableCache(config.getCacheFlags());

        final JDA jda = builder.build().awaitReady();

        //register commands on all guilds
        for (Guild g : jda.getGuilds()) {
            g.updateCommands().addCommands(commandClient.getData()).queue();
        }
    }
}