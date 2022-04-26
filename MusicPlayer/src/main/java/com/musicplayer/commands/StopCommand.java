package com.musicplayer.commands;

import com.musicplayer.audio.GuildMusicManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;

public class StopCommand extends MusicCommand {

    public StopCommand(HashMap<String, GuildMusicManager> managers) {
        setName("stop");
        setDescription("Stops playing");
        setManagerMap(managers);
        setData(Commands.slash(getName(), getDescription()));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if (slashCommandInteractionEvent.getGuild() == null) {
            slashCommandInteractionEvent.reply("You can only execute this command on a server").setEphemeral(true).queue();
            return;
        }

        final AudioManager manager = slashCommandInteractionEvent.getGuild().getAudioManager();

        if (!manager.isConnected()) {
            slashCommandInteractionEvent.reply("I am currently not connected to a voice channel").setEphemeral(true).queue();
            return;
        }

        final Member executor = slashCommandInteractionEvent.getMember();

        if (!executor.getVoiceState().inAudioChannel() || !executor.getVoiceState().getChannel().getId().equals(manager.getConnectedChannel().getId())) { //Check if member is NOT in the same channel
            slashCommandInteractionEvent.reply("You have to be in the same voice channel!").setEphemeral(true).queue();
            return;
        }

        final GuildMusicManager musicManager = getGuildAudioPlayer(slashCommandInteractionEvent.getGuild());

        musicManager.close();
        getManagerMap().remove(slashCommandInteractionEvent.getGuild().getId());

        slashCommandInteractionEvent.reply("Goodbye!").queue();
    }
}
