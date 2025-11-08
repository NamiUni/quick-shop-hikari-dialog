/*
 * quick-shop-hikari-dialog
 *
 * Copyright (c) 2025. Namiu (うにたろう)
 *                     Contributors []
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.namiuni.qshdialog.shop.dialog;

import io.github.namiuni.qshdialog.configuration.ConfigHolder;
import io.github.namiuni.qshdialog.configuration.PrimaryConfig;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopCreationDialogFactory {

    private static final DialogInput SHOP_TYPE_TOGGLE = DialogInput
            .singleOption(
                    "shop_type",
                    TranslationMessages.shopCreationTypeLabel(),
                    List.of(
                            SingleOptionDialogInput.OptionEntry.create(
                                    "shop_type_sell",
                                    TranslationMessages.shopCreationTypeSell(),
                                    true
                            ),
                            SingleOptionDialogInput.OptionEntry.create(
                                    "shop_type_buy",
                                    TranslationMessages.shopCreationTypeBuy(),
                                    false
                            )
                    )
            )
            .build();
    private static final DialogType SHOP_CREATION_CONFIRMATION = DialogType.confirmation(
            ActionButton.builder(TranslationMessages.shopCreationConfirmationCreate()).build(),
            ActionButton.builder(TranslationMessages.shopCreationConfirmationCancel()).build()
    );

    private final ConfigHolder<PrimaryConfig> config;

    public ShopCreationDialogFactory(final ConfigHolder<PrimaryConfig> config) {
        this.config = config;
    }

    public Dialog create(final ItemStack product) {
        final DialogBase dialogBase = DialogBase.builder(this.title())
                .body(this.body(product))
                .inputs(this.inputs(product))
                .build();

        return Dialog.create(builder -> builder
                .empty()
                .base(dialogBase)
                .type(SHOP_CREATION_CONFIRMATION)
        );
    }

    private Component title() {
        return TranslationMessages.shopCreationTitle();
    }

    private List<? extends DialogBody> body(final ItemStack product) {
        final DialogBody body = DialogBody.item(product.asOne())
                .description(DialogBody.plainMessage(TranslationMessages.shopCreationDescription()))
                .build();

        return List.of(body);
    }

    private List<? extends DialogInput> inputs(final ItemStack product) {
        final DialogInput price = DialogInput.text("product_price", TranslationMessages.shopCreationPriceLabel())
                .initial("100") // TODO: price-restriction.yml
                .build();

        return List.of(
                price,
                SHOP_TYPE_TOGGLE
        );
    }
}
