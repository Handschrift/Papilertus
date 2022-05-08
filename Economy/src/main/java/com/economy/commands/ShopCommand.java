package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ShopCommand extends Command {

    public ShopCommand() {
        setName("shop");
        setDescription("Shows the shop");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
        slashCommandInteractionEvent.reply(user.getShopMessageBuilder().build()).queue();

    }
}
