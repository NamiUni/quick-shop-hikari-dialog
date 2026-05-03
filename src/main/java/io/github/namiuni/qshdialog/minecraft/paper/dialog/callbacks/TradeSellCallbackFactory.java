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
package io.github.namiuni.qshdialog.minecraft.paper.dialog.callbacks;

import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.DialogInputKeys;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Objects;
import net.kyori.adventure.text.event.ClickCallback;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class TradeSellCallbackFactory {

    private static final ClickCallback.Options CALLBACK_OPTIONS = ClickCallback.Options.builder()
            .uses(1)
            .lifetime(ClickCallback.DEFAULT_LIFETIME)
            .build();

    private final TranslationService translations;
    private final TradeService tradeService;

    @Inject
    TradeSellCallbackFactory(
            final TranslationService translations,
            final TradeService tradeService
    ) {
        this.translations = translations;
        this.tradeService = tradeService;
    }

    public DialogAction createAction(final UserSession user, final ShopBlock shop) {
        return DialogAction.customClick((response, _) -> {
            final int quantity = Objects.requireNonNull(response.getFloat(DialogInputKeys.TRADE_QUANTITY)).intValue();
            if (this.tradeService.sell(user, shop, quantity) instanceof Result.Error) {
                user.sendMessage(this.translations.shopModificationFailedShopNotFound(user));
            }
        }, CALLBACK_OPTIONS);
    }
}
