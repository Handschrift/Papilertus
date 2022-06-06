package com.funplugin.gui.buttons;

import com.funplugin.entities.SpiritUser;
import com.mongodb.client.model.Updates;
import com.papilertus.gui.button.Pressable;
import com.papilertus.plugin.PluginDataStore;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.bson.Document;

import java.util.ArrayList;

public class FavoriteAdder implements Pressable {
    private final PluginDataStore dataStore;

    public FavoriteAdder(PluginDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void onClick(ButtonInteractionEvent buttonClickEvent) {
        final String url = buttonClickEvent.getMessage().getEmbeds().get(0).getImage().getUrl();
        final String id = url.substring(url.lastIndexOf("/") + 1).replace(".png", "");
        final Document filter = new Document("_id.userId", buttonClickEvent.getUser().getId());
        filter.append("_id.guildId", buttonClickEvent.getGuild().getId());
        final SpiritUser user = dataStore.getEntry(filter, SpiritUser.class);
        final ArrayList<String> ids = new ArrayList<>();
        ids.add(id);

        if (user == null) {
            dataStore.addEntry(new SpiritUser(buttonClickEvent.getUser().getId(), buttonClickEvent.getGuild().getId(), ids));
            buttonClickEvent.reply("This animal was marked as your favorite!").setEphemeral(true).queue();
            return;
        }

        if (!user.getFavoriteIds().isEmpty() && user.getFavoriteIds().get(0).equals(id)) {
            buttonClickEvent.reply("This animal is already your favorite!").setEphemeral(true).queue();
            return;
        }

        if (!user.getFavoriteIds().isEmpty()) {
            buttonClickEvent.reply("You already have a favorite animal! You need to remove your current favorite first!").setEphemeral(true).queue();
            return;
        }

        //add animal
        dataStore.modifyEntry(filter, Updates.set("favoriteIds", ids));
        buttonClickEvent.reply("This animal was marked as your favorite!").setEphemeral(true).queue();
    }
}
