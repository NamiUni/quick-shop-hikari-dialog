package io.github.namiuni.qshdialog.configuration.configurations.dialog.body;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.ItemSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public sealed interface DialogBodySettings<T extends DialogBody> permits ItemDialogBodySettings, PlainMessageDialogBodySettings {

    static ItemDialogBodySettings.Builder item(final ItemSettings<?> item) {
        return new ItemDialogBodySettings.Builder(item);
    }

    static PlainMessageDialogBodySettings.Builder message(final Component contents) {
        return new PlainMessageDialogBodySettings.Builder(contents);
    }

    @Range(from = 1, to = 256) int width();

    T createDialogBody(DialogProviderContext context);
}
