package com.papilertus.birthdays.commands;

import com.papilertus.birthdays.database.BirthdayUser;
import com.papilertus.birthdays.database.UserDatabase;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public class WishlistCommand extends Command {

    public WishlistCommand() {
        setName("wishlist");
        setDescription("Manages your wishlist");
        setData(Commands.slash(getName(), getDescription())
                .addSubcommands(new SubcommandData("add", "Adds an item to your wishlist")
                        .addOption(OptionType.STRING, "item", "Your wish", true)
                )
                .addSubcommands(new SubcommandData("list", "Shows your wishlist"))
                .addSubcommands(new SubcommandData("remove", "Removes an item of yor wishlist")
                        .addOption(OptionType.STRING, "item", "The wish", true)));
    }

    @Override
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        final String wish = slashCommandInteractionEvent.getOption("item") == null ? null : slashCommandInteractionEvent.getOption("item").getAsString();
        final UserDatabase database = new UserDatabase();
        final BirthdayUser user = database.fetchUser(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
        switch (slashCommandInteractionEvent.getSubcommandName()) {
            case "add":
                if (user.getWishlist().contains(wish.toLowerCase())) {
                    slashCommandInteractionEvent.reply(wish + " is already in your wishlist!").setEphemeral(true).queue();
                    break;
                }
                if (user.getWishlist().size() == 10) {
                    slashCommandInteractionEvent.reply("You cannot have more than 10 items in your wishlist!").setEphemeral(true).queue();
                    break;
                }
                if (wish.length() > 256) {
                    slashCommandInteractionEvent.reply("Your wish has too many characters!").setEphemeral(true).queue();
                    break;
                }
                user.addWish(wish);
                slashCommandInteractionEvent.reply("Added " + wish + " to your wishlist!").setEphemeral(true).queue();
                break;
            case "remove":
                if (!user.getWishlist().contains(wish.toLowerCase())) {
                    slashCommandInteractionEvent.reply("You don't have " + wish + " in your wishlist!").setEphemeral(true).queue();
                    break;
                }
                user.removeWish(wish);
                slashCommandInteractionEvent.reply("Removed " + wish + " from your wishlist!").setEphemeral(true).queue();
                break;
            case "list":
                final EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(slashCommandInteractionEvent.getUser().getName(), null, slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                        .setThumbnail(slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                        .setColor(Color.BLACK)
                        .setTitle("Wishlist")
                        .setFooter("By Handschrift");
                for (String s : user.getWishlist()) {
                    builder.getDescriptionBuilder().append(s).append("\n");
                }
                slashCommandInteractionEvent.replyEmbeds(builder.build()).queue();
                break;
        }
    }
}
