package com.github.bestfriendplugin.commands;

import com.openpackagedbot.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public class ZodiacCommand extends Command {

    public ZodiacCommand() {
        setName("horoskop");
        setDescription("Zeigt dir dein Horoskop an");
        setData(Commands.slash(getName(), getDescription()).addOptions(new OptionData(OptionType.STRING, "sternzeichen", "Sternzeichen", true)
                .addChoice("Widder", "widder")
                .addChoice("Stier", "stier")
                .addChoice("Zwillinge", "zwillinge")
                .addChoice("Krebs", "krebs")
                .addChoice("Löwe", "löwe")
                .addChoice("Jungfrau", "jungfrau")
                .addChoice("Waage", "waage")
                .addChoice("Skorpion", "skorpion")
                .addChoice("Schütze", "schütze")
                .addChoice("Steinbock", "steinbock")
                .addChoice("Wassermann", "wassermann")
                .addChoice("Fische", "fische")));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        try {
            String zodiac = slashCommandInteractionEvent.getOption("sternzeichen").getAsString();

            Document document = Jsoup.connect("https://astrowoche.wunderweib.de/tageshoroskop/heute/" + zodiac).get();

            List<Element> elementList = document.body().getElementsByClass("typo--editor");

            List<Element> elements2 = elementList.get(0).getAllElements();

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(zodiac);
            builder.setAuthor(slashCommandInteractionEvent.getMember().getUser().getName(), null, slashCommandInteractionEvent.getMember().getEffectiveAvatarUrl());

            StringBuilder stringBuilder = builder.getDescriptionBuilder();

            for (Element e : elements2) {
                if ((e.tagName().equals("p") || e.tagName().equals("h3")) && !e.text().startsWith(">")) {
                    stringBuilder.append(e.text()).append("\n");
                }
            }

            slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
        } catch (IOException e) {
            slashCommandInteractionEvent.reply("Es ist ein Fehler aufgetreten!").setEphemeral(true).queue();
        }
    }
}
