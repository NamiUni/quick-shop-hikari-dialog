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
import io.github.namiuni.qshdialog.minecraft.paper.dialog.DialogResponseParser;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.InvalidPriceException;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPlaceholders;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyFormatter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopFailure;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopSuccess;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.Set;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopCreationCallbackFactory {

    private static final ClickCallback.Options CALLBACK_OPTIONS = ClickCallback.Options.builder()
            .uses(1)
            .lifetime(ClickCallback.DEFAULT_LIFETIME)
            .build();

    private final TranslationService translations;
    private final ShopService shopService;
    private final QSPlaceholders qsPlaceholders;
    private final EconomyFormatter economyFormatter;

    @Inject
    ShopCreationCallbackFactory(
            final TranslationService translations,
            final ShopService shopService,
            final QSPlaceholders qsPlaceholders,
            final EconomyFormatter economyFormatter
    ) {
        this.translations = translations;
        this.shopService = shopService;
        this.qsPlaceholders = qsPlaceholders;
        this.economyFormatter = economyFormatter;
    }

    public DialogAction createAction(final UserSession user, final ShopBlock preparingShop) {
        return DialogAction.customClick((response, _) -> {
            final ShopComponent inputComponent;
            try {
                inputComponent = DialogResponseParser.parse(response, preparingShop.component());
            } catch (final InvalidPriceException e) {
                user.sendMessage(this.translations.shopCreationFailedPriceInvalid(user, e.rawInput()));
                return;
            }

            final ShopBlock shop = preparingShop.withComponent(inputComponent);
            final TagResolver newPlaceholders = TagResolver.resolver(this.qsPlaceholders.shopPlaceholder(shop));
            final String world = shop.container().getWorld().getName();

            switch (this.shopService.createShop(user, shop)) {
                case Result.Success(final ShopSuccess success) -> user.sendMessage(
                        this.translations.shopCreationSuccess(
                                user,
                                success.paid(),
                                this.economyFormatter.format(success.paid(), world),
                                newPlaceholders
                        ));
                case Result.Error(final Set<ShopFailure> errors) -> errors.forEach(failure -> {
                    switch (failure) {
                        case ShopFailure.ContainerNotFound _ ->
                                user.sendMessage(this.translations.shopCreationFailedContainerNotFound(user, newPlaceholders));
                        case ShopFailure.OperatorInsufficientFunds it -> {
                            final BigDecimal cost = it.totalCost();
                            user.sendMessage(this.translations.shopCreationFailedInsufficientFunds(
                                    user, cost, this.economyFormatter.format(cost, world), newPlaceholders));
                        }
                        case ShopFailure.PriceOutOfRange _ ->
                                user.sendMessage(this.translations.shopCreationFailedPriceOutOfRange(user, newPlaceholders));
                        default -> { }
                    }
                });
            }
        }, CALLBACK_OPTIONS);
    }
}
