package com.papilertus.init;

import java.util.ArrayList;
import java.util.Random;

public class Notification {
    private final ArrayList<String> messages = new ArrayList<>();


    public Notification() {
        final ArrayList<String> messages = Config.getConfig().getPossibleNotificationMessages();
        while (this.messages.size() < 3) {
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
            builder.append("•").append(message).append("\n");
        }
        return builder.toString();
    }
}
