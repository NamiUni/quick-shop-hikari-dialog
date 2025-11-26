package io.github.namiuni.qshdialog.configuration.configurations.dialog.body;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.ItemSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public sealed interface DialogBodySettings permits ItemDialogBodySettings, PlainMessageDialogBodySettings {

    static ItemDialogBodySettings.Builder item(final ItemSettings<?> item) {
        return new ItemDialogBodySettings.Builder(item);
    }

    static PlainMessageDialogBodySettings.Builder message(final String contents) {
        return new PlainMessageDialogBodySettings.Builder(contents);
    }

    Key type();

    @Nullable @Range(from = 1, to = 256) Integer width();

    DialogBody createDialogBody(DialogProviderContext context);
}
