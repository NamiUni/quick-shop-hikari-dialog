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

import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradeInputs;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.callbacks.TradePurchaseCallbackFactory;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPlaceholders;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeQuantityCalculator;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeQuantityFailure;
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
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class TradePurchaseDialogFactory {

    private final TranslationService translations;
    private final TradeInputs tradeInputs;
    private final QSPlaceholders qsPlaceholders;
    private final TradeQuantityCalculator quantityCalculator;
    private final TradePurchaseCallbackFactory callbackFactory;

    @Inject
    TradePurchaseDialogFactory(
            final TranslationService translations,
            final TradeInputs tradeInputs,
            final QSPlaceholders qsPlaceholders,
            final TradeQuantityCalculator quantityCalculator,
            final TradePurchaseCallbackFactory callbackFactory
    ) {
        this.translations = translations;
        this.tradeInputs = tradeInputs;
        this.qsPlaceholders = qsPlaceholders;
        this.quantityCalculator = quantityCalculator;
        this.callbackFactory = callbackFactory;
    }

    public @Nullable DialogLike createDialog(final UserSession user, final ShopBlock shop) {
        final TagResolver placeholders = TagResolver.resolver(this.qsPlaceholders.shopPlaceholder(shop));

        final Result<Integer, TradeQuantityFailure> quantityResult = this.quantityCalculator.purchasableQuantity(user, shop);
        if (quantityResult instanceof Result.Error<Integer, TradeQuantityFailure>(final TradeQuantityFailure failure)) {
            final Component message = switch (failure) {
                case CUSTOMER_INVENTORY_FULL -> this.translations.tradeFailedCustomerInventoryFull(user, 0, placeholders);
                case SHOP_OUT_OF_STOCK -> this.translations.tradeFailedShopOutOfStock(user, placeholders);
                case CUSTOMER_INSUFFICIENT_FUNDS -> this.translations.tradeFailedCustomerInsufficientFunds(user, placeholders);
                default -> null;
            };
            if (message != null) {
                user.sendMessage(message);
            }
            return null;
        }

        final int maxQuantity = ((Result.Success<Integer, TradeQuantityFailure>) quantityResult).result();

        final DialogBase base = DialogBase.builder(this.translations.dialogTradePurchaseTitle(user, placeholders))
                .body(List.of(DialogBody.item(shop.component().product())
                        .description(DialogBody.plainMessage(this.translations.dialogTradePurchaseDescription(user, placeholders)))
                        .build()))
                .inputs(List.of(this.tradeInputs.tradeQuantity(maxQuantity, 1, user, placeholders)))
                .build();

        final ActionButton confirmButton = ActionButton.builder(this.translations.dialogTradePurchaseConfirmButton(user, placeholders))
                .action(this.callbackFactory.createAction(user, shop))
                .build();
        final ActionButton cancelButton = ActionButton.builder(this.translations.dialogTradePurchaseCancelButton(user, placeholders))
                .build();

        return Dialog.create(builder -> builder.empty()
                .base(base)
                .type(DialogType.confirmation(confirmButton, cancelButton)));
    }
}
