package io.github.namiuni.qshdialog.configuration.configurations.dialog;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.DynamicItemSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.QSHDialogItemType;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.SingleOptionEntrySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.type.ConfirmationDialogTypeSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.type.action.DialogActionButtonSettings;
import io.github.namiuni.qshdialog.shop.ShopDisplay;
import io.github.namiuni.qshdialog.shop.ShopMode;
import io.github.namiuni.qshdialog.shop.ShopStatus;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DialogConfigurations {

    public static final DialogConfiguration CREATION_DEFAULT = DialogConfiguration.builder("<lang:qsh_dialog.dialog.title.shop.creation>")
            .body(List.of(
                    DialogBodySettings.item(new DynamicItemSettings(QSHDialogItemType.PRODUCT, Map.of(), 1))
                            .description(DialogBodySettings.message("<lang:qsh_dialog.dialog.description.shop.creation>").build())
                            .build()))
            .inputs(List.of(
                    DialogInputSettings.singleOption("shop_mode", "<lang:qsh_dialog.dialog.label.shop.mode>")
                            .options(List.of(
                                    new SingleOptionEntrySettings(
                                            ShopMode.SELLING.name(),
                                            "<lang:qsh_dialog.shop.mode.selling>",
                                            true
                                    ),
                                    new SingleOptionEntrySettings(
                                            ShopMode.BUYING.name(),
                                            "<lang:qsh_dialog.shop.mode.buying>",
                                            false
                                    ))
                            )
                            .build(),
                    DialogInputSettings.singleOption("shop_status", "<lang:qsh_dialog.dialog.label.shop.status>")
                            .options(List.of(
                                    new SingleOptionEntrySettings(
                                            ShopStatus.AVAILABLE.name(),
                                            "<lang:qsh_dialog.shop.status.available>",
                                            true
                                    ),
                                    new SingleOptionEntrySettings(
                                            ShopStatus.UNAVAILABLE.name(),
                                            "<lang:qsh_dialog.shop.status.unavailable>",
                                            false
                                    )
                            ))
                            .build(),
                    DialogInputSettings.singleOption("display_item", "<lang:qsh_dialog.dialog.label.shop.display_item>")
                            .options(List.of(
                                    new SingleOptionEntrySettings(
                                            ShopDisplay.SHOW.name(),
                                            "<lang:qsh_dialog.dialog.option.shop.display_item.show>",
                                            true
                                    ),
                                    new SingleOptionEntrySettings(
                                            ShopDisplay.HIDE.name(),
                                            "<lang:qsh_dialog.dialog.option.shop.display_item.hide>",
                                            false
                                    )
                            ))
                            .build(),
                    DialogInputSettings.numberRange("product_size", "qsh_dialog.dialog.label.product.size", String.valueOf(1), "<product:max_stack_size>")
                            .step(String.valueOf(1))
                            .labelFormat("<lang:qsh_dialog.dialog.label.product.size.format>")
                            .build(),
                    DialogInputSettings.text("shop_price", "<lang:qsh_dialog.dialog.label.product.price>")
                            .initial("<product:min_price>")
                            .build(),
                    DialogInputSettings.text("shop_name", "<lang:qsh_dialog.dialog.label.shop.name>")
                            .build(),
                    DialogInputSettings.text("shop_currency", "<lang:qsh_dialog.dialog.label.shop.currency>")
                            .build(),
                    DialogInputSettings.bool("shop_unlimited", "<lang:qsh_dialog.dialog.label.shop.unlimited>")
                            .build()
            ))
            .build(new ConfirmationDialogTypeSettings(
                    DialogActionButtonSettings.builder("<lang:qsh_dialog.dialog.label.create>")
                            .action(DialogAction.customClick(Key.key("qshdialog", "create_shop"), null))
                            .build(),
                    DialogActionButtonSettings.builder("<lang:qsh_dialog.dialog.label.cancel>")
                            .build()
            ));

    private DialogConfigurations() {
    }
}
