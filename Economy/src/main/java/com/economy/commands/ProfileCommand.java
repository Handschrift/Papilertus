package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ProfileCommand extends Command {


    public ProfileCommand() {
        setName("profile");
        setDescription("gets profile data");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
        final EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(slashCommandInteractionEvent.getUser().getName(), null, slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                .setFooter(slashCommandInteractionEvent.getUser().getAsTag())
                .setThumbnail(slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl());
        builder.getDescriptionBuilder().append(Economy.getConfig().readString("collectable_name"))
                .append(": ").append(Economy.getConfig().readString("collectable_icon")).append(user.getCollectables()).append("\n")
                .append(Economy.getConfig().readString("currency_name"))
                .append(": ").append(Economy.getConfig().readString("currency_icon")).append(user.getCoins());
        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }
}
