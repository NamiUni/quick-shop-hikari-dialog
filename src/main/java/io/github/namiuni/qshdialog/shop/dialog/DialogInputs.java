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
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.shop.ShopDisplay;
import io.github.namiuni.qshdialog.shop.ShopMode;
import io.github.namiuni.qshdialog.shop.ShopStatus;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
final class DialogInputs {

    private DialogInputs() {
    }

    public static Result<DialogInput, Component> shopMode(final QSHUser qshUser, final TagResolver shopTags, final boolean isOwner, final ShopMode initial) {
        final Component label = TranslationMessages.shopTradeTypeLabel(qshUser, shopTags);
        final List<SingleOptionDialogInput.OptionEntry> types = new ArrayList<>();

        if (isOwner && qshUser.hasPermission("quickshop.create.sell") || qshUser.hasPermission("quickshop.other.buy")) {
            final Component selling = GlobalTranslator.render(ShopMode.SELLING.displayName(), qshUser.locale());
            final var option = SingleOptionDialogInput.OptionEntry.create("SELLING", selling, initial == ShopMode.SELLING);
            types.add(option);
        }

        if (isOwner && qshUser.hasPermission("quickshop.create.buy") || qshUser.hasPermission("quickshop.other.sell")) {
            final Component buying = GlobalTranslator.render(ShopMode.BUYING.displayName(), qshUser.locale());
            final var option = SingleOptionDialogInput.OptionEntry.create("BUYING", buying, initial == ShopMode.BUYING);
            types.add(option);
        }

        if (!types.isEmpty()) {
            final DialogInput input = DialogInput.singleOption("shop_mode", label, types).build();
            return Result.success(input);
        } else {
            return Result.error(TranslationMessages.shopTradeTypeNoPermissionError(qshUser));
        }
    }

    public static DialogInput shopStatus(final QSHUser user, final TagResolver shopTags, final ShopStatus initial) {
        final Component label = TranslationMessages.shopToggleAvailableLabel(user, shopTags);

        final Component available = GlobalTranslator.render(ShopStatus.AVAILABLE.displayName(), user.locale());
        final var availableOption = SingleOptionDialogInput.OptionEntry.create("AVAILABLE", available, initial == ShopStatus.AVAILABLE);

        final Component unavailable = GlobalTranslator.render(ShopStatus.UNAVAILABLE.displayName(), user.locale());
        final var unavailableOption = SingleOptionDialogInput.OptionEntry.create("UNAVAILABLE", unavailable, initial == ShopStatus.UNAVAILABLE);

        return DialogInput.singleOption("shop_status", label, List.of(availableOption, unavailableOption)).build();
    }

    public static DialogInput shopDisplay(final QSHUser qshUser, final TagResolver shopTags, final ShopDisplay initial) {
        final Component label = TranslationMessages.shopShowDisplayLabel(qshUser, shopTags);

        final Component show = GlobalTranslator.render(ShopDisplay.SHOW.displayName(), qshUser.locale());
        final var showOption = SingleOptionDialogInput.OptionEntry.create("SHOW", show, initial == ShopDisplay.SHOW);

        final Component hide = GlobalTranslator.render(ShopDisplay.HIDE.displayName(), qshUser.locale());
        final var hideOption = SingleOptionDialogInput.OptionEntry.create("HIDE", hide, initial == ShopDisplay.HIDE);

        return DialogInput.singleOption("shop_display", label, List.of(showOption, hideOption)).build();
    }

    public static DialogInput productBundleSize(final QSHUser owner, final TagResolver shopTags, final int initial, final int max) {
        final Component label = TranslationMessages.shopProductBundleSize(owner, shopTags);
        final String format = TranslationMessages.shopProductBundleFormat(owner, shopTags);

        return DialogInput.numberRange("product_size", label, 1.0f, max)
                .step(1.0f)
                .initial((float) initial)
                .labelFormat(format)
                .build();
    }

    public static DialogInput productPrice(final QSHUser qshUser, final TagResolver shopTags, final BigDecimal initial, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final Component productPrice = TranslationMessages.productPriceLabel(qshUser, shopTags, minPrice, maxPrice);

        return DialogInput.text("product_price", productPrice)
                .initial(initial.toPlainString())
                .build();
    }

    public static DialogInput shopName(final QSHUser qshUser, final TagResolver shopTags, final @Nullable String initial) {
        final double namingCost = QuickShopUtil.namingCost(qshUser);
        final Component label = TranslationMessages.shopNameLabel(qshUser, shopTags, namingCost);

        return DialogInput.text("shop_name", label)
                .initial(Optional.ofNullable(initial).orElse(""))
                .maxLength(QuickShopUtil.maxNameLength())
                .build();
    }

    public static DialogInput shopCurrency(final QSHUser qshUser, final TagResolver shopTags, final @Nullable String initial) {
        final Component label = TranslationMessages.shopCurrencyLabel(qshUser, shopTags);

        return DialogInput.text("shop_currency", label)
                .initial(Optional.ofNullable(initial).orElse(""))
                .build();
    }

    public static DialogInput shopUnlimitedStock(final QSHUser qshUser, final TagResolver shopTags, final boolean initial) {
        final Component label = TranslationMessages.shopUnlimitedStockLabel(qshUser, shopTags);

        return DialogInput.bool("unlimited_stock", label)
                .initial(initial)
                .build();
    }

    public static Result<DialogInput, Component> purchaseQuantity(final QSHUser customer, final Shop shop, final TagResolver shopTags) {
        final Result<Integer, Component> availableQuantity = QuickShopUtil.availableQuantityForPurchase(customer, shop);

        return switch (availableQuantity) {
            case Result.Success<Integer, Component>(Integer result) -> {
                final Component label = TranslationMessages.shopQuantityLabel(customer, shopTags);
                final float start = 1.0f;
                final float end = result.floatValue();

                final DialogInput input = DialogInput.numberRange("trade_quantity", label, start, end)
                        .step(1.0f)
                        .initial(1.0f)
                        .labelFormat(TranslationMessages.shopQuantityFormat(customer, shopTags))
                        .build();
                yield Result.success(input);
            }
            case Result.Error<Integer, Component>(Component errorMessage) -> Result.error(errorMessage);
        };
    }

    public static Result<DialogInput, Component> saleQuantity(final QSHUser customer, final Shop shop, final TagResolver shopTags) {
        final Result<Integer, Component> availableQuantity = QuickShopUtil.availableQuantityForSale(customer, shop);

        return switch (availableQuantity) {
            case Result.Success<Integer, Component>(Integer result) -> {
                final Component label = TranslationMessages.shopQuantityLabel(customer, shopTags);
                final float start = 1.0f;
                final float end = result.floatValue();

                final DialogInput input = DialogInput.numberRange("trade_quantity", label, start, end)
                        .step(1.0f)
                        .initial(1.0f)
                        .labelFormat(TranslationMessages.shopQuantityFormat(customer, shopTags))
                        .build();
                yield Result.success(input);
            }
            case Result.Error<Integer, Component>(Component errorMessage) -> Result.error(errorMessage);
        };
    }
}
