package com.papilertus.gui.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonListener extends ListenerAdapter {


    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        final DiscordButton button = ButtonRegistry.getButtonById(event.getComponentId());
        if (button == null) {
            event.reply("This button expired! Please execute the command again.").setEphemeral(true).queue();
            return;
        }
        if (!button.getUserId().isEmpty() && !event.getUser().getId().equals(button.getUserId())) {
            event.reply("You are not authorized to press this button").setEphemeral(true).queue();
            return;
        }

        button.getOnClick().onClick(event);
    }

}
