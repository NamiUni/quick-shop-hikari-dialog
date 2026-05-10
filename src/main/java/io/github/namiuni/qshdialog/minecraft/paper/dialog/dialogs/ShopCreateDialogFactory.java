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

import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopInputs;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.callbacks.ShopCreateCallbackFactory;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.configurations.PrimaryConfiguration;
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
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.util.List;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopCreateDialogFactory {

    private final Provider<PrimaryConfiguration> primaryConfig;
    private final TranslationService translations;
    private final ShopInputs shopInputs;
    private final QSPlaceholders qsPlaceholders;
    private final ShopCreateCallbackFactory callbackFactory;

    @Inject
    ShopCreateDialogFactory(
            final Provider<PrimaryConfiguration> primaryConfig,
            final TranslationService translations,
            final ShopInputs shopInputs,
            final QSPlaceholders qsPlaceholders,
            final ShopCreateCallbackFactory callbackFactory
    ) {
        this.primaryConfig = primaryConfig;
        this.translations = translations;
        this.shopInputs = shopInputs;
        this.qsPlaceholders = qsPlaceholders;
        this.callbackFactory = callbackFactory;
    }

    public DialogLike createDialog(final UserSession user, final ShopBlock shop) {
        final TagResolver placeholders = TagResolver.resolver(this.qsPlaceholders.shopPlaceholder(shop));
        return Dialog.create(builder -> builder.empty()
                .base(this.createBase(user, shop, placeholders))
                .type(this.createType(user, shop, placeholders)));
    }

    private DialogBase createBase(final UserSession user, final ShopBlock shop, final TagResolver placeholders) {
        final ShopComponent component = shop.component();
        return DialogBase.builder(this.translations.shopCreationDialogTitle(user, placeholders))
                .body(List.of(DialogBody.item(component.product().asOne())
                        .description(DialogBody.plainMessage(this.translations.shopCreationDialogDescription(user, placeholders)))
                        .build()))
                .inputs(this.shopInputs.createShopCreateInputs(user, shop, placeholders, this.primaryConfig.get().dialog().shopCreateInputs()))
                .build();
    }

    private DialogType createType(
            final UserSession user,
            final ShopBlock shop,
            final TagResolver placeholders
    ) {
        final ActionButton applyButton = ActionButton.builder(this.translations.shopCreationDialogConfirm(user, placeholders))
                .action(this.callbackFactory.createAction(user, shop))
                .build();
        final ActionButton cancelButton = ActionButton.builder(this.translations.shopCreationDialogCancel(user, placeholders))
                .build();
        return DialogType.confirmation(applyButton, cancelButton);
    }
}
