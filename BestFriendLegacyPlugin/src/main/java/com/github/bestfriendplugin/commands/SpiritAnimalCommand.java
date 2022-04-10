package com.github.bestfriendplugin.commands;

import com.github.bestfriendplugin.BestFriend;
import com.github.bestfriendplugin.buttonevents.FavoriteAdder;
import com.github.bestfriendplugin.database.SpiritUser;
import com.mongodb.client.model.Updates;
import com.openpackagedbot.commands.core.Command;
import com.openpackagedbot.gui.button.DiscordButton;
import com.openpackagedbot.gui.generator.PapilertusMessageBuilder;
import com.openpackagedbot.plugin.PluginDataStore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import org.bson.Document;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class SpiritAnimalCommand extends Command {

    private final PluginDataStore dataStore;

    public SpiritAnimalCommand(PluginDataStore dataStore) {
        setName("spirit-animal");
        setDescription("Shows your current spirit animal");
        setData(new CommandData(getName(), getDescription())
                .addSubcommands(new SubcommandData("show", "Shows your today's spirit animal"))
                .addSubcommands(new SubcommandData("favorite-show", "Shows your current favorite animal"))
                .addSubcommands(new SubcommandData("favorite-remove", "Removes your current favorite animal")));
        this.dataStore = dataStore;
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {

        long userId = slashCommandEvent.getUser().getIdLong();
        int sumOfDays = LocalDate.now().getDayOfMonth() + LocalDate.now().getYear() + LocalDate.now().getMonthValue();
        int random = new Random(userId / sumOfDays).nextInt(BestFriend.getConfig().readInt("amount_of_images"));
        final Document document = new Document("_id.userId", slashCommandEvent.getUser().getId());
        document.append("_id.guildId", slashCommandEvent.getGuild().getId());
        final SpiritUser user = dataStore.getEntry(document, SpiritUser.class);
        switch (slashCommandEvent.getSubcommandName()) {
            case "favorite-show":
                if (user == null || user.getFavoriteIds().isEmpty()) {
                    slashCommandEvent.reply("You don't have a favorite animal yet").setEphemeral(true).queue();
                    return;
                }

                final EmbedBuilder favoriteShowBuilder = new EmbedBuilder()
                        .setAuthor(slashCommandEvent.getUser().getAsTag(), null, slashCommandEvent.getUser().getEffectiveAvatarUrl())
                        .setDescription("Your current favorite spirit animal is:")
                        .setFooter("Execute /spirit-animal favorite-remove!")
                        .setImage("attachment://" + user.getFavoriteIds().get(0) + ".png");
                slashCommandEvent.replyEmbeds(favoriteShowBuilder.build()).addFile(new File("spirit_animals/" + user.getFavoriteIds().get(0) + ".png")).queue();

                break;
            case "favorite-remove":
                if (user.getFavoriteIds().isEmpty()) {
                    slashCommandEvent.reply("You don't have a favorite animal!").setEphemeral(true).queue();
                    return;
                }
                dataStore.modifyEntry(document, Updates.set("favoriteIds", new ArrayList<>()));
                slashCommandEvent.reply("Your favorite animal has been removed!").setEphemeral(true).queue();
                break;
            case "show":
                final EmbedBuilder builder = new EmbedBuilder().setImage("attachment://" + random + ".png")
                        .setAuthor(slashCommandEvent.getUser().getAsTag(), null, slashCommandEvent.getUser().getEffectiveAvatarUrl())
                        .setDescription("And your today's spirit animal is...")
                        .setFooter("Execute the command tomorrow to see your next spirit animal!");
                final PapilertusMessageBuilder papilertusMessageBuilder = new PapilertusMessageBuilder();
                papilertusMessageBuilder.setEmbeds(builder.build())
                        .addButtons(new DiscordButton(slashCommandEvent.getUser().getId(), new FavoriteAdder(dataStore), ButtonStyle.PRIMARY, "Mark as favorite"));
                slashCommandEvent.reply(papilertusMessageBuilder.build()).addFile(new File("spirit_animals/" + random + ".png")).queue();
                break;
        }
    }
}
