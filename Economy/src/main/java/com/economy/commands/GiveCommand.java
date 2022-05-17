package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.init.Economy;
import com.economy.util.MathUtils;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.text.MessageFormat;

public class GiveCommand extends Command {

    public GiveCommand() {
        setName("give");
        setDescription("gives a specific amount of currency");
        setData(Commands.slash(getName(), getDescription())
                .addOption(OptionType.USER, "user", "receiving user", true)
                .addOption(OptionType.NUMBER, "amount", "amount of currency", true));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final User receiverUser = slashCommandInteractionEvent.getOption("user").getAsUser();
        final float amount = (float) MathUtils.round(slashCommandInteractionEvent.getOption("amount").getAsDouble());

        final EconomyUser sender = UserDatabase.fetch(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
        final EconomyUser receiver = UserDatabase.fetch(receiverUser.getId(), slashCommandInteractionEvent.getGuild().getId());

        if (receiverUser.isBot() || receiverUser.isSystem()) {
            slashCommandInteractionEvent.reply(MessageFormat.format("You cannot give {0} to bots!", Economy.getEconomyConfig().getCurrencyName())).setEphemeral(true).queue();
            return;
        }

        if (sender.getUserId().equals(receiver.getUserId())) {
            slashCommandInteractionEvent.reply("You cannot send to yourself!").setEphemeral(true).queue();
            return;
        }

        if (amount > sender.getCoins()) {
            slashCommandInteractionEvent.reply(MessageFormat.format("You don't have enough {0}!", Economy.getEconomyConfig().getCurrencyName())).setEphemeral(true).queue();
            return;
        }

        if (!sender.canSend(amount)) {
            slashCommandInteractionEvent.reply("You already sent too much today!").setEphemeral(true).queue();
            return;
        }

        if (!receiver.canReceive(amount)) {
            slashCommandInteractionEvent.reply("This user already received too much today!").setEphemeral(true).queue();
            return;
        }

        sender.giveToUser(receiver, amount);

        UserDatabase.updateUser(sender);
        UserDatabase.updateUser(receiver);
        slashCommandInteractionEvent.reply(MessageFormat.format("You gave {0} {1} to {2}", amount, Economy.getEconomyConfig().getCurrencyName(), receiverUser.getAsMention())).queue();

    }
}
