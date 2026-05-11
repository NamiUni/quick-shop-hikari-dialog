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
package io.github.namiuni.qshdialog.minecraft.paper.dialog;

import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeType;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopInputs {

    private enum Context {
        CREATE,
        EDIT,
    }

    private final TranslationService translations;
    private final QSConfiguration qsConfig;
    private final EconomyService economyService;

    @Inject
    ShopInputs(
            final TranslationService translations,
            final QSConfiguration qsConfig,
            final EconomyService economyService
    ) {
        this.translations = translations;
        this.qsConfig = qsConfig;
        this.economyService = economyService;
    }

    public List<DialogInput> createShopCreateInputs(
            final UserSession user,
            final ShopBlock shop,
            final TagResolver placeholders,
            final List<ShopInputType> inputTypes
    ) {
        return this.build(user, shop, placeholders, inputTypes, Context.CREATE);
    }

    public List<DialogInput> createShopEditInputs(
            final UserSession user,
            final ShopBlock shop,
            final TagResolver placeholders,
            final List<ShopInputType> inputTypes
    ) {
        return this.build(user, shop, placeholders, inputTypes, Context.EDIT);
    }

    private List<DialogInput> build(
            final UserSession user,
            final ShopBlock shop,
            final TagResolver placeholders,
            final List<ShopInputType> inputTypes,
            final Context context
    ) {
        final ShopComponent component = shop.component();
        final boolean isStaff = context == Context.EDIT && component.isStaff(user.uuid());
        final List<DialogInput> result = new ArrayList<>(inputTypes.size());

        for (final ShopInputType type : inputTypes) {
            final DialogInput input = switch (type) {
                case SHOP_NAME -> this.shopName(user, component, placeholders, context, isStaff);
                case TRADE_TYPE -> this.tradeType(user, component, placeholders, context, isStaff);
                case CURRENCY -> this.currency(user, component, placeholders, context, isStaff);
                case UNIT -> this.unit(user, component, placeholders, context, isStaff);
                case PRICE -> this.price(user, component, placeholders, context, isStaff);
                case STATUS -> this.status(user, component, placeholders, context, isStaff);
                case DISPLAY -> this.display(user, component, placeholders, context, isStaff);
                case UNLIMITED_STOCK -> this.unlimitedStock(user, component, placeholders, context);
            };
            if (input != null) {
                result.add(input);
            }
        }
        return List.copyOf(result);
    }

    private @Nullable DialogInput shopName(
            final UserSession user,
            final ShopComponent component,
            final TagResolver placeholders,
            final Context context,
            final boolean isStaff
    ) {
        final boolean permitted = switch (context) {
            case CREATE -> user.hasPermission(QSPermissions.SHOP_NAMING);
            case EDIT -> user.hasPermission(QSPermissions.SHOP_NAMING_OTHER)
                    || isStaff && user.hasPermission(QSPermissions.SHOP_NAMING);
        };
        if (!permitted) {
            return null;
        }
        final Component label = switch (context) {
            case CREATE -> this.translations.shopCreationInputName(user, placeholders);
            case EDIT -> this.translations.shopModificationInputName(user, placeholders);
        };
        return DialogInput.text(DialogInputKeys.SHOP_NAME, label)
                .initial(Objects.requireNonNullElse(component.shopName(), ""))
                .maxLength(this.qsConfig.shopNameMaxLength())
                .build();
    }

    private @Nullable DialogInput tradeType(
            final UserSession user,
            final ShopComponent component,
            final TagResolver placeholders,
            final Context context,
            final boolean isStaff
    ) {
        final List<TradeType> modes = new ArrayList<>();
        switch (context) {
            case CREATE -> {
                if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING)) {
                    modes.add(TradeType.SELLING);
                }
                if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING)) {
                    modes.add(TradeType.BUYING);
                }
            }
            case EDIT -> {
                if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING_OTHER)
                        || isStaff && user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING)) {
                    modes.add(TradeType.SELLING);
                }
                if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING_OTHER)
                        || isStaff && user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING)) {
                    modes.add(TradeType.BUYING);
                }
            }
        }
        if (modes.isEmpty()) {
            return null;
        }

        final Component label = switch (context) {
            case CREATE -> this.translations.shopCreationInputTradeType(user, placeholders);
            case EDIT -> this.translations.shopModificationInputTradeType(user, placeholders);
        };
        final List<SingleOptionDialogInput.OptionEntry> entries = modes.stream()
                .map(tradeType -> {
                    final Component optionLabel = switch (context) {
                        case CREATE -> switch (tradeType) {
                            case SELLING -> this.translations.shopCreationInputTradeTypeSelling(user);
                            case BUYING -> this.translations.shopCreationInputTradeTypeBuying(user);
                        };
                        case EDIT -> switch (tradeType) {
                            case SELLING -> this.translations.shopModificationInputTradeTypeSelling(user);
                            case BUYING -> this.translations.shopModificationInputTradeTypeBuying(user);
                        };
                    };
                    return SingleOptionDialogInput.OptionEntry.create(
                            tradeType.name(), optionLabel, tradeType == component.tradeType());
                })
                .toList();
        return DialogInput.singleOption(DialogInputKeys.SHOP_TRADE_TYPE, label, entries).build();
    }

    private @Nullable DialogInput currency(
            final UserSession user,
            final ShopComponent component,
            final TagResolver placeholders,
            final Context context,
            final boolean isStaff
    ) {
        if (component.currency() == null || !this.economyService.supportsMultiCurrency()) {
            return null;
        }
        final boolean permitted = switch (context) {
            case CREATE -> user.hasPermission(QSPermissions.SHOP_CURRENCY);
            case EDIT -> user.hasPermission(QSPermissions.SHOP_CURRENCY_OTHER)
                    || isStaff && user.hasPermission(QSPermissions.SHOP_CURRENCY);
        };
        if (!permitted) {
            return null;
        }
        final Component label = switch (context) {
            case CREATE -> this.translations.shopCreationInputCurrency(user, placeholders);
            case EDIT -> this.translations.shopModificationInputCurrency(user, placeholders);
        };
        return DialogInput.text(DialogInputKeys.SHOP_CURRENCY, label)
                .initial(component.currency())
                .build();
    }

    private @Nullable DialogInput unit(
            final UserSession user,
            final ShopComponent component,
            final TagResolver placeholders,
            final Context context,
            final boolean isStaff
    ) {
        if (!this.qsConfig.supportsUnitTransaction()) {
            return null;
        }
        final boolean permitted = switch (context) {
            case CREATE -> user.hasPermission(QSPermissions.SHOP_UNIT);
            case EDIT -> isStaff && user.hasPermission(QSPermissions.SHOP_UNIT);
        };
        if (!permitted) {
            return null;
        }
        final Component label = switch (context) {
            case CREATE -> this.translations.shopCreationInputQuantity(user, placeholders);
            case EDIT -> this.translations.shopModificationInputQuantity(user, placeholders);
        };
        final String format = switch (context) {
            case CREATE -> this.translations.shopCreationInputQuantityFormat(user, placeholders);
            case EDIT -> this.translations.shopModificationInputQuantityFormat(user, placeholders);
        };
        final int maxStackSize = Objects.requireNonNullElse(
                component.product().getData(DataComponentTypes.MAX_STACK_SIZE),
                component.product().getMaxStackSize()
        );
        return DialogInput.numberRange(DialogInputKeys.SHOP_UNIT, label, 1.0f, maxStackSize)
                .step(1.0f)
                .initial((float) component.product().getAmount())
                .labelFormat(format)
                .build();
    }

    private @Nullable DialogInput price(
            final UserSession user,
            final ShopComponent component,
            final TagResolver placeholders,
            final Context context,
            final boolean isStaff
    ) {
        final boolean permitted = switch (context) {
            case CREATE -> user.hasPermission(QSPermissions.SHOP_PRICE);
            case EDIT -> user.hasPermission(QSPermissions.SHOP_PRICE_OTHER)
                    || isStaff && user.hasPermission(QSPermissions.SHOP_PRICE);
        };
        if (!permitted) {
            return null;
        }
        final Component label = switch (context) {
            case CREATE -> this.translations.shopCreationInputPrice(user, placeholders);
            case EDIT -> this.translations.shopModificationInputPrice(user, placeholders);
        };
        final BigDecimal price = component.price();
        return DialogInput.text(DialogInputKeys.SHOP_PRICE, label)
                .initial(price.toPlainString())
                .build();
    }

    private @Nullable DialogInput status(
            final UserSession user,
            final ShopComponent component,
            final TagResolver placeholders,
            final Context context,
            final boolean isStaff
    ) {
        final boolean permitted = switch (context) {
            case CREATE -> user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS);
            case EDIT -> user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS_OTHER)
                    || isStaff && user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS);
        };
        if (!permitted) {
            return null;
        }
        final Component label = switch (context) {
            case CREATE -> this.translations.shopCreationInputStatus(user, placeholders);
            case EDIT -> this.translations.shopModificationInputStatus(user, placeholders);
        };
        return DialogInput.bool(DialogInputKeys.SHOP_STATUS, label)
                .initial(component.available())
                .build();
    }

    private @Nullable DialogInput display(
            final UserSession user,
            final ShopComponent component,
            final TagResolver placeholders,
            final Context context,
            final boolean isStaff
    ) {
        final boolean permitted = switch (context) {
            case CREATE -> user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY);
            case EDIT -> user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY_OTHER)
                    || isStaff && user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY);
        };
        if (!permitted) {
            return null;
        }
        final Component label = switch (context) {
            case CREATE -> this.translations.shopCreationInputDisplay(user, placeholders);
            case EDIT -> this.translations.shopModificationInputDisplay(user, placeholders);
        };
        return DialogInput.bool(DialogInputKeys.SHOP_DISPLAY, label)
                .initial(component.displayVisible())
                .build();
    }

    private @Nullable DialogInput unlimitedStock(
            final UserSession user,
            final ShopComponent component,
            final TagResolver placeholders,
            final Context context
    ) {
        if (!user.hasPermission(QSPermissions.SHOP_UNLIMITED_STOCK)) {
            return null;
        }
        final Component label = switch (context) {
            case CREATE -> this.translations.shopCreationInputUnlimitedStock(user, placeholders);
            case EDIT -> this.translations.shopModificationInputUnlimitedStock(user, placeholders);
        };
        return DialogInput.bool(DialogInputKeys.SHOP_UNLIMITED_STOCK, label)
                .initial(component.infiniteStock())
                .build();
    }
}
