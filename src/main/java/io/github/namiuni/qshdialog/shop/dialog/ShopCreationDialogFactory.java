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

import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopCreationDialogFactory {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;

    public ShopCreationDialogFactory(final ConfigurationHolder<PrimaryConfiguration> configHolder) {
        this.configHolder = configHolder;
    }

    public Dialog create(final Block block, final QSHUser qshUser) {
        final DialogBase dialogBase = DialogBase.builder(this.title(block, qshUser))
                .body(this.body(block, qshUser))
                .inputs(this.inputs(block, qshUser))
                .build();

        final DialogType dialogType = DialogType.confirmation(
                ActionButton.builder(TranslationMessages.shopCreationConfirmationCreate(qshUser)).build(),
                ActionButton.builder(TranslationMessages.shopCreationConfirmationCancel(qshUser)).build()
        );

        return Dialog.create(builder -> builder
                .empty()
                .base(dialogBase)
                .type(dialogType)
        );
    }

    private Component title(final Block block, final QSHUser qshUser) {
        return TranslationMessages.shopCreationTitle(qshUser);
    }

    private List<? extends DialogBody> body(final Block block, final QSHUser qshUser) {
        final DialogBody body = DialogBody.item(qshUser.mainHandItem().asOne())
                .description(DialogBody.plainMessage(TranslationMessages.shopCreationDescription(qshUser)))
                .build();

        return List.of(body);
    }

    private List<? extends DialogInput> inputs(final Block block, final QSHUser qshUser) {
        final DialogInput price = DialogInput.text("product_price", TranslationMessages.shopCreationPriceLabel(qshUser))
                .initial("100") // TODO: price-restriction.yml
                .build();

        final DialogInput shopTypeToggle = DialogInput
                .singleOption(
                        "shop_type",
                        TranslationMessages.shopCreationTypeLabel(qshUser),
                        List.of(
                                SingleOptionDialogInput.OptionEntry.create(
                                        "shop_type_sell",
                                        TranslationMessages.shopCreationTypeSell(qshUser),
                                        true
                                ),
                                SingleOptionDialogInput.OptionEntry.create(
                                        "shop_type_buy",
                                        TranslationMessages.shopCreationTypeBuy(qshUser),
                                        false
                                )
                        )
                )
                .build();

        return List.of(
                price,
                shopTypeToggle
        );
    }
}
