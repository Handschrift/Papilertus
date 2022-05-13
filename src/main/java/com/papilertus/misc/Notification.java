package com.papilertus.misc;

import com.papilertus.init.Config;

import java.util.ArrayList;
import java.util.Random;

public class Notification {
    private final ArrayList<String> messages = new ArrayList<>();


    public Notification(int amount) {
        final ArrayList<String> messages = Config.getConfig(new ConfigMiscTicket()).getPossibleNotificationMessages();
        while (this.messages.size() < amount) {
            final String random = messages.get(new Random().nextInt(messages.size()));
            if (this.messages.contains(random)) {
                continue;
            }
            this.messages.add(random);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (String message : this.messages) {
            builder.append("• ").append(message).append("\n");
        }
        return builder.toString();
    }
}
