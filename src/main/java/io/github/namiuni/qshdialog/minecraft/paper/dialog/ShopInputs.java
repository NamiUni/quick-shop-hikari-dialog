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
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeType;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopInputs {

    private final TranslationService translations;
    private final QSConfiguration qsConfig;

    @Inject
    ShopInputs(
            final TranslationService translations,
            final QSConfiguration qsConfig
    ) {
        this.translations = translations;
        this.qsConfig = qsConfig;
    }

    public Builder target(final UserSession user, final TagResolver placeholders) {
        return new Builder(user, placeholders);
    }

    @NullMarked
    public final class Builder {

        private final UserSession user;
        private final TagResolver placeholders;

        private final Map<ShopInputType, DialogInput> inputs = new LinkedHashMap<>();

        private Builder(
                final UserSession user,
                final TagResolver placeholders
        ) {
            this.user = user;
            this.placeholders = placeholders;
        }

        public Builder name(final @Nullable String initial) {
            return this.name(initial, Component.empty());
        }

        public Builder name(final @Nullable String initial, final Component errorText) {
            final DialogInput input = DialogInput.text(DialogInputKeys.SHOP_NAME, withError(ShopInputs.this.translations.inputLabelShopName(this.user, this.placeholders), errorText))
                    .initial(Objects.requireNonNullElse(initial, ""))
                    .maxLength(ShopInputs.this.qsConfig.shopNameMaxLength())
                    .build();

            this.inputs.put(ShopInputType.NAME, input);
            return this;
        }

        public Builder tradeType(final List<TradeType> available, final TradeType initial) {
            final var entries = available.stream()
                    .map(tradeType -> {
                        final Component component = switch (tradeType) {
                            case SELLING -> ShopInputs.this.translations.tradeTypeSelling(this.user);
                            case BUYING -> ShopInputs.this.translations.tradeTypeBuying(this.user);
                        };
                        return SingleOptionDialogInput.OptionEntry.create(tradeType.name(), component, tradeType == initial);
                    })
                    .toList();
            final DialogInput input = DialogInput.singleOption(
                            DialogInputKeys.SHOP_TRADE_TYPE,
                            ShopInputs.this.translations.inputLabelShopTradeType(this.user, this.placeholders),
                            entries
                    )
                    .build();
            this.inputs.put(ShopInputType.TRADE_TYPE, input);
            return this;
        }

        public Builder currency(final @Nullable String initial) {
            final DialogInput input = DialogInput.text(DialogInputKeys.SHOP_CURRENCY,
                            ShopInputs.this.translations.inputLabelShopCurrency(this.user, this.placeholders))
                    .initial(Objects.requireNonNullElse(initial, ""))
                    .build();
            this.inputs.put(ShopInputType.CURRENCY, input);
            return this;
        }

        public Builder quantity(final int max, final int initial) {
            final DialogInput input = DialogInput.numberRange(DialogInputKeys.SHOP_QUANTITY, ShopInputs.this.translations.inputLabelProductQuantity(this.user, this.placeholders), 1.0f, max)
                    .step(1.0f)
                    .initial((float) initial)
                    .labelFormat(ShopInputs.this.translations.inputFormatProductQuantity(this.user, this.placeholders))
                    .build();
            this.inputs.put(ShopInputType.PRODUCT_QUANTITY, input);
            return this;
        }

        public Builder price(final BigDecimal initial) {
            return this.price(initial, Component.empty());
        }

        public Builder price(final BigDecimal initial, final Component errorText) {
            final DialogInput input = DialogInput.text(DialogInputKeys.SHOP_PRICE, withError(ShopInputs.this.translations.inputLabelProductPrice(this.user, this.placeholders), errorText))
                    .initial(initial.toPlainString())
                    .build();
            this.inputs.put(ShopInputType.PRICE, input);
            return this;
        }

        public Builder status(final boolean initial) {
            final DialogInput input = DialogInput.bool(DialogInputKeys.SHOP_AVAILABLE, ShopInputs.this.translations.inputLabelShopStatus(this.user, this.placeholders))
                    .initial(initial)
                    .build();
            this.inputs.put(ShopInputType.STATUS, input);
            return this;
        }

        public Builder display(final boolean initial) {
            final DialogInput input = DialogInput.bool(DialogInputKeys.SHOP_DISPLAY_VISIBLE, ShopInputs.this.translations.inputLabelShopDisplay(this.user, this.placeholders))
                    .initial(initial)
                    .build();
            this.inputs.put(ShopInputType.DISPLAY, input);
            return this;
        }

        public Builder stock(final boolean initial) {
            final DialogInput input = DialogInput.bool(DialogInputKeys.SHOP_INFINITE_STOCK, ShopInputs.this.translations.inputLabelShopInfiniteStock(this.user, this.placeholders))
                    .initial(initial)
                    .build();
            this.inputs.put(ShopInputType.STOCK, input);
            return this;
        }

        public List<DialogInput> buildInputs() {
            return List.copyOf(this.inputs.values());
        }

        private static Component withError(final Component label, final Component errorText) {
            if (errorText == Component.empty()) {
                return label;
            }
            return label.append(Component.space()).append(errorText);
        }
    }
}
