package io.github.namiuni.qshdialog.configuration.configurations.dialog.type;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public sealed interface DialogTypeSettings permits ConfirmationDialogTypeSettings {

    Key type();

    DialogType createDialogType(DialogProviderContext context);
}
