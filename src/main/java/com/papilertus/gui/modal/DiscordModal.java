package com.papilertus.gui.modal;

import com.papilertus.gui.button.ButtonRegistry;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordModal {
    private final String id;
    private String userId = "";
    private final Modallable modallable;

    private final TextInput[] inputs;

    private Modal modal;

    private final String title;


    public DiscordModal(String userId, String title, Modallable modallable, TextInput... inputs) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.modallable = modallable;
        this.inputs = inputs;
        this.title = title;
    }

    public DiscordModal(String title, Modallable modallable, TextInput... inputs) {
        this.id = UUID.randomUUID().toString();
        this.modallable = modallable;
        this.inputs = inputs;
        this.title = title;

    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Modallable getModallable() {
        return modallable;
    }

    public TextInput[] getInputs() {
        return inputs;
    }

    public Modal buildModal() {
        Modal.Builder builder = Modal.create(id, title);
        for (TextInput input : inputs) {
            builder.addActionRows(ActionRow.of(input));
        }
        modal = builder.build();
        ModalRegistry.registerModal(this.id, this);

        //for unregistering
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                ButtonRegistry.unregisterButton(id);
                executorService.shutdown();
            }
        }, 25, TimeUnit.MINUTES);

        return modal;
    }

    public String getTitle() {
        return title;
    }


}
