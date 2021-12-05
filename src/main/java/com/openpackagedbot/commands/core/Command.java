package com.openpackagedbot.commands.core;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class Command {
    private String name;
    private String description;
    private boolean privateCommand = false;
    private boolean enabled = true;
    private String help;
    private CommandData data;

    protected abstract void execute(SlashCommandEvent event);

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getHelp() {
        return help;
    }

    protected void setHelp(String help) {
        this.help = help;
    }

    public CommandData getData() {
        return data;
    }

    protected void setData(CommandData data) {
        this.data = data;
    }

    public boolean isPrivateCommand() {
        return privateCommand;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void setPrivateCommand(boolean privateCommand) {
        this.privateCommand = privateCommand;
    }

    protected void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
