package com.papilertus.command.core

import com.papilertus.command.Command
import com.papilertus.gui.modal.DiscordModal
import com.papilertus.init.Config
import dev.minn.jda.ktx.interactions.components.TextInput
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle

class FeedbackCommand(private val config: Config) : Command("feedback", "Sends feedback to the bot owner") {

    override fun execute(event: SlashCommandInteractionEvent): Boolean {
        val text = TextInput(
            "text",
            "Feedback",
            TextInputStyle.PARAGRAPH,
            required = true,
            requiredLength = IntRange(10, 2048)
        )

        val modal = DiscordModal(event.user.id, "test", { modalInteractionEvent ->
            modalInteractionEvent.jda.getUserById(config.feedbackRecipientId)!!.openPrivateChannel().queue {
                it.sendMessage(modalInteractionEvent.getValue("text")!!.asString).queue()
            }
            modalInteractionEvent.reply("Your feedback was sent!").setEphemeral(true).queue()

        }, text)
        event.replyModal(modal.buildModal()).queue()
        return true
    }
}