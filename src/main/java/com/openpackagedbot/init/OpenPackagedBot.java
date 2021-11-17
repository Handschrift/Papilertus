package com.openpackagedbot.init;

import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.commands.core.CommandClient;
import com.openpackagedbot.commands.core.CommandListener;
import com.openpackagedbot.config.Config;
import com.openpackagedbot.listeners.BotJoinListener;
import com.openpackagedbot.plugin.PluginLoader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.log4j.BasicConfigurator;

import javax.security.auth.login.LoginException;

public class OpenPackagedBot {
    public static void main(String[] args) throws LoginException, InterruptedException {
        final Config config = Config.getConfig();
        final PluginLoader loader = new PluginLoader(config.getPluginDir());
        loader.load();

        BasicConfigurator.configure();
        final CommandClient commandClient = new CommandClient();
        commandClient.addCommands(loader.getRegisteredCommands().toArray(new Command[0]));

        final JDABuilder builder = JDABuilder.create(config.getToken(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));

        //General listener for the commands
        builder.addEventListeners(new CommandListener(commandClient));

        builder.addEventListeners(new BotJoinListener(commandClient));

        builder.setStatus(OnlineStatus.ONLINE);

        final JDA jda = builder.build().awaitReady();

        //register commands on all guilds
        for (Guild g : jda.getGuilds()) {
            g.updateCommands().addCommands(commandClient.getData()).queue();
        }
    }
}