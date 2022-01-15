package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.openpackagedbot.commands.core.Command;
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
        final UserDatabase userDatabase = new UserDatabase();
        final EconomyUser user = userDatabase.fetchUser(slashCommandEvent.getUser().getId(), slashCommandEvent.getGuild().getId());
        user.addCoins(100);
        System.out.println(user.getCoins());
        slashCommandEvent.reply(Double.toString(user.getCoins())).queue();
    }
}
