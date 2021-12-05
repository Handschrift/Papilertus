package com.musicplayer.commands;

import com.musicplayer.audio.GuildMusicManager;
import com.musicplayer.audio.SoundManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.HashMap;

public class PlayCommand extends MusicCommand {

    public PlayCommand(HashMap<String, GuildMusicManager> managers) {
        setName("play");
        setDescription("Plays a track");
        setManagerMap(managers);
        setData(new CommandData(getName(), getDescription()).addOption(OptionType.STRING, "name", "name or url", true));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {

        if (slashCommandEvent.getGuild() == null) {
            slashCommandEvent.reply("This command can only be executed in a server").setEphemeral(true).queue();
            return;
        }

        final String name = slashCommandEvent.getOption("name").getAsString();
        final Member executor = slashCommandEvent.getMember();
        final AudioManager manager = slashCommandEvent.getGuild().getAudioManager();
        final GuildMusicManager guildManager = getGuildAudioPlayer(slashCommandEvent.getGuild());


        if (!executor.getVoiceState().inVoiceChannel()) {
            slashCommandEvent.reply("You have to be in a voice channel!").setEphemeral(true).queue();
            return;
        }

        if (!manager.isConnected()) {
            manager.openAudioConnection(executor.getVoiceState().getChannel());
        } else if (!executor.getVoiceState().getChannel().getId().equals(manager.getConnectedChannel().getId())) { //Check if member is NOT in the same channel
            slashCommandEvent.reply("You have to be in the same voice channel!").setEphemeral(true).queue();
            return;
        }

        final EmbedBuilder builder = new EmbedBuilder().setColor(Color.RED);

        SoundManager.getManager().loadItemOrdered(guildManager, name, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                builder.setColor(Color.GREEN);
                if (guildManager.getScheduler().queue(track)) {
                    builder.setDescription("Added " + track.getInfo().title + " to queue");
                } else {
                    builder.setDescription("Now playing: " + track.getInfo().title);
                }
                slashCommandEvent.replyEmbeds(builder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                builder.setDescription("Loading playlists is currently not supported!");
                slashCommandEvent.replyEmbeds(builder.build()).queue();
            }

            @Override
            public void noMatches() {
                builder.setDescription("There were no songs found with this identifier");
                slashCommandEvent.replyEmbeds(builder.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                builder.setDescription("An error occurred while queueing the track");
                slashCommandEvent.replyEmbeds(builder.build()).queue();
            }
        });
    }
}
