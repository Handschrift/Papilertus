package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.init.Economy;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ProfileCommand extends Command {


    public ProfileCommand() {
        setName("profile");
        setDescription("gets profile data");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
        final EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(slashCommandInteractionEvent.getUser().getName(), null, slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                .setFooter(slashCommandInteractionEvent.getUser().getAsTag())
                .setThumbnail(slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl());
        final String collectableName = Economy.getEconomyConfig().getCollectableName();
        final String currencyName = Economy.getEconomyConfig().getCurrencyName();

        builder.getDescriptionBuilder().append(Economy.getEconomyConfig().getCollectableName())
                .append(": ").append(Economy.getEconomyConfig().getCollectableIcon()).append(user.getCollectables()).append("\n")
                .append(Economy.getEconomyConfig().getCurrencyName())
                .append(": ").append(Economy.getEconomyConfig().getCurrencyIcon()).append(user.getCoins()).append("\n")
                .append("Weekly: ").append(user.getWeeklyCurrency()).append("\n\n")

                .append("Your stats:").append("\n")
                .append(collectableName).append(" per bump: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCollectablesOnBumpGain(), user, IncrementType.BUMP)).append("\n")
                .append(collectableName).append(" per minute in VoiceChat: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCoinOnVoiceActivityAmount(), user, IncrementType.VOICE)).append("\n")
                .append(collectableName).append(" per message: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCoinOnMessageAmount(), user, IncrementType.MESSAGE)).append("\n")
                .append(collectableName).append(" per daily: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseDailyGain(), user, IncrementType.DAILY)).append("\n")
                .append(currencyName).append(" per work: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseWorkGain(), user, IncrementType.WORK));
        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }
}
