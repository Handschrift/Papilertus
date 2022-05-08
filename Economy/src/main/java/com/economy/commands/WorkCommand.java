package com.economy.commands;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.game.element.AnswerButton;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.init.Economy;
import com.economy.minigames.Quiz;
import com.papilertus.commands.core.Command;
import com.papilertus.gui.button.DiscordButton;
import com.papilertus.gui.generator.PapilertusMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WorkCommand extends Command {


    public WorkCommand() {
        setName("work");
        setDescription("Getting Plants!");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final EconomyUser user = UserDatabase.fetch(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
        if (!user.canWork()) {
            final long future = TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MINUTES.toMillis(Economy.getEconomyConfig().getWorkCooldown()) + user.getLastWorkCooldown());
            slashCommandInteractionEvent.reply("Sorry, but you have to wait for <t:" + future + ":R>")
                    .setEphemeral(true).queue();
            return;
        }
        if (Economy.getEconomyConfig().isEnableWorkMinigame()) {
            try {
                final Quiz quiz = new Quiz();
                final EmbedBuilder questionBuilder = new EmbedBuilder();
                questionBuilder.setDescription("Category: **" + quiz.getCategory() + "**\n\n`" + quiz.getQuestion() + "`")
                        .setTitle("In order to get some Plants you have to say if the statement is true or false")
                        .setColor(Color.CYAN)
                        .setAuthor(slashCommandInteractionEvent.getUser().getName(), null, slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                        .setFooter("This question is provided by the Open Trivia Database", "https://opentdb.com/images/logo.png");

                final PapilertusMessageBuilder messageBuilder = new PapilertusMessageBuilder();
                messageBuilder.setEmbeds(questionBuilder.build());
                messageBuilder
                        .addButtons(new DiscordButton(slashCommandInteractionEvent.getUser().getId(), new AnswerButton(true, quiz.getCorrectAnswer()), ButtonStyle.PRIMARY, "true"))
                        .addButtons(new DiscordButton(slashCommandInteractionEvent.getUser().getId(), new AnswerButton(false, quiz.getCorrectAnswer()), ButtonStyle.PRIMARY, "false"));

                slashCommandInteractionEvent.reply(messageBuilder.build()).queue();
                user.setLastWorkCooldown(System.currentTimeMillis());
                UserDatabase.updateUser(user);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            float coins = GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseWorkGain(), user, IncrementType.WORK);
            user.addCoins(coins);
            user.setLastWorkCooldown(System.currentTimeMillis());
            UserDatabase.updateUser(user);
            slashCommandInteractionEvent.reply("You are living a lot greener! You got " + coins + " " + Economy.getEconomyConfig().getCurrencyName()).queue();
        }


    }
}
