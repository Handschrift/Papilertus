package com.papilertus.commands.core;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class Command {
    private String name;
    private String description;
    private boolean privateCommand = false;
    private boolean enabled = true;
    private String help;
    private SlashCommandData data;

    protected abstract void execute(SlashCommandInteractionEvent event);

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

    protected void setData(SlashCommandData data) {
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
