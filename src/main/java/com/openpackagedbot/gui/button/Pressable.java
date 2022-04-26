package com.openpackagedbot.gui.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface Pressable {
    void onClick(ButtonInteractionEvent event);
}
