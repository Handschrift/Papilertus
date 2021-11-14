package com.openpackagedbot.init;

import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.commands.core.CommandClient;
import com.openpackagedbot.commands.core.CommandListener;
import com.openpackagedbot.commands.fun.TestCommand;
import com.openpackagedbot.config.Config;
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
        PluginLoader loader = new PluginLoader("plugins/");
        loader.load();

        Config config = Config.getConfig();
        BasicConfigurator.configure();

        CommandClient commandClient = new CommandClient();
       // commandClient.addCommands(new TestCommand());
        commandClient.addCommands(loader.getRegisteredCommands().toArray(new Command[0]));

        JDABuilder builder = JDABuilder.create(config.getToken(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        builder.addEventListeners(new CommandListener(commandClient));
        builder.setStatus(OnlineStatus.ONLINE);
        JDA jda = builder.build().awaitReady();

        for (Guild g : jda.getGuilds()) {
            g.updateCommands().addCommands(commandClient.getData()).queue();
        }
    }
}