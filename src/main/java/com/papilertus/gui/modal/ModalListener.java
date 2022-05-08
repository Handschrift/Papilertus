package com.papilertus.gui.modal;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ModalListener extends ListenerAdapter {
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        final DiscordModal modal = ModalRegistry.getModalById(event.getModalId());
        if (modal == null) {
            event.reply("This modal expired! Please execute the command again.").setEphemeral(true).queue();
            return;
        }
        if (!modal.getUserId().isEmpty() && !event.getUser().getId().equals(modal.getUserId())) {
            event.reply("You are not authorized to press this button").setEphemeral(true).queue();
            return;
        }

        modal.getModallable().onModalInteract(event);
    }
}
