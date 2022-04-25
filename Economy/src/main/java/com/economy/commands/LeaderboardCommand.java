package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;

public class LeaderboardCommand extends Command {

    public LeaderboardCommand() {
        setName("leaderboard");
        setDescription("Shows the server leaderboard");
        setData(new CommandData(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        final EmbedBuilder builder = new EmbedBuilder()
                .setTitle("The current top 10 of the server:");
        final ArrayList<EconomyUser> users = UserDatabase.getUsers(slashCommandEvent.getGuild().getId());
        int i = 1;
        for (EconomyUser user : users) {
            builder.addField(i + ") " + slashCommandEvent.getGuild().getMemberById(user.getUserId()).getEffectiveName(),
                    user.getCoins() + " " + Economy.getConfig().readString("currency_name"), false);
            i++;
        }
        slashCommandEvent.replyEmbeds(builder.build()).queue();
    }
}
