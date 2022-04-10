package com.openpackagedbot.gui.button;

import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.interactions.ButtonImpl;

import java.util.UUID;

public class DiscordButton {

    private final String id;
    private String userId = "";
    private final Runnable onClick;
    private Button button;

    public DiscordButton(Runnable onClick, Button button) {

        this.id = UUID.randomUUID().toString();
        this.onClick = onClick;
        this.button = button;
    }

    public DiscordButton(String userId, Runnable onClick, Button button) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.onClick = onClick;
        this.button = Button.of(ButtonStyle.PRIMARY, id, "bla");
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
