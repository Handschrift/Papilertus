package com.papilertus.init;

import com.papilertus.commands.base.FeedbackCommand;
import com.papilertus.commands.core.Command;
import com.papilertus.commands.core.CommandClient;
import com.papilertus.commands.core.CommandListener;
import com.papilertus.gui.button.ButtonListener;
import com.papilertus.gui.modal.ModalListener;
import com.papilertus.listeners.BotJoinListener;
import com.papilertus.plugin.PluginJDA;
import com.papilertus.plugin.PluginLoader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.internal.requests.Requester;

import javax.security.auth.login.LoginException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Papilertus {
    private static JDA jda;
    private static PluginJDA pluginJDA;

    public static void main(String[] args) throws LoginException, InterruptedException {
        final Config config = Config.getConfig();
        final PluginLoader loader = new PluginLoader(config.getPluginDir());
        loader.load();

        final CommandClient commandClient = new CommandClient();

        //adding plugin commands
        commandClient.addCommands(loader.getRegisteredCommands().toArray(new Command[0]));

        //adding inbuild commands
        commandClient.addCommands(new FeedbackCommand(Config.getConfig().getFeedbackRecipientId()));

        final JDABuilder builder = JDABuilder.create(config.getToken(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));

        //General listener for the commands
        builder.addEventListeners(new CommandListener(commandClient));

        //General listener for the buttons and modals
        builder.addEventListeners(new ButtonListener());
        builder.addEventListeners(new ModalListener());

        builder.addEventListeners(new BotJoinListener(commandClient));

        //Add Plugin event listeners
        if (loader.getRegisteredEventListeners().size() > 0) {
            for (Object listener : loader.getRegisteredEventListeners()) {
                builder.addEventListeners(listener);
            }
        }

        builder.setStatus(OnlineStatus.ONLINE);

        builder.disableCache(config.getCacheFlags());

        jda = builder.build().awaitReady();

        //Setting fosscord as the api endpoint to provide fosscord compatibility
        if (config.isFosscordMode()) {
            try {
                Field field = Requester.class.getDeclaredField("DISCORD_API_PREFIX");
                field.setAccessible(true);

                Field modifiers = Field.class.getDeclaredField("modifiers");
                modifiers.setAccessible(true);
                modifiers.set(field, field.getModifiers() & ~Modifier.FINAL);
                field.set(null, "https://api.fosscord.com");

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        //register commands on all guilds
        final CommandListUpdateAction action = jda.updateCommands();
        action.addCommands(commandClient.getData()).queue();
    }

    public static PluginJDA getPluginJDA() {
        if (jda == null)
            return null;
        if (pluginJDA == null) {
            pluginJDA = new PluginJDA(jda);
        }

        return pluginJDA;
    }
}