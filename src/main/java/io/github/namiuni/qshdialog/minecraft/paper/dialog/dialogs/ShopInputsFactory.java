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

import io.github.namiuni.qshdialog.minecraft.paper.dialog.DialogInputKeys;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.configurations.PrimaryConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeType;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopInputsFactory {

    private final Provider<PrimaryConfiguration> primaryConfig;
    private final TranslationService translations;
    private final QSConfiguration qsConfig;
    private final EconomyService economyService;

    @Inject
    ShopInputsFactory(
            final Provider<PrimaryConfiguration> primaryConfig,
            final TranslationService translations,
            final QSConfiguration qsConfig,
            final EconomyService economyService
    ) {
        this.primaryConfig = primaryConfig;
        this.translations = translations;
        this.qsConfig = qsConfig;
        this.economyService = economyService;
    }

    public List<DialogInput> createInputs(final UserSession user, final ShopComponent shopComponent, final TagResolver placeholders) {
        final boolean isStaff = shopComponent.isStaff(user.uuid());

        return this.primaryConfig.get().modificationDialogInputs().stream()
                .map(inputType -> switch (inputType) {
                    case NAME -> {
                        if (user.hasPermission(QSPermissions.SHOP_NAMING_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_NAMING)) {
                            final Component label = ShopInputsFactory.this.translations.shopCreateInputName(user, placeholders);
                            yield DialogInput.text(DialogInputKeys.SHOP_NAME, label)
                                    .initial(Objects.requireNonNullElse(shopComponent.name(), ""))
                                    .maxLength(ShopInputsFactory.this.qsConfig.shopNameMaxLength())
                                    .build();
                        }
                        yield null;
                    }
                    case TRADE_TYPE -> {
                        final List<TradeType> modes = new ArrayList<>();
                        if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING))
                            modes.add(TradeType.SELLING);
                        if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING))
                            modes.add(TradeType.BUYING);
                        if (!modes.isEmpty()) {
                            final Component label = ShopInputsFactory.this.translations.shopCreateInputTradeType(user, placeholders);
                            final var entries = modes.stream()
                                    .map(tradeType -> {
                                        final Component optionLabel = switch (tradeType) {
                                            case SELLING -> ShopInputsFactory.this.translations.shopCreateInputTradeTypeSelling(user);
                                            case BUYING -> ShopInputsFactory.this.translations.shopCreateInputTradeTypeBuying(user);
                                        };

                                        return SingleOptionDialogInput.OptionEntry.create(tradeType.name(), optionLabel, tradeType == shopComponent.tradeType());
                                    })
                                    .toList();
                            yield DialogInput.singleOption(DialogInputKeys.SHOP_TRADE_TYPE, label, entries)
                                    .build();
                        }
                        yield null;
                    }
                    case CURRENCY -> {
                        final String currency = shopComponent.currency();
                        if (currency != null && this.economyService.supportsMultiCurrency()) {
                            if (user.hasPermission(QSPermissions.SHOP_CURRENCY_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_CURRENCY)) {
                                final Component label = ShopInputsFactory.this.translations.shopCreateInputCurrency(user, placeholders);
                                yield DialogInput.text(DialogInputKeys.SHOP_CURRENCY, label)
                                        .initial(currency)
                                        .build();
                            }
                        }
                        yield null;
                    }
                    case PRODUCT_QUANTITY -> {
                        if (this.qsConfig.supportsUnitTransaction()) {
                            if (isStaff && user.hasPermission(QSPermissions.SHOP_PRODUCT_QUANTITY)) {
                                final Component label = ShopInputsFactory.this.translations.shopCreateInputQuantity(user, placeholders);
                                final String format = ShopInputsFactory.this.translations.shopCreateInputQuantityFormat(user, placeholders);
                                final int maxStackSize = Objects.requireNonNullElse(
                                        shopComponent.product().getData(DataComponentTypes.MAX_STACK_SIZE),
                                        shopComponent.product().getMaxStackSize()
                                );
                                yield DialogInput.numberRange(DialogInputKeys.SHOP_QUANTITY, label, 1.0f, maxStackSize)
                                        .step(1.0f)
                                        .initial((float) shopComponent.product().getAmount())
                                        .labelFormat(format)
                                        .build();
                            }
                        }
                        yield null;
                    }
                    case PRICE -> {
                        if (user.hasPermission(QSPermissions.SHOP_PRICE_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_PRICE)) {
                            final Component label = ShopInputsFactory.this.translations.shopCreateInputPrice(user, placeholders);
                            yield DialogInput.text(DialogInputKeys.SHOP_PRICE, label)
                                    .initial(shopComponent.price().toPlainString())
                                    .build();
                        }
                        yield null;
                    }
                    case STATUS -> {
                        if (user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS)) {
                            final Component label = ShopInputsFactory.this.translations.shopCreateInputStatus(user, placeholders);
                            yield DialogInput.bool(DialogInputKeys.SHOP_AVAILABLE, label)
                                    .initial(shopComponent.status())
                                    .build();
                        }
                        yield null;
                    }
                    case DISPLAY -> {
                        if (user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY)) {
                            final Component label = ShopInputsFactory.this.translations.shopCreateInputDisplay(user, placeholders);
                            yield DialogInput.bool(DialogInputKeys.SHOP_DISPLAY_VISIBLE, label)
                                    .initial(shopComponent.displayVisible())
                                    .build();
                        }
                        yield null;
                    }
                    case STOCK -> {
                        if (user.hasPermission(QSPermissions.SHOP_INFINITE_STOCK)) {
                            final Component label = ShopInputsFactory.this.translations.shopCreateInputUnlimitedStock(user, placeholders);
                            yield DialogInput.bool(DialogInputKeys.SHOP_INFINITE_STOCK, label)
                                    .initial(shopComponent.infiniteStock())
                                    .build();
                        }
                        yield null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
