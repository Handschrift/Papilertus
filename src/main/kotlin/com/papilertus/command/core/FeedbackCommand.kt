package com.papilertus.command.core

import com.papilertus.command.Command
import com.papilertus.gui.modal.DiscordModal
import dev.minn.jda.ktx.interactions.components.TextInput
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle

class FeedbackCommand : Command("feedback", "Sends feedback to the bot owner") {

    override fun execute(event: SlashCommandInteractionEvent) {
        val test = TextInput("a", "asd", TextInputStyle.SHORT)

        val modal = DiscordModal(event.user.id, "test", { modalInteractionEvent ->
            modalInteractionEvent.user.openPrivateChannel().queue {
                it.sendMessage("bla").queue()
            }
            modalInteractionEvent.reply("gese").queue()

        }, test)
        event.replyModal(modal.buildModal()).queue()
    }
}