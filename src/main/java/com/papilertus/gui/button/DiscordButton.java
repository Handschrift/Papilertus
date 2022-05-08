package com.papilertus.gui.button;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.UUID;

public class DiscordButton {

    private final String id;
    private String userId = "";
    private final Pressable onClick;
    private Button button;

    public DiscordButton(Pressable onClick, ButtonStyle style, String label) {

        this.id = UUID.randomUUID().toString();
        this.onClick = onClick;
        this.button = Button.of(style, id, label);
    }

    public DiscordButton(String userId, Pressable onClick, ButtonStyle style, String label) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.onClick = onClick;
        this.button = Button.of(style, id, label);
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Pressable getOnClick() {
        return onClick;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
