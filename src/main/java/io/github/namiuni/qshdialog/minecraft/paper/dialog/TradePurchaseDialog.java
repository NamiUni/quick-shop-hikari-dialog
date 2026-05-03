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

import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPlaceholders;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeFailure;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeQuantityCalculator;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeQuantityFailure;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class TradePurchaseDialog {

    private final TranslationService translations;
    private final TradeInputs tradeInputs;
    private final QSPlaceholders qsPlaceholders;
    private final TradeService tradeService;
    private final TradeQuantityCalculator quantityCalculator;

    @Inject
    TradePurchaseDialog(
            final TranslationService translations,
            final TradeInputs tradeInputs,
            final QSPlaceholders qsPlaceholders,
            final TradeService tradeService,
            final TradeQuantityCalculator quantityCalculator
    ) {
        this.translations = translations;
        this.tradeInputs = tradeInputs;
        this.qsPlaceholders = qsPlaceholders;
        this.tradeService = tradeService;
        this.quantityCalculator = quantityCalculator;
    }

    public @Nullable DialogLike createDialog(final UserSession user, final ShopBlock shop) {
        final TagResolver placeholders = TagResolver.builder()
                .resolver(this.qsPlaceholders.shopPlaceholder(shop))
                .build();

        final Result<Integer, TradeQuantityFailure> quantityResult = this.quantityCalculator.purchasableQuantity(user, shop);

        if (quantityResult instanceof Result.Error<Integer, TradeQuantityFailure>(final TradeQuantityFailure failure)) {
            final Component message = switch (failure) {
                case CUSTOMER_INVENTORY_FULL -> this.translations.tradeFailedCustomerInventoryFull(user, 0, placeholders);
                case SHOP_OUT_OF_STOCK -> this.translations.tradeFailedShopOutOfStock(user, placeholders);
                case CUSTOMER_INSUFFICIENT_FUNDS -> this.translations.tradeFailedCustomerInsufficientFunds(user, placeholders);
                case SHOP_INVENTORY_FULL, SHOP_INSUFFICIENT_FUNDS, CUSTOMER_INSUFFICIENT_ITEMS -> null;
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
                .inputs(List.of(this.tradeInputs.tradeQuantity(maxQuantity, 1, user, placeholders))) // TODO スタック
                .build();

        final var callbackOptions = ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();
        final var confirmButton = ActionButton.builder(this.translations.dialogTradePurchaseConfirmButton(user, placeholders))
                .action(DialogAction.customClick((response, _) -> {
                    final int quantity = Objects.requireNonNull(response.getFloat(DialogInputKeys.TRADE_QUANTITY)).intValue();
                    final Result<Void, TradeFailure> result = this.tradeService.purchase(user, shop, quantity);
                    if (result instanceof Result.Error) {
                        final Component message = this.translations.shopModificationFailedShopNotFound(user);
                        user.sendMessage(message);
                    }
                }, callbackOptions))
                .build();
        final var cancelButton = ActionButton.builder(this.translations.dialogTradePurchaseCancelButton(user, placeholders))
                .build();

        return Dialog.create(builder -> builder.empty()
                .base(base)
                .type(DialogType.confirmation(confirmButton, cancelButton)));
    }
}
