package com.openpackagedbot.gui.button;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        final DiscordButton button = ButtonRegistry.getButtonById(event.getComponentId());
        if (!button.getId().isEmpty() && !event.getUser().getId().equals(button.getUserId())) {
            event.reply("You are not authorized to press this button").setEphemeral(true).queue();
            return;
        }

        button.getOnClick().onClick(event);


    }


}
