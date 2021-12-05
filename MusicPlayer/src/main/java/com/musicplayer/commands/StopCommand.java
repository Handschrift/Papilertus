package com.musicplayer.commands;

import com.musicplayer.audio.GuildMusicManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;

public class StopCommand extends MusicCommand {

    public StopCommand(HashMap<String, GuildMusicManager> managers) {
        setName("stop");
        setDescription("Stops playing");
        setManagerMap(managers);
        setData(new CommandData(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        if (slashCommandEvent.getGuild() == null) {
            slashCommandEvent.reply("You can only execute this command on a server").setEphemeral(true).queue();
            return;
        }

        final AudioManager manager = slashCommandEvent.getGuild().getAudioManager();

        if (!manager.isConnected()) {
            slashCommandEvent.reply("I am currently not connected to a voice channel").setEphemeral(true).queue();
            return;
        }

        final Member executor = slashCommandEvent.getMember();

        if (!executor.getVoiceState().inVoiceChannel() || !executor.getVoiceState().getChannel().getId().equals(manager.getConnectedChannel().getId())) { //Check if member is NOT in the same channel
            slashCommandEvent.reply("You have to be in the same voice channel!").setEphemeral(true).queue();
            return;
        }

        final GuildMusicManager musicManager = getGuildAudioPlayer(slashCommandEvent.getGuild());

        musicManager.close();
        getManagerMap().remove(slashCommandEvent.getGuild().getId());

        slashCommandEvent.reply("Goodbye!").queue();
    }
}
