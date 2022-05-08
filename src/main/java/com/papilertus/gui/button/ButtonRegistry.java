package com.papilertus.gui.button;

import java.util.HashMap;

public class ButtonRegistry {
    private static final HashMap<String, DiscordButton> buttons = new HashMap<>();

    public static void registerButton(String id, DiscordButton button) {
        buttons.put(id, button);
    }

    public static void unregisterButton(String id) {
        buttons.remove(id);
    }

    public static DiscordButton getButtonById(String id) {
        return buttons.get(id);
    }
}
