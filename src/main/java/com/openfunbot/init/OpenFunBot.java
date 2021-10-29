package com.openfunbot.init;

import com.openfunbot.commands.core.CommandClient;
import com.openfunbot.commands.core.CommandListener;
import com.openfunbot.commands.fun.TestCommand;
import com.openfunbot.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class OpenFunBot {
    public static void main(String[] args) throws LoginException, InterruptedException {
        Config config = Config.getConfig();
        CommandClient commandClient = new CommandClient();
        commandClient.addCommands(new TestCommand());
        JDABuilder builder = JDABuilder.create(config.getToken(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        builder.addEventListeners(new CommandListener(commandClient));
        JDA jda = builder.build().awaitReady();

        for (Guild g : jda.getGuilds()) {
            g.updateCommands().addCommands(commandClient.getData()).queue();
        }
    }
}
