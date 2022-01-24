package com.papilertus.birthdays.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import org.jetbrains.annotations.NotNull;

public class BirthdayEvent extends Event {
    public BirthdayEvent(@NotNull JDA api, long responseNumber) {
        super(api, responseNumber);
    }

    public BirthdayEvent(@NotNull JDA api) {
        super(api);
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return super.getJDA();
    }

    @Override
    public long getResponseNumber() {
        return super.getResponseNumber();
    }
}
