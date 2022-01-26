package com.papilertus.birthdays.commands;

import com.openpackagedbot.commands.core.Command;
import com.papilertus.birthdays.database.BirthdayUser;
import com.papilertus.birthdays.database.GuildDatabase;
import com.papilertus.birthdays.database.UserDatabase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class BirthdayCommand extends Command {

    public BirthdayCommand() {
        setName("birthday");
        setDescription("Manage your birthdays");
        setData(new CommandData(getName(), getDescription())
                .addSubcommands(new SubcommandData("set", "Adds a birthday")
                        .addOption(OptionType.STRING, "birthday", "Your birthday", true))
                .addSubcommands(new SubcommandData("remove", "Removes your birthday"))
                .addSubcommands(new SubcommandData("info", "Provides information about your birthday profile"))
                .addSubcommands(new SubcommandData("list", "Lists the next birthdays"))
                .addSubcommands(new SubcommandData("channel", "Sets the channel for the birthday notifications")
                        .addOption(OptionType.CHANNEL, "channel", "Notification channel", true)
                ));
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {

        if (slashCommandEvent.getSubcommandName() == null) {
            slashCommandEvent.reply("Please enter an option (set|remove)").setEphemeral(true).queue();
            return;
        }
        final DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0);
        final DateTimeFormatter formatter = builder.toFormatter();
        final UserDatabase database = new UserDatabase();
        final BirthdayUser user = database.fetchUser(slashCommandEvent.getUser().getId(), slashCommandEvent.getGuild().getId());

        switch (slashCommandEvent.getSubcommandName()) {
            case "set":
                final String birthday = slashCommandEvent.getOption("birthday").getAsString();
                final LocalDateTime raw = LocalDateTime.from(formatter.parse(birthday));
                LocalDateTime t;
                try {
                    t = LocalDateTime.of(LocalDate.now().getYear(), raw.getMonth(), raw.getDayOfMonth(), 0, 0, 0);

                    if (LocalDate.now().isAfter(t.toLocalDate())) {
                        t = LocalDateTime.of(t.getYear() + 1, t.getMonth(), t.getDayOfMonth(), 0, 0, 0);
                    }
                } catch (DateTimeParseException e) {
                    slashCommandEvent.reply("Please enter a valid date").setEphemeral(true).queue();
                    return;
                }
                if (user == null) {
                    database.addUser(slashCommandEvent.getUser().getId(),
                            slashCommandEvent.getGuild().getId(),
                            t.toLocalDate(), "Europe/Berlin", Period.between(raw.toLocalDate(), LocalDate.now()).getYears());
                    slashCommandEvent.reply("Your birthday was set to " + birthday).queue();
                    return;
                } else {
                    //TODO: Add cooldown or restriction
                    user.setBirthday(t.toLocalDate());
                    slashCommandEvent.reply("Your birthday has been updated to " + birthday).queue();
                }
                break;
            case "remove":
                database.deleteUser(slashCommandEvent.getUser().getId(), slashCommandEvent.getGuild().getId());
                slashCommandEvent.reply("Your birthday was removed").queue();
                break;
            case "list":
                final EmbedBuilder listBuilder = new EmbedBuilder()
                        .setTitle("Next birthdays");
                int i = 0;
                for (BirthdayUser birthdayUser : database.getAllAfter(slashCommandEvent.getGuild().getId(), LocalDate.now())) {
                    if (i > 10)
                        break;
                    listBuilder.getDescriptionBuilder()
                            .append(slashCommandEvent.getGuild().getMemberById(birthdayUser.getUserId()).getUser().getAsMention())
                            .append(" ")
                            .append(birthdayUser.getBirthday()).append("\n");
                    i++;
                }
                slashCommandEvent.replyEmbeds(listBuilder.build()).queue();
                break;
            case "info":
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                if (user == null) {
                    slashCommandEvent.reply("You didn't provide your birthday yet").setEphemeral(true).queue();
                    break;
                }
                embedBuilder.addField("Your Birthday!", user.getBirthday(), true)
                        .setThumbnail(slashCommandEvent.getUser().getEffectiveAvatarUrl())
                        .setAuthor(slashCommandEvent.getUser().getName(), null, slashCommandEvent.getUser().getEffectiveAvatarUrl())
                        .setFooter("Made by Handschrift")
                        .setTitle("Your profile");
                slashCommandEvent.replyEmbeds(embedBuilder.build()).queue();
                break;
            case "channel":
                final MessageChannel channel = slashCommandEvent.getOption("channel").getAsMessageChannel();
                final GuildDatabase guildDatabase = new GuildDatabase();
                guildDatabase.addBirthdayChannel(slashCommandEvent.getGuild().getId(), channel.getId());
                slashCommandEvent.reply("Set!").queue();
                break;
        }

    }
}