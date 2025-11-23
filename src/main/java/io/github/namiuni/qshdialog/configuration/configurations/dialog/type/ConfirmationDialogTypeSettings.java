package io.github.namiuni.qshdialog.configuration.configurations.dialog.type;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.DialogActionButtonSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.type.ConfirmationType;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
@SuppressWarnings("UnstableApiUsage")
public record ConfirmationDialogTypeSettings(
        Key type,
        DialogActionButtonSettings yesButton,
        DialogActionButtonSettings noButton
) implements DialogTypeSettings<ConfirmationType> {

    private static final Key TYPE = Key.key("confirmation");

    public static ConfirmationDialogTypeSettings of(
            final DialogActionButtonSettings yesButton,
            final DialogActionButtonSettings noButton
    ) {
        return new ConfirmationDialogTypeSettings(TYPE, yesButton, noButton);
    }

    @Override
    public ConfirmationType createDialogType(final DialogProviderContext context) {
        return DialogType.confirmation(
                this.yesButton.createDialogActionButton(context),
                this.noButton.createDialogActionButton(context)
        );
    }
}
