package com.economy.commands;

import com.economy.game.element.Forecast;
import com.economy.init.Economy;
import com.papilertus.commands.core.Command;
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
        final Forecast forecast = Forecast.getForecast();
        int i = 0;
        for (Forecast.ForecastEntry entry : forecast.getData()) {
            builder.addField(LocalDate.now().plusDays(i) + " " + entry.getName(), "1 " + Economy.getEconomyConfig().getCollectableName() + " = " + entry.getValue() + " " + Economy.getEconomyConfig().getCurrencyName(), false);
            i++;
        }
        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }
}
