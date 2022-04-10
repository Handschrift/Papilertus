package com.openpackagedbot.gui.button;

import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.util.UUID;

public class DiscordButton {

    private final String id;
    private String userId = "";
    private final Runnable onClick;
    private Button button;

    public DiscordButton(Runnable onClick, ButtonStyle style, String label) {

        this.id = UUID.randomUUID().toString();
        this.onClick = onClick;
        this.button = Button.of(style, id, label);
    }

    public DiscordButton(String userId, Runnable onClick, ButtonStyle style, String label) {
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

    public Runnable getOnClick() {
        return onClick;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
