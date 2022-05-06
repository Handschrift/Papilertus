package com.economy.minigames;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class Quiz {

    private final boolean correctAnswer;
    private final String question;

    private final String category;

    public Quiz() throws IOException, InterruptedException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

        final HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create("https://opentdb.com/api.php?amount=1&type=boolean&encode=base64")).build();

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        final JsonObject questionObject = JsonParser.parseString(response.body()).getAsJsonObject().getAsJsonArray("results").get(0).getAsJsonObject();

        correctAnswer = Boolean.parseBoolean(new String(decoder.decode(questionObject.get("correct_answer").getAsString())).toLowerCase());

        question = new String(decoder.decode(questionObject.get("question").getAsString()));

        category = new String(decoder.decode(questionObject.get("category").getAsString()));


    }

    public boolean getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCategory() {
        return category;
    }
}
