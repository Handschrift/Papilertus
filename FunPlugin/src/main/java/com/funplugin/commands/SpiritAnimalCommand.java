package com.funplugin.commands;

import com.funplugin.entities.SpiritUser;
import com.funplugin.gui.buttons.FavoriteAdder;
import com.funplugin.init.FunPlugin;
import com.mongodb.client.model.Updates;
import com.papilertus.commands.core.Command;
import com.papilertus.gui.button.DiscordButton;
import com.papilertus.gui.generator.PapilertusMessageBuilder;
import com.papilertus.plugin.PluginDataStore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
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
        setData(Commands.slash(getName(), getDescription())
                .addSubcommands(new SubcommandData("show", "Shows your today's spirit animal"))
                .addSubcommands(new SubcommandData("favorite-show", "Shows your current favorite animal"))
                .addSubcommands(new SubcommandData("favorite-remove", "Removes your current favorite animal")));
        this.dataStore = dataStore;
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {

        final long userId = slashCommandInteractionEvent.getUser().getIdLong();
        final int sumOfDays = LocalDate.now().getDayOfMonth() + LocalDate.now().getYear() + LocalDate.now().getMonthValue();
        final Document document = new Document("_id.userId", slashCommandInteractionEvent.getUser().getId());
        document.append("_id.guildId", slashCommandInteractionEvent.getGuild().getId());
        final SpiritUser user = dataStore.getEntry(document, SpiritUser.class);
        final File spiritAnimalDir = new File("spirit_animals");
        final int random = new Random(userId / sumOfDays).nextInt(spiritAnimalDir.list().length);
        switch (slashCommandInteractionEvent.getSubcommandName()) {
            case "favorite-show":
                if (user == null || user.getFavoriteIds().isEmpty()) {
                    slashCommandInteractionEvent.reply("You don't have a favorite animal yet").setEphemeral(true).queue();
                    return;
                }

                final EmbedBuilder favoriteShowBuilder = new EmbedBuilder()
                        .setAuthor(slashCommandInteractionEvent.getUser().getAsTag(), null, slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                        .setDescription("Your current favorite spirit animal is:")
                        .setFooter("Execute /spirit-animal favorite-remove to remove your favorite animal!")
                        .setImage("attachment://" + user.getFavoriteIds().get(0) + ".png");
                slashCommandInteractionEvent.replyEmbeds(favoriteShowBuilder.build()).addFile(new File("spirit_animals/" + user.getFavoriteIds().get(0) + ".png")).queue();

                break;
            case "favorite-remove":
                if (user.getFavoriteIds().isEmpty()) {
                    slashCommandInteractionEvent.reply("You don't have a favorite animal!").setEphemeral(true).queue();
                    return;
                }
                dataStore.modifyEntry(document, Updates.set("favoriteIds", new ArrayList<>()));
                slashCommandInteractionEvent.reply("Your favorite animal has been removed!").setEphemeral(true).queue();
                break;
            case "show":
                final File randomFile = spiritAnimalDir.listFiles()[random];
                final EmbedBuilder builder = new EmbedBuilder().setImage("attachment://" + randomFile.getName())
                        .setAuthor(slashCommandInteractionEvent.getUser().getAsTag(), null, slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                        .setDescription("And your today's spirit animal is...")
                        .setFooter("Execute the command tomorrow to see your next spirit animal!");
                final PapilertusMessageBuilder papilertusMessageBuilder = new PapilertusMessageBuilder();
                papilertusMessageBuilder.setEmbeds(builder.build())
                        .addButtons(new DiscordButton(slashCommandInteractionEvent.getUser().getId(), new FavoriteAdder(dataStore), ButtonStyle.PRIMARY, "Mark as favorite"));
                slashCommandInteractionEvent.reply(papilertusMessageBuilder.build()).addFile(randomFile).queue();
                break;
        }
    }
}
