package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.plugin.PluginDataStore;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class ProfileCommand extends Command {

    private final PluginDataStore store;

    public ProfileCommand(PluginDataStore store) {
        this.store = store;
        setName("profile");
        setDescription("gets profile data");
        setData(new CommandData(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        final UserDatabase userDatabase = new UserDatabase(store);
        final EconomyUser user = userDatabase.fetchUser(slashCommandEvent.getUser().getId(), slashCommandEvent.getGuild().getId());
        slashCommandEvent.reply(Double.toString(user.getCoins())).queue();
    }
}
