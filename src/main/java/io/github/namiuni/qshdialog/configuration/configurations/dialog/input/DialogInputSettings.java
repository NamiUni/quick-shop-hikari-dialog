package io.github.namiuni.qshdialog.configuration.configurations.dialog.input;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public sealed interface DialogInputSettings permits BooleanDialogInputSettings, NumberRangeDialogInputSettings, SingleOptionDialogInputSettings, TextDialogInputSettings {

    static TextDialogInputSettings.Builder text(final String key, final String label) {
        return new TextDialogInputSettings.Builder(key, label);
    }

    static BooleanDialogInputSettings.Builder bool(final String key, final String label) {
        return new BooleanDialogInputSettings.Builder(key, label);
    }

    static SingleOptionDialogInputSettings.Builder singleOption(final String key, final String label) {
        return new SingleOptionDialogInputSettings.Builder(key, label);
    }

    static NumberRangeDialogInputSettings.Builder numberRange(final String key, final String label, final String start, final String end) {
        return new NumberRangeDialogInputSettings.Builder(key, label, start, end);
    }

    Key type();

    DialogInput createDialogInput(DialogProviderContext context);

    String key();

    String label();
}
