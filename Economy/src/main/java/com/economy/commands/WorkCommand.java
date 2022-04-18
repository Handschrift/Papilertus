package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.init.Economy;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class WorkCommand extends Command {

    private final EventWaiter waiter;

    public WorkCommand(EventWaiter waiter) {
        setName("work");
        setDescription("Getting Plants!");
        setData(new CommandData(getName(), getDescription()));
        this.waiter = waiter;
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandEvent.getUser().getId(), slashCommandEvent.getGuild().getId());
        if (!user.canWork()) {
            /* final long future = Economy.getConfig().readInt("work_cooldown") - TimeUnit.MILLISECONDS.toMinutes(
                    user.getLastWorkCooldown()
            ); */
            //<t:future:R>
            slashCommandEvent.reply("Sorry, but you have to wait for one hour")
                    .setEphemeral(true).queue();
            return;
        }
        if (Economy.getConfig().readBoolean("enable_work_minigame")) {
            //Minigame
        } else {
            float coins = Economy.getConfig().readInt("base_work_gain") * GameUpgrade.getAggregatedUpgradeCoefficient(user, IncrementType.WORK);
            user.addCoins(coins);
            user.setLastWorkCooldown(System.currentTimeMillis());
            UserDatabase.updateUser(user);
            slashCommandEvent.reply("You are living a lot greener! You got " + coins + " " + Economy.getConfig().readString("collectable_name")).queue();
        }


    }
}
