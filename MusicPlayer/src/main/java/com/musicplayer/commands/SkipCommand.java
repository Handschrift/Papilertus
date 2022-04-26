package com.musicplayer.commands;

import com.musicplayer.audio.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;

public class SkipCommand extends MusicCommand {

    public SkipCommand(HashMap<String, GuildMusicManager> managers) {
        setName("skip");
        setDescription("Skips the current song");
        setManagerMap(managers);
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if (slashCommandInteractionEvent.getGuild() == null) {
            slashCommandInteractionEvent.reply("You can only execute this command in a server").setEphemeral(true).queue();
            return;
        }
        final AudioManager manager = slashCommandInteractionEvent.getGuild().getAudioManager();
        final Member executor = slashCommandInteractionEvent.getMember();

        if (!executor.getVoiceState().inAudioChannel() || !executor.getVoiceState().getChannel().getId().equals(manager.getConnectedChannel().getId())) { //Check if member is NOT in the same channel
            slashCommandInteractionEvent.reply("You have to be in the same voice channel!").setEphemeral(true).queue();
            return;
        }

        final EmbedBuilder builder = new EmbedBuilder();
        final GuildMusicManager guildManager = getGuildAudioPlayer(slashCommandInteractionEvent.getGuild());
        guildManager.getScheduler().nextTrack();
        builder.setDescription("Skipped to the next track");
        slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
    }
}
