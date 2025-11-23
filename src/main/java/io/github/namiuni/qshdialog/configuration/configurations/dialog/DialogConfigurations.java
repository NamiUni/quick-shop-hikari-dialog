package io.github.namiuni.qshdialog.configuration.configurations.dialog;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.DynamicItemSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.ItemTypes;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputSettings;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DialogConfigurations {

    public static final ConfirmationDialogConfiguration CREATION_DEFAULT = new ConfirmationDialogConfiguration(
            Component.translatable("qsh_dialog.dialog.title.shop.creation"),
            null,
            List.of(
                    DialogBodySettings.item(new DynamicItemSettings(ItemTypes.PRODUCT, Map.of(), 1))
                            .description(DialogBodySettings.message(Component.translatable("qsh_dialog.dialog.description.shop.creation")).build())
                            .showDecoration(true)
                            .showTooltip(true)
                            .build()
            ),
            List.of(
                    DialogInputSettings.text("shop_name", Component.translatable("qsh_dialog.dialog.label.shop.name"))
                            .labelVisible(true)
                            .initial("")
                            .multilineOptions(null)
                            .build()
            ),
            true,
            true,
            DialogBase.DialogAfterAction.CLOSE,
            DialogActionButtonSettings.builder(Component.translatable("qsh_dialog.dialog.label.create"))
                    .action(DialogAction.customClick(Key.key("qshdialog", "shop/create"), null))
                    .build(),
            DialogActionButtonSettings.builder(Component.translatable("qsh_dialog.dialog.label.cancel"))
                    .build()
    );

    private DialogConfigurations() {
    }
}
