package com.github.bestfriendplugin.commands;

import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PollCommand extends Command {
    public PollCommand() {
        setName("poll");
        setDescription("Erstellt eine Umfrage");
        setData(Commands.slash(getName(), getDescription()).addOptions(

                new OptionData(OptionType.STRING, "question", "Die Frage", true),
                new OptionData(OptionType.STRING, "answer0", "Antwort", true),
                new OptionData(OptionType.STRING, "answer1", "Antwort", true),
                new OptionData(OptionType.STRING, "answer2", "Antwort", false),
                new OptionData(OptionType.STRING, "answer3", "Antwort", false),
                new OptionData(OptionType.STRING, "answer4", "Antwort", false),
                new OptionData(OptionType.STRING, "answer5", "Antwort", false),
                new OptionData(OptionType.STRING, "answer6", "Antwort", false),
                new OptionData(OptionType.STRING, "answer7", "Antwort", false),
                new OptionData(OptionType.STRING, "answer8", "Antwort", false),
                new OptionData(OptionType.STRING, "answer9", "Antwort", false)

        ));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final String[] numberToEmote = {
                "\u0031\u20E3",
                "\u0032\u20E3",
                "\u0033\u20E3",
                "\u0034\u20E3",
                "\u0035\u20E3",
                "\u0036\u20E3",
                "\u0037\u20E3",
                "\u0038\u20E3",
                "\u0039\u20E3",
                "\uD83D\uDD1F"
        };
        String question = slashCommandInteractionEvent.getOption("question").getAsString();
        EmbedBuilder builder = new EmbedBuilder();
        StringBuilder stringBuilder = builder.getDescriptionBuilder();
        builder.setTitle(question);
        for (int i = 0; i < 10; i++) {
            if (slashCommandInteractionEvent.getOption("answer" + i) != null) {
                stringBuilder.append(numberToEmote[i]).append(slashCommandInteractionEvent.getOption("answer" + i).getAsString()).append("\n");
            }
        }
        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue(interactionHook -> {
            interactionHook.retrieveOriginal().queue(message -> {
                for (int i = 0; i < 10; i++) {
                    if (slashCommandInteractionEvent.getOption("answer" + i) != null) {
                        message.addReaction(numberToEmote[i]).queue();
                    }
                }
            });
        });
    }
}
