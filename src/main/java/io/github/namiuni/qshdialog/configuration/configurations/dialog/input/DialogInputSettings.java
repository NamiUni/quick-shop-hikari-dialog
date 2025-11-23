package io.github.namiuni.qshdialog.configuration.configurations.dialog.input;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public sealed interface DialogInputSettings<T extends DialogInput> permits TextDialogInputSettings {

    static TextDialogInputSettings.Builder text(final String key, final Component label) {
        return new TextDialogInputSettings.Builder(key, label);
    }

    T createDialogInput(DialogProviderContext context);
}
