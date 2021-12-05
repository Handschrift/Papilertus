package com.musicplayer.commands;

import com.musicplayer.audio.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;

public class SkipCommand extends MusicCommand {

    public SkipCommand(HashMap<String, GuildMusicManager> managers) {
        setName("skip");
        setDescription("Skips the current song");
        setManagerMap(managers);
        setData(new CommandData(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        if (slashCommandEvent.getGuild() == null) {
            slashCommandEvent.reply("You can only execute this command in a server").setEphemeral(true).queue();
            return;
        }
        final AudioManager manager = slashCommandEvent.getGuild().getAudioManager();
        final Member executor = slashCommandEvent.getMember();

        if (!executor.getVoiceState().inVoiceChannel() || !executor.getVoiceState().getChannel().getId().equals(manager.getConnectedChannel().getId())) { //Check if member is NOT in the same channel
            slashCommandEvent.reply("You have to be in the same voice channel!").setEphemeral(true).queue();
            return;
        }

        final EmbedBuilder builder = new EmbedBuilder();
        final GuildMusicManager guildManager = getGuildAudioPlayer(slashCommandEvent.getGuild());
        guildManager.getScheduler().nextTrack();
        builder.setDescription("Skipped to the next track");
        slashCommandEvent.replyEmbeds(builder.build()).queue();
    }
}
