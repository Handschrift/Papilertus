package com.economy.commands;

import com.economy.game.element.Forecast;
import com.economy.init.Economy;
import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.LocalDate;

public class ForecastCommand extends Command {

    public ForecastCommand() {
        setName("forecast");
        setDescription("shows the forecast for the next days");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final EmbedBuilder builder = new EmbedBuilder();
        final Forecast forecast = new Forecast(7);
        int i = 0;
        for (double value : forecast.getData()) {
            builder.addField(LocalDate.now().plusDays(i).toString(), "1 " + Economy.getConfig().readString("collectable_name") + " = " + value + " " + Economy.getConfig().readString("currency_name"), false);
            i++;
        }
        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }
}
