package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.init.Economy;
import com.openpackagedbot.commands.core.Command;
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
        final String collectableName = Economy.getConfig().readString("collectable_name");
        final String currencyName = Economy.getConfig().readString("currency_name");

        builder.getDescriptionBuilder().append(Economy.getConfig().readString("collectable_name"))
                .append(": ").append(Economy.getConfig().readString("collectable_icon")).append(user.getCollectables()).append("\n")
                .append(Economy.getConfig().readString("currency_name"))
                .append(": ").append(Economy.getConfig().readString("currency_icon")).append(user.getCoins())

                .append("Your stats:").append("\n")
                .append(collectableName).append(" per bump: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_collectables_on_bump_gain"), user, IncrementType.BUMP)).append("\n")
                .append(collectableName).append(" per minute in VoiceChat: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_coin_on_voice_activity_amount"), user, IncrementType.VOICE)).append("\n")
                .append(collectableName).append(" per message: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_coin_on_message_amount"), user, IncrementType.MESSAGE)).append("\n")
                .append(collectableName).append(" per daily: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_daily_gain"), user, IncrementType.DAILY)).append("\n")
                .append(currencyName).append(" per work: ").append(GameUpgrade.getAggregatedUpgradeValue(Economy.getConfig().readInt("base_work_gain"), user, IncrementType.WORK));
        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }
}
