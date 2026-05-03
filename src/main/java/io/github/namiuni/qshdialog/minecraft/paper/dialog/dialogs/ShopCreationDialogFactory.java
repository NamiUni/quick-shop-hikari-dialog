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

import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopInputType;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopInputs;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.callbacks.ShopCreationCallbackFactory;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.configurations.PrimaryConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPlaceholders;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeType;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopCreationDialogFactory {

    private final Provider<PrimaryConfiguration> primaryConfig;
    private final TranslationService translations;
    private final ShopInputs shopInputs;
    private final QSPlaceholders qsPlaceholders;
    private final EconomyService economyService;
    private final QSConfiguration qsConfig;
    private final ShopCreationCallbackFactory callbackFactory;

    @Inject
    ShopCreationDialogFactory(
            final Provider<PrimaryConfiguration> primaryConfig,
            final TranslationService translations,
            final ShopInputs shopInputs,
            final QSPlaceholders qsPlaceholders,
            final EconomyService economyService,
            final QSConfiguration qsConfig,
            final ShopCreationCallbackFactory callbackFactory
    ) {
        this.primaryConfig = primaryConfig;
        this.translations = translations;
        this.shopInputs = shopInputs;
        this.qsPlaceholders = qsPlaceholders;
        this.economyService = economyService;
        this.qsConfig = qsConfig;
        this.callbackFactory = callbackFactory;
    }

    public DialogLike createDialog(final UserSession user, final ShopBlock shop) {
        final TagResolver placeholders = TagResolver.resolver(this.qsPlaceholders.shopPlaceholder(shop));
        return Dialog.create(builder -> builder.empty()
                .base(this.createBase(user, shop, placeholders))
                .type(this.createType(user, shop, placeholders)));
    }

    private DialogBase createBase(final UserSession user, final ShopBlock shop, final TagResolver placeholders) {
        final ShopComponent shopComponent = shop.component();
        final ShopInputs.Builder inputBuilder = this.shopInputs.forCreation(user, placeholders);

        for (final ShopInputType inputType : this.primaryConfig.get().creationDialogInputs()) {
            switch (inputType) {
                case NAME -> {
                    if (user.hasPermission(QSPermissions.SHOP_NAMING)) {
                        inputBuilder.name(shopComponent.name());
                    }
                }
                case TRADE_TYPE -> {
                    final List<TradeType> modes = new ArrayList<>();
                    if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING))
                        modes.add(TradeType.SELLING);
                    if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING))
                        modes.add(TradeType.BUYING);
                    if (!modes.isEmpty()) {
                        inputBuilder.tradeType(modes, shopComponent.tradeType());
                    }
                }
                case CURRENCY -> {
                    final String currency = shop.component().currency();
                    if (currency != null && this.economyService.supportsMultiCurrency()) {
                        if (user.hasPermission(QSPermissions.SHOP_CURRENCY)) {
                            inputBuilder.currency(shopComponent.currency());
                        }
                    }
                }
                case PRODUCT_QUANTITY -> {
                    if (this.qsConfig.supportsUnitTransaction()) {
                        if (user.hasPermission(QSPermissions.SHOP_PRODUCT_QUANTITY)) {
                            final int maxStackSize = Objects.requireNonNullElse(
                                    shopComponent.product().getData(DataComponentTypes.MAX_STACK_SIZE),
                                    shopComponent.product().getMaxStackSize()
                            );
                            inputBuilder.quantity(maxStackSize, shopComponent.product().getAmount());
                        }
                    }
                }
                case PRICE -> {
                    if (user.hasPermission(QSPermissions.SHOP_PRICE)) {
                        inputBuilder.price(shopComponent.price());
                    }
                }
                case STATUS -> {
                    if (user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS)) {
                        inputBuilder.status(shopComponent.available());
                    }
                }
                case DISPLAY -> {
                    if (user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY)) {
                        inputBuilder.display(shopComponent.displayVisible());
                    }
                }
                case STOCK -> {
                    if (user.hasPermission(QSPermissions.SHOP_INFINITE_STOCK)) {
                        inputBuilder.stock(shopComponent.infiniteStock());
                    }
                }
            }
        }

        final Component title = this.translations.dialogCreationTitle(user, placeholders);
        final DialogBody body = DialogBody.item(shopComponent.product().asOne())
                .description(DialogBody.plainMessage(this.translations.dialogCreationDescription(user, placeholders)))
                .build();
        return DialogBase.builder(title)
                .body(List.of(body))
                .inputs(inputBuilder.buildInputs())
                .build();
    }

    private DialogType createType(
            final UserSession user,
            final ShopBlock preparingShop,
            final TagResolver placeholders
    ) {
        final ActionButton applyButton = ActionButton.builder(this.translations.dialogCreationConfirmButton(user, placeholders))
                .action(this.callbackFactory.createAction(user, preparingShop))
                .build();
        final ActionButton cancelButton = ActionButton.builder(this.translations.dialogCreationCancelButton(user, placeholders))
                .build();
        return DialogType.confirmation(applyButton, cancelButton);
    }
}
