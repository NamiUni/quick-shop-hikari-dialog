package io.github.namiuni.qshdialog.configuration.configurations.dialog.input;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
@SuppressWarnings("UnstableApiUsage")
public record SingleOptionEntrySettings(String id, String display, boolean initial) {

    public SingleOptionDialogInput.OptionEntry createOptionEntry(final DialogProviderContext context) {
        final Component renderedDisplay = MiniMessage.miniMessage().deserialize(this.display);
        return SingleOptionDialogInput.OptionEntry.create(this.id, renderedDisplay, this.initial);
    }
}
