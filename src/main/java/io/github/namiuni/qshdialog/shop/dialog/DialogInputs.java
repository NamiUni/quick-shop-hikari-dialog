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
import io.github.namiuni.qshdialog.shop.TradeType;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
final class DialogInputs {

    private DialogInputs() {
    }

    public static DialogInput tradeType(final QSHUser qshUser, final TradeType initial) {
        final Component label = TranslationMessages.shopTradeTypeLabel(qshUser);
        final List<SingleOptionDialogInput.OptionEntry> types = new ArrayList<>();

        if (qshUser.hasPermission("quickshop.create.sell")) {
            final Component sell = TranslationMessages.shopTradeTypeSell(qshUser);
            final var option = SingleOptionDialogInput.OptionEntry.create("SELL", sell, initial == TradeType.SELL);
            types.add(option);
        }

        if (qshUser.hasPermission("quickshop.create.buy")) {
            final Component buy = TranslationMessages.shopTradeTypeBuy(qshUser);
            final var option = SingleOptionDialogInput.OptionEntry.create("BUY", buy, initial == TradeType.BUY);
            types.add(option);
        }

        return DialogInput.singleOption("trade_type", label, types).build();
    }

    public static DialogInput productBundleSize(final QSHUser owner, final int initial, final int max) {
        final Component label = TranslationMessages.shopProductBundleSize(owner);
        final String format = TranslationMessages.shopProductBundleFormat(owner);

        return DialogInput.numberRange("product_size", label, 1.0f, max)
                .step(1.0f)
                .initial((float) initial)
                .labelFormat(format)
                .build();
    }

    public static DialogInput productPrice(final QSHUser qshUser, final BigDecimal initial, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final Component productPrice = TranslationMessages.productPriceLabel(qshUser, minPrice, maxPrice);

        return DialogInput.text("product_price", productPrice)
                .initial(initial.toPlainString())
                .build();
    }

    public static DialogInput shopName(final QSHUser qshUser, final @Nullable String initial) {
        final double namingCost = QuickShopUtil.namingCost(qshUser);
        final Component label = TranslationMessages.shopNameLabel(qshUser, namingCost);

        return DialogInput.text("shop_name", label)
                .initial(Optional.ofNullable(initial).orElse(""))
                .maxLength(QuickShopUtil.maxNameLength())
                .build();
    }

    public static DialogInput shopCurrency(final QSHUser qshUser, final @Nullable String initial) {
        final Component label = TranslationMessages.shopCurrencyLabel(qshUser);

        return DialogInput.text("shop_currency", label)
                .initial(Optional.ofNullable(initial).orElse(""))
                .build();
    }

    public static DialogInput shopShowDisplay(final QSHUser qshUser, final boolean initial) {
        final Component label = TranslationMessages.shopShowDisplayLabel(qshUser);

        return DialogInput.bool("show_display", label)
                .initial(initial)
                .build();
    }

    public static DialogInput shopUnlimitedStock(final QSHUser qshUser, final boolean initial) {
        final Component label = TranslationMessages.shopUnlimitedStockLabel(qshUser);

        return DialogInput.bool("unlimited_stock", label)
                .initial(initial)
                .build();
    }

    public static DialogInput buyQuantity(final QSHUser qshUser, final Shop shop) {
        final Component label = TranslationMessages.shopQuantityLabel(qshUser, shop);
        final float start = 1.0f;
        final float end = shop.isUnlimited()
                ? qshUser.quickShopUser().getBukkitPlayer().map(HumanEntity::getInventory).map(Inventory::getStorageContents).stream().flatMap(Stream::of).filter(Objects::nonNull).count()
                : shop.getRemainingStock();
        if (end < start) {
            throw new IllegalArgumentException("Out of stock at the store!");
        }

        return DialogInput.numberRange("trade_quantity", label, start, end)
                .step(1.0f)
                .initial(1.0f)
                .labelFormat(TranslationMessages.shopQuantityFormat(qshUser, shop))
                .build();
    }

    public static Result<DialogInput, Component> saleQuantity(final QSHUser customer, final Shop shop) {
        final Result<Integer, Component> availableQuantity = QuickShopUtil.availableQuantityForSale(customer, shop);

        return switch (availableQuantity) {
            case Result.Success<Integer, Component>(Integer result) -> {
                final Component label = TranslationMessages.shopQuantityLabel(customer, shop);
                final float start = 1.0f;
                final float end = result.floatValue();

                final DialogInput input = DialogInput.numberRange("trade_quantity", label, start, end)
                        .step(1.0f)
                        .initial(1.0f)
                        .labelFormat(TranslationMessages.shopQuantityFormat(customer, shop))
                        .build();
                yield Result.success(input);
            }
            case Result.Error<Integer, Component>(Component errorMessage) -> Result.error(errorMessage);
        };
    }
}
