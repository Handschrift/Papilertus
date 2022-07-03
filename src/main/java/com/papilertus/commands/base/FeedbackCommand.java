package com.papilertus.commands.base;

import com.papilertus.commands.core.Command;
import com.papilertus.gui.modal.DiscordModal;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class FeedbackCommand extends Command {

    private final String recId;

    public FeedbackCommand(String recId) {
        setName("feedback");
        setDescription("sends feedback to the author");
        setData(Commands.slash(getName(), getDescription()));
        this.recId = recId;
    }

    @Override
    protected void execute(SlashCommandInteractionEvent event) {

        if (recId.isBlank() || recId.isEmpty()) {
            event.reply("There was no recipient set in the configuration! Please contact a server administrator.").setEphemeral(true).queue();
        }

        final TextInput email = TextInput.create("title", "Title", TextInputStyle.SHORT)
                .setPlaceholder("Enter a title for your feedback")
                .setMinLength(0)
                .setMaxLength(100)
                .build();

        final TextInput body = TextInput.create("body", "Text", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Your feedback goes here")
                .setMinLength(10)
                .setMaxLength(1024)
                .build();

        final EmbedBuilder feedbackBuilder = new EmbedBuilder();

        final DiscordModal modal = new DiscordModal(event.getUser().getId(), "Send feedback", modalInteractionEvent -> {
            modalInteractionEvent.getJDA().getUserById(recId).openPrivateChannel().queue(privateChannel -> {
                feedbackBuilder.setAuthor(modalInteractionEvent.getUser().getName())
                        .setTitle(modalInteractionEvent.getValue("title").getAsString())
                        .setDescription(modalInteractionEvent.getValue("body").getAsString());
                privateChannel.sendMessageEmbeds(feedbackBuilder.build()).queue();
                modalInteractionEvent.reply("Your feedback was sent").setEphemeral(true).queue();
            });
        }, email, body);


        event.replyModal(modal.buildModal()).queue();
    }
}
