package com.funplugin.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MemeCommand extends Command {

    public MemeCommand() {
        setName("meme");
        setDescription("sends a meme");
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).followRedirects(HttpClient.Redirect.NORMAL).build();

        final HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create("https://reddit.com/r/memes/random.json")).build();

        final EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.RED);
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            final JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
            final String url = array.get(0).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonObject().get("url").getAsString();
            slashCommandInteractionEvent.replyEmbeds(builder.setImage(url).build()).queue();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
