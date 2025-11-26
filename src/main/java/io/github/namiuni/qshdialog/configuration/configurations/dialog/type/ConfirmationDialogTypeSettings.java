package io.github.namiuni.qshdialog.configuration.configurations.dialog.type;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.type.action.DialogActionButtonSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.type.ConfirmationType;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record ConfirmationDialogTypeSettings(
        DialogActionButtonSettings yes,
        DialogActionButtonSettings no
) implements DialogTypeSettings {

    @Override
    public Key type() {
        return DialogTypes.CONFIRMATION;
    }

    @Override
    public ConfirmationType createDialogType(final DialogProviderContext context) {
        return DialogType.confirmation(
                this.yes.createDialogActionButton(context),
                this.no.createDialogActionButton(context)
        );
    }
}
