package com.openpackagedbot.commands.moderation;

import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class BanCommand extends Command {

    public BanCommand() {
        setName("ban");
        setDescription("Bans a user");
        setData(new CommandData(getName(), getDescription()).addOption(OptionType.USER, "user", "user to ban"));
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        Member banMember = event.getOption("user").getAsMember();
        if (banMember != null) {
            banMember.ban(7).queue();
            event.reply("The user " + banMember.getAsMention() + " was banned!").setEphemeral(true).queue();
        }
    }
}
