package com.papilertus.birthdays.commands;

import com.papilertus.birthdays.database.BirthdayUser;
import com.papilertus.birthdays.database.GuildDatabase;
import com.papilertus.birthdays.database.UserDatabase;
import com.papilertus.commands.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
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
        setData(Commands.slash(getName(), getDescription())
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
    protected void execute(SlashCommandInteractionEvent slashCommandInteractionEvent) {

        if (slashCommandInteractionEvent.getSubcommandName() == null) {
            slashCommandInteractionEvent.reply("Please enter an option (set|remove)").setEphemeral(true).queue();
            return;
        }
        final DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendOptional(DateTimeFormatter.ofPattern("MM-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("dd.MM"))
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM"))
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear());
        final DateTimeFormatter formatter = builder.toFormatter();
        final BirthdayUser user = UserDatabase.fetchUser(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());

        switch (slashCommandInteractionEvent.getSubcommandName()) {
            case "set":
                final String birthday = slashCommandInteractionEvent.getOption("birthday").getAsString();
                final LocalDateTime raw = LocalDateTime.from(formatter.parse(birthday));
                LocalDateTime t;
                try {
                    t = LocalDateTime.of(LocalDate.now().getYear(), raw.getMonth(), raw.getDayOfMonth(), 0, 0, 0);

                    if (LocalDate.now().isAfter(t.toLocalDate())) {
                        t = LocalDateTime.of(t.getYear() + 1, t.getMonth(), t.getDayOfMonth(), 0, 0, 0);
                    }
                } catch (DateTimeParseException e) {
                    slashCommandInteractionEvent.reply("Please enter a valid date").setEphemeral(true).queue();
                    return;
                }
                if (user == null) {
                    UserDatabase.addUser(slashCommandInteractionEvent.getUser().getId(),
                            slashCommandInteractionEvent.getGuild().getId(),
                            t.toLocalDate(), "Europe/Berlin", Period.between(raw.toLocalDate(), LocalDate.now()).getYears(), 5);
                    slashCommandInteractionEvent.reply("Your birthday was set to " + birthday).queue();
                    return;
                } else {
                    if (user.getTries() == 0) {
                        slashCommandInteractionEvent.reply("You cannot set your birthday anymore!").setEphemeral(true).queue();
                        break;
                    }
                    user.setBirthday(t.toLocalDate());
                    user.setTries(user.getTries() - 1);
                    slashCommandInteractionEvent.reply("Your birthday has been updated to " + birthday).queue();
                }
                break;
            case "remove":
                UserDatabase.deleteUser(slashCommandInteractionEvent.getUser().getId(), slashCommandInteractionEvent.getGuild().getId());
                slashCommandInteractionEvent.reply("Your birthday has been removed!").queue();
                break;
            case "list":
                final EmbedBuilder listBuilder = new EmbedBuilder()
                        .setTitle("Next birthdays");
                int i = 0;
                if (UserDatabase.getAllAfter(slashCommandInteractionEvent.getGuild().getId(), LocalDate.now()).isEmpty()) {
                    listBuilder.getDescriptionBuilder().append("There are no specified birthdays");
                    slashCommandInteractionEvent.replyEmbeds(listBuilder.build()).setEphemeral(true).queue();
                    break;

                }
                for (BirthdayUser birthdayUser : UserDatabase.getAllAfter(slashCommandInteractionEvent.getGuild().getId(), LocalDate.now())) {
                    if (i > 10)
                        break;
                    listBuilder.getDescriptionBuilder()
                            .append(slashCommandInteractionEvent.getGuild().getMemberById(birthdayUser.getUserId()).getUser().getAsMention())
                            .append(" ")
                            .append(birthdayUser.getBirthday()).append("\n");
                    i++;
                }
                slashCommandInteractionEvent.replyEmbeds(listBuilder.build()).queue();
                break;
            case "info":
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                if (user == null) {
                    slashCommandInteractionEvent.reply("You didn't provide your birthday yet").setEphemeral(true).queue();
                    break;
                }
                embedBuilder.addField("Your Birthday!", user.getBirthday(), true)
                        .setThumbnail(slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                        .setAuthor(slashCommandInteractionEvent.getUser().getName(), null, slashCommandInteractionEvent.getUser().getEffectiveAvatarUrl())
                        .setTitle("Your profile");
                slashCommandInteractionEvent.replyEmbeds(embedBuilder.build()).queue();
                break;
            case "channel":
                final MessageChannel channel = slashCommandInteractionEvent.getOption("channel").getAsMessageChannel();
                GuildDatabase.addBirthdayGuild(slashCommandInteractionEvent.getGuild().getId(), channel.getId());
                slashCommandInteractionEvent.reply(channel.getAsMention() + " is now the birthday-channel!").queue();
                break;
        }

    }
}