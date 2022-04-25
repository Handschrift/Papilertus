package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.economy.util.MathUtils;
import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class GiveCommand extends Command {

    public GiveCommand() {
        setName("give");
        setDescription("gives a specific amount of currency");
        setData(new CommandData(getName(), getDescription())
                .addOption(OptionType.USER, "user", "receiving user", true)
                .addOption(OptionType.NUMBER, "amount", "amount of currency", true));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        final User receiverUser = slashCommandEvent.getOption("user").getAsUser();
        final float amount = (float) MathUtils.round(slashCommandEvent.getOption("amount").getAsDouble());

        final EconomyUser sender = UserDatabase.fetch(slashCommandEvent.getUser().getId(), slashCommandEvent.getGuild().getId());
        final EconomyUser receiver = UserDatabase.fetch(receiverUser.getId(), slashCommandEvent.getGuild().getId());

        if (amount > sender.getCoins()) {
            slashCommandEvent.reply("You don't have enough money!").setEphemeral(true).queue();
            return;
        }

        if (!sender.canSend(amount)) {
            slashCommandEvent.reply("You already sent too much today!").setEphemeral(true).queue();
            return;
        }

        if (!receiver.canReceive(amount)) {
            slashCommandEvent.reply("This user already received too much today!").setEphemeral(true).queue();
            return;
        }

        sender.giveToUser(receiver, amount);

        slashCommandEvent.reply("You gave " + amount + " " + Economy.getConfig().readString("currency_name")).queue();
    }
}
