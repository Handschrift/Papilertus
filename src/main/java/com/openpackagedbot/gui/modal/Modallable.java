package com.openpackagedbot.gui.modal;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface Modallable {
    void onModalInteract(ModalInteractionEvent event);
}
