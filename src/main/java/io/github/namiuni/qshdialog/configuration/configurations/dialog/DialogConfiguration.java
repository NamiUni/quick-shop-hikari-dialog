package io.github.namiuni.qshdialog.configuration.configurations.dialog;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.DialogBase;
import java.util.List;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public sealed interface DialogConfiguration permits ConfirmationDialogConfiguration {

    DialogLike createDialog(DialogProviderContext context);

    Component title();

    @Nullable Component externalTitle();

    List<DialogBodySettings<?>> body();

    List<DialogInputSettings<?>> inputs();

    boolean canCloseWithEscape();

    boolean pause();

    DialogBase.DialogAfterAction afterAction();
}
