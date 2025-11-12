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

import com.ghostchu.quickshop.api.shop.Shop;
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ItemPurchaseDialogFactory {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;

    public ItemPurchaseDialogFactory(final ConfigurationHolder<PrimaryConfiguration> configHolder) {
        this.configHolder = configHolder;
    }

    public Dialog create(final Shop shop, final QSHUser qshUser) {
        if (!shop.isSelling()) {
            throw new IllegalArgumentException("Invalid shop type: %s".formatted(shop.shopType().identifier()));
        }

        final DialogBase dialogBase = DialogBase.builder(this.title(shop, qshUser))
                .body(this.body(shop, qshUser))
                .inputs(this.inputs(shop, qshUser))
                .build();

        final DialogType dialogType = DialogType.confirmation(
                ActionButton.builder(TranslationMessages.itemPurchaseConfirmationConfirm(qshUser)).build(),
                ActionButton.builder(TranslationMessages.itemPurchaseConfirmationCancel(qshUser)).build());

        return Dialog.create(builder -> builder
                .empty()
                .base(dialogBase)
                .type(dialogType)
        );
    }

    private Component title(final Shop shop, final QSHUser qshUser) {
        return TranslationMessages.itemPurchaseTitle(qshUser);
    }

    private List<? extends DialogBody> body(final Shop shop, final QSHUser qshUser) {
        final DialogBody body = DialogBody.item(shop.getItem())
                .description(DialogBody.plainMessage(TranslationMessages.itemPurchaseDescription(qshUser)))
                .build();

        return List.of(body);
    }

    private List<? extends DialogInput> inputs(final Shop shop, final QSHUser qshUser) {
        return List.of();
    }
}
