package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.economy.util.MathUtils;
import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SellCommand extends Command {

    public SellCommand() {
        setName(Economy.getConfig().readString("convert_command_name"));
        setDescription("Sell or convert your collectables to currency");
        setData(new CommandData(getName(), getDescription())
                .addOption(OptionType.NUMBER, "amount", "amount", false));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandEvent.getUser().getId(), slashCommandEvent.getGuild().getId());
        final double collectables = slashCommandEvent.getOption("amount") == null ? user.getCollectables() : slashCommandEvent.getOption("amount").getAsDouble();
        final double coins = MathUtils.round(collectables * Economy.getConfig().readInt("collectable_to_currency_conversion"));
        user.removeCollectables(collectables);
        user.addCoins(coins);
        UserDatabase.updateUser(user);
        slashCommandEvent.reply("You gave " + collectables + " " + Economy.getConfig().readString("collectable_name")
                + " for " + coins + " " + Economy.getConfig().readString("currency_name")).setEphemeral(true).queue();
    }
}
