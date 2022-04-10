package com.openpackagedbot.gui.generator;

import com.openpackagedbot.gui.button.ButtonRegistry;
import com.openpackagedbot.gui.button.DiscordButton;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PapilertusMessageBuilder {
    final ArrayList<DiscordButton> buttons = new ArrayList<>();
    final MessageBuilder builder = new MessageBuilder();

    public PapilertusMessageBuilder addButtons(DiscordButton... buttons) {
        this.buttons.addAll(List.of(buttons));
        builder.setActionRows(ActionRow.of(this.buttons.stream().map(DiscordButton::getButton).collect(Collectors.toList())));
        return this;
    }

    public PapilertusMessageBuilder setContent(String content) {
        builder.setContent(content);
        return this;
    }


    public PapilertusMessageBuilder setEmbeds(MessageEmbed... embeds) {
        builder.setEmbeds(embeds);
        return this;
    }

    public Message build() {
        for (DiscordButton button : buttons) {
            ButtonRegistry.registerButton(button.getId(), button);

            //quite unsure if this is save
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    ButtonRegistry.unregisterButton(button.getId());
                    executorService.shutdown();
                }
            }, 30, TimeUnit.MINUTES);
        }
        return builder.build();
    }
}
