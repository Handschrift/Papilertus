package com.papilertus.birthdays.commands;

import com.openpackagedbot.commands.core.Command;
import com.papilertus.birthdays.database.BirthdayUser;
import com.papilertus.birthdays.database.UserDatabase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public class WishlistCommand extends Command {

    public WishlistCommand() {
        setName("wishlist");
        setDescription("Manages your wishlist");
        setData(new CommandData(getName(), getDescription())
                .addSubcommands(new SubcommandData("add", "Adds an item to your wishlist")
                        .addOption(OptionType.STRING, "item", "Your wish", true)
                )
                .addSubcommands(new SubcommandData("list", "Shows your wishlist"))
                .addSubcommands(new SubcommandData("remove", "Removes an item of yor wishlist")
                        .addOption(OptionType.STRING, "item", "The wish", true)));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        final String wish = slashCommandEvent.getOption("item") == null ? null : slashCommandEvent.getOption("item").getAsString();
        final UserDatabase database = new UserDatabase();
        final BirthdayUser user = database.fetchUser(slashCommandEvent.getUser().getId(), slashCommandEvent.getGuild().getId());
        switch (slashCommandEvent.getSubcommandName()) {
            case "add":
                if (user.getWishlist().contains(wish.toLowerCase())) {
                    slashCommandEvent.reply(wish + " is already in your wishlist!").setEphemeral(true).queue();
                    break;
                }
                if (user.getWishlist().size() == 10) {
                    slashCommandEvent.reply("You cannot have more than 10 items in your wishlist!").setEphemeral(true).queue();
                    break;
                }
                if (wish.length() > 256) {
                    slashCommandEvent.reply("Your wish has too many characters!").setEphemeral(true).queue();
                    break;
                }
                user.addWish(wish);
                slashCommandEvent.reply("Added " + wish + " to your wishlist!").setEphemeral(true).queue();
                break;
            case "remove":
                if (!user.getWishlist().contains(wish.toLowerCase())) {
                    slashCommandEvent.reply("You don't have " + wish + " in your wishlist!").setEphemeral(true).queue();
                    break;
                }
                user.removeWish(wish);
                slashCommandEvent.reply("Removed " + wish + " from your wishlist!").setEphemeral(true).queue();
                break;
            case "list":
                final EmbedBuilder builder = new EmbedBuilder()
                        .setAuthor(slashCommandEvent.getUser().getName(), null, slashCommandEvent.getUser().getEffectiveAvatarUrl())
                        .setThumbnail(slashCommandEvent.getUser().getEffectiveAvatarUrl())
                        .setColor(Color.BLACK)
                        .setTitle("Wishlist")
                        .setFooter("By Handschrift");
                for (String s : user.getWishlist()) {
                    builder.getDescriptionBuilder().append(s).append("\n");
                }
                slashCommandEvent.replyEmbeds(builder.build()).queue();
                break;
        }
    }
}
