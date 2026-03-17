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
import io.github.namiuni.qshdialog.minecraft.paper.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.TradeInputs;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSMessages;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopFailure;
import io.github.namiuni.qshdialog.minecraft.paper.service.TradeQuantityCalculator;
import io.github.namiuni.qshdialog.minecraft.paper.service.TradeQuantityFailure;
import io.github.namiuni.qshdialog.minecraft.paper.service.TradeService;
import io.github.namiuni.qshdialog.minecraft.paper.translation.Translations;
import io.github.namiuni.qshdialog.minecraft.paper.utilities.ShopTagMapper;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class TradePurchaseDialog {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;
    private final Translations translations;
    private final TradeInputs tradeInputs;
    private final ShopTagMapper shopTagMapper;

    public TradePurchaseDialog(
            final ConfigurationHolder<PrimaryConfiguration> configHolder,
            final Translations translations,
            final TradeInputs tradeInputs,
            final ShopTagMapper shopTagMapper
    ) {
        this.configHolder = configHolder;
        this.translations = translations;
        this.tradeInputs = tradeInputs;
        this.shopTagMapper = shopTagMapper;
    }

    public @Nullable DialogLike createDialog(final UserSession user, final ShopBlock shop) {
        final TagResolver placeholders = TagResolver.builder()
                .resolver(this.shopTagMapper.shopPlaceholders(shop))
                .resolver(this.shopTagMapper.itemPlaceholders(shop.component().product()))
                .build();

        final Result<Integer, TradeQuantityFailure> quantityResult =
                TradeQuantityCalculator.purchasableQuantity(user, shop);

        if (quantityResult instanceof Result.Error<Integer, TradeQuantityFailure>(final TradeQuantityFailure failure)) {
            final Component message = switch (failure) {
                case CUSTOMER_INVENTORY_FULL -> QSMessages.errorCustomerInventoryFull(user, 0);
                case SHOP_OUT_OF_STOCK -> QSMessages.errorShopOutOfStock(user, shop);
                case CUSTOMER_INSUFFICIENT_FUNDS -> QSMessages.errorCustomerInsufficientFunds(user, shop);
                case SHOP_INVENTORY_FULL, SHOP_INSUFFICIENT_FUNDS, CUSTOMER_INSUFFICIENT_ITEMS -> null; // 購入時には発生しない
            };
            if (message != null) {
                user.sendMessage(message);
            }
            return null;
        }

        final int maxQuantity = ((Result.Success<Integer, TradeQuantityFailure>) quantityResult).result();

        final DialogBase base = DialogBase.builder(this.translations.tradePurchaseTitle(user, placeholders))
                .body(List.of(DialogBody.item(shop.component().product().asOne())
                        .description(DialogBody.plainMessage(this.translations.tradePurchaseDescription(user, placeholders)))
                        .build()))
                .inputs(List.of(this.tradeInputs.tradeQuantity(maxQuantity, 1, user, placeholders))) // TODO スタック
                .build();

        final var callbackOptions = ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();
        final var confirmButton = ActionButton.builder(this.translations.tradePurchaseConfirmButton(user, placeholders))
                .action(DialogAction.customClick((response, audience) -> {
                    final int quantity = Objects.requireNonNull(response.getFloat(DialogInputKeys.TRADE_QUANTITY)).intValue();
                    final var result = TradeService.purchase(user, shop, quantity);
                    if (result instanceof Result.Error<Void, ShopFailure.ShopNotFound>(final var failure)) {
                        final Component message = this.translations.shopModificationFailedShopNotFound(user, placeholders, failure);
                        user.sendMessage(message);
                    }
                }, callbackOptions))
                .build();
        final var cancelButton = ActionButton.builder(this.translations.tradePurchaseCancelButton(user, placeholders))
                .build();

        return Dialog.create(builder -> builder.empty()
                .base(base)
                .type(DialogType.confirmation(confirmButton, cancelButton)));
    }
}
