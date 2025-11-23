package io.github.namiuni.qshdialog.configuration.configurations.dialog.type;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.DialogActionButtonSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
@SuppressWarnings("UnstableApiUsage")
public sealed interface DialogTypeSettings<T extends DialogType> permits ConfirmationDialogTypeSettings {

    static ConfirmationDialogTypeSettings confirmation(
            final DialogActionButtonSettings yesButton,
            final DialogActionButtonSettings noButton
    ) {
        return ConfirmationDialogTypeSettings.of(yesButton, noButton);
    }

    Key type();

    T createDialogType(DialogProviderContext context);
}
