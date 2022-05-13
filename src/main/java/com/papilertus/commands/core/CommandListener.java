package com.papilertus.commands.core;

import com.papilertus.misc.Notification;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public final class CommandListener extends ListenerAdapter {

    private final CommandClient commandClient;

    public CommandListener(CommandClient commandClient) {
        this.commandClient = commandClient;
    }


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getChannel().getType() == ChannelType.PRIVATE) {
            if (commandClient.getCommand(event.getName()).isPrivateCommand()) {
                commandClient.getCommand(event.getName()).execute(event);
            } else {
                event.reply("This command is only usable on servers!").setEphemeral(true).queue();
            }
        } else {
            commandClient.getCommand(event.getName()).execute(event);
        }
        sendNotification(event.getChannel());
    }

    private void sendNotification(MessageChannel channel) {
        if (Math.random() < 0.7) {
            final EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.CYAN);
            builder.setDescription("Did you know: \n" + new Notification(3).toString());
            builder.setFooter("For testing purposes this message will appear more often");
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

}
