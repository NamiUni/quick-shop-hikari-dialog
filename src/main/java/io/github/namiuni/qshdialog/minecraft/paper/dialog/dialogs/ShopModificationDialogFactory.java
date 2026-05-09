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
package io.github.namiuni.qshdialog.minecraft.paper.dialog.dialogs;

import io.github.namiuni.qshdialog.minecraft.paper.dialog.callbacks.ShopModificationCallbackFactory;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPlaceholders;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopModificationDialogFactory {

    private final TranslationService translations;
    private final ShopInputsFactory shopInputsFactory;
    private final QSPlaceholders qsPlaceholders;
    private final ShopModificationCallbackFactory callbackFactory;

    @Inject
    ShopModificationDialogFactory(
            final TranslationService translations,
            final ShopInputsFactory shopInputsFactory,
            final QSPlaceholders qsPlaceholders,
            final ShopModificationCallbackFactory callbackFactory
    ) {
        this.translations = translations;
        this.shopInputsFactory = shopInputsFactory;
        this.qsPlaceholders = qsPlaceholders;
        this.callbackFactory = callbackFactory;
    }

    public DialogLike createDialog(final UserSession user, final ShopBlock shop) {
        final TagResolver placeholders = TagResolver.resolver(this.qsPlaceholders.shopPlaceholder(shop));
        return Dialog.create(db -> db.empty()
                .base(this.createBase(user, shop, placeholders))
                .type(this.createType(user, shop, placeholders)));
    }

    private DialogBase createBase(final UserSession user, final ShopBlock shop, final TagResolver placeholders) {
        final ShopComponent shopComponent = shop.component();
        final Component title = this.translations.shopEditDialogTitle(user, placeholders);
        final DialogBody body = DialogBody.item(shopComponent.product().asOne())
                .description(DialogBody.plainMessage(this.translations.shopEditDialogDescription(user, placeholders)))
                .build();
        return DialogBase.builder(title)
                .body(List.of(body))
                .inputs(this.shopInputsFactory.createInputs(user, shopComponent, placeholders))
                .build();
    }

    private DialogType createType(
            final UserSession user,
            final ShopBlock shop,
            final TagResolver placeholders
    ) {
        final ActionButton applyButton = ActionButton.builder(this.translations.shopCreateDialogConfirm(user, placeholders))
                .action(this.callbackFactory.createAction(user, shop))
                .build();
        final ActionButton cancelButton = ActionButton.builder(this.translations.shopCreateDialogCancel(user, placeholders))
                .build();
        return DialogType.confirmation(applyButton, cancelButton);
    }
}
