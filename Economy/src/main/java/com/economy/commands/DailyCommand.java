package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.init.Economy;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.concurrent.TimeUnit;

public class DailyCommand extends Command {

    public DailyCommand() {
        setName("daily");
        setDescription("Get your daily reward");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());

        if (!user.canGetDaily()) {
            final long future = TimeUnit.MILLISECONDS.toSeconds(TimeUnit.HOURS.toMillis(24) + user.getLastDaily());
            slashCommandInteractionEvent.reply("Sorry, but you have to wait for <t:" + future + ":R>")
                    .setEphemeral(true).queue();
            return;
        }

        final float seeds = GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseDailyGain(), user, IncrementType.DAILY);
        user.addCollectables(seeds);
        user.setLastDaily(System.currentTimeMillis());
        UserDatabase.updateUser(user);
        slashCommandInteractionEvent.reply("You got " + seeds + " " + Economy.getEconomyConfig().getCollectableName() + " as a daily reward, come back next day!").queue();

    }
}
