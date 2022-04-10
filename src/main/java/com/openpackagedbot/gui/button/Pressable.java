package com.openpackagedbot.gui.button;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface Pressable {
    void onClick(ButtonClickEvent event);
}
