package com.papilertus.gui.modal;

import java.util.HashMap;

public class ModalRegistry {
    private static final HashMap<String, DiscordModal> modals = new HashMap<>();

    public static void registerModal(String id, DiscordModal button) {
        modals.put(id, button);
    }

    public static void unregisterModal(String id) {
        modals.remove(id);
    }

    public static DiscordModal getModalById(String id) {
        return modals.get(id);
    }
}
