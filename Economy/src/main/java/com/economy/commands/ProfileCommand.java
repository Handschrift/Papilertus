package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class ProfileCommand extends Command {


    public ProfileCommand() {
        setName("profile");
        setDescription("gets profile data");
        setData(new CommandData(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandEvent.getUser().getId(), slashCommandEvent.getGuild().getId());
        final EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(slashCommandEvent.getUser().getName(), null, slashCommandEvent.getUser().getEffectiveAvatarUrl())
                .setFooter(slashCommandEvent.getUser().getAsTag())
                .setThumbnail(slashCommandEvent.getUser().getEffectiveAvatarUrl());
        builder.getDescriptionBuilder().append(Economy.getConfig().readString("collectable_name"))
                .append(": ").append(Economy.getConfig().readString("collectable_icon")).append(user.getCollectables()).append("\n")
                .append(Economy.getConfig().readString("currency_name"))
                .append(": ").append(Economy.getConfig().readString("currency_icon")).append(user.getCoins());
        slashCommandEvent.replyEmbeds(builder.build()).queue();
    }
}
