package com.papilertus.gui.generator;

import com.papilertus.gui.button.ButtonRegistry;
import com.papilertus.gui.button.DiscordButton;
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
import java.util.stream.IntStream;

public class PapilertusMessageBuilder {
    private final ArrayList<DiscordButton> buttons = new ArrayList<>();
    private final MessageBuilder builder = new MessageBuilder();
    private final int MAX_PAGES = 5;

    public PapilertusMessageBuilder addButtons(DiscordButton... buttons) {
        this.buttons.addAll(List.of(buttons));
        if (this.buttons.size() <= MAX_PAGES) {
            builder.setActionRows(ActionRow.of(this.buttons.stream().map(DiscordButton::getButton).collect(Collectors.toList())));
            return this;
        }
        final ArrayList<ActionRow> actionRows = new ArrayList<>();
        //Using an intstream to get a "pagination" of the discord buttons
        //Stackoverflow link: https://stackoverflow.com/questions/43057690/java-stream-collect-every-n-elements
        IntStream.range(0, (this.buttons.size() + MAX_PAGES - 1) / MAX_PAGES).mapToObj(i -> this.buttons.subList(i * MAX_PAGES, Math.min(MAX_PAGES * (i + 1), this.buttons.size()))).forEach(
                discordButtons -> {
                    actionRows.add(ActionRow.of(discordButtons.stream().map(DiscordButton::getButton).collect(Collectors.toList())));
                }
        );
        builder.setActionRows(actionRows);

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

            //quite unsure if this is safe
            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
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
