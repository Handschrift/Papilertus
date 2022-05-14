package com.economy.listeners;

import com.economy.database.databases.UserDatabase;
import com.economy.database.models.EconomyUser;
import com.economy.game.element.GameUpgrade;
import com.economy.game.element.IncrementType;
import com.economy.init.Economy;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BumpListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getInteraction() == null) return;
        final Message.Interaction bumpInteraction = event.getMessage().getInteraction();
        //check if it is the disboard bot and if the command executed was a bump command
        if (event.getAuthor().getId().equals("302050872383242240")
                && bumpInteraction.getName().equals("bump")) {
            //I don't know if this is needed, but it is an extra verification step for a successful bump
            if (event.getMessage().getEmbeds().get(0).getImage().getUrl().equals("https://disboard.org/images/bot-command-image-bump.png")) {
                final EconomyUser user = UserDatabase.fetch(bumpInteraction.getUser().getId(), event.getGuild().getId());
                final float coins = GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCollectablesOnBumpGain(), user, IncrementType.BUMP);
                if (coins == 0)
                    return;
                user.addCoins(coins);
                UserDatabase.updateUser(user);
                event.getChannel().sendMessage(bumpInteraction.getUser().getAsMention() + " bumped the server and got "
                        + GameUpgrade.getAggregatedUpgradeValue(Economy.getEconomyConfig().getBaseCollectablesOnBumpGain(), user, IncrementType.BUMP) + " " + Economy.getEconomyConfig().getCollectableName() + "!").queue();
            }
        }
    }
}
