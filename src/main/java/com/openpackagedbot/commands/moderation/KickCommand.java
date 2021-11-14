package com.openpackagedbot.commands.moderation;

import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class KickCommand extends Command {

    public KickCommand() {
        setName("kick");
        setDescription("Kicks a user");
        setData(new CommandData(getName(), getDescription()).addOption(OptionType.USER, "user", "user to kick"));
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        Member kickedMember = event.getOption("user").getAsMember();
        event.getGuild().kick(kickedMember).queue();
        event.reply("The user " + kickedMember.getAsMention() + " was kicked!").setEphemeral(true).queue();
    }
}
