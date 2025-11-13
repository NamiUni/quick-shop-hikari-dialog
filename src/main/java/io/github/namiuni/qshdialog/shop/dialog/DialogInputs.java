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

import io.github.namiuni.qshdialog.shop.policy.ShopCreationContext;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
final class DialogInputs {

    private DialogInputs() {
    }

    public static DialogInput tradeType(final QSHUser qshUser) {
        final Component label = TranslationMessages.shopTradeTypeLabel(qshUser);
        final List<SingleOptionDialogInput.OptionEntry> types = new ArrayList<>();

        if (qshUser.hasPermission("quickshop.create.sell")) {
            final Component sell = TranslationMessages.shopTradeTypeSell(qshUser);
            final var option = SingleOptionDialogInput.OptionEntry.create("SELL", sell, true);
            types.add(option);
        }

        if (qshUser.hasPermission("quickshop.create.buy")) {
            final Component buy = TranslationMessages.shopTradeTypeBuy(qshUser);
            final var option = SingleOptionDialogInput.OptionEntry.create("BUY", buy, false);
            types.add(option);
        }

        return DialogInput.singleOption("trade_type", label, types).build();
    }

    public static DialogInput productBundleSize(final ShopCreationContext context) {
        final Component label = TranslationMessages.shopProductBundleSize(context.owner());
        final String format = TranslationMessages.shopProductBundleFormat(context.owner());

        return DialogInput.numberRange("product_size", label, 0.0f, 64.0f)
                .step(1.0f)
                .initial((float) context.product().getAmount())
                .labelFormat(format)
                .build();
    }

    public static DialogInput productPrice(final QSHUser qshUser, final double minPrice, final double maxPrice) {
        final Component productPrice = TranslationMessages.productPriceLabel(qshUser, minPrice, maxPrice);

        return DialogInput.text("product_price", productPrice)
                .initial(String.valueOf(minPrice))
                .build();
    }

    public static DialogInput shopName(final QSHUser qshUser) {
        final double namingCost = QuickShopUtil.namingCost(qshUser);
        final Component label = TranslationMessages.shopNameLabel(qshUser, namingCost);

        return DialogInput.text("shop_name", label)
                .maxLength(QuickShopUtil.maxNameLength())
                .build();
    }

    public static DialogInput shopCurrency(final QSHUser qshUser) {
        final Component label = TranslationMessages.shopCurrencyLabel(qshUser);

        return DialogInput.text("shop_currency", label).build();
    }

    public static DialogInput shopShowDisplay(final QSHUser qshUser) {
        final Component label = TranslationMessages.shopShowDisplayLabel(qshUser);

        return DialogInput.bool("show_display", label)
                .initial(true)
                .build();
    }

    public static DialogInput shopUnlimitedStock(final QSHUser qshUser) {
        final Component label = TranslationMessages.shopUnlimitedStockLabel(qshUser);

        return DialogInput.bool("unlimited_stock", label)
                .initial(false)
                .build();
    }
}
