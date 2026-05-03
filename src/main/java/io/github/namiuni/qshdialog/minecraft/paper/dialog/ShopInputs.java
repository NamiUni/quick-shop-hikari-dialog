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

    private enum DialogContext {
        CREATION,
        MODIFICATION,
    }

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

    public Builder forCreation(final UserSession user, final TagResolver placeholders) {
        return new Builder(user, placeholders, DialogContext.CREATION);
    }

    public Builder forModification(final UserSession user, final TagResolver placeholders) {
        return new Builder(user, placeholders, DialogContext.MODIFICATION);
    }

    @NullMarked
    public final class Builder {

        private final UserSession user;
        private final TagResolver placeholders;
        private final DialogContext context;

        private final Map<ShopInputType, DialogInput> inputs = new LinkedHashMap<>();

        private Builder(
                final UserSession user,
                final TagResolver placeholders,
                final DialogContext context
        ) {
            this.user = user;
            this.placeholders = placeholders;
            this.context = context;
        }

        public Builder name(final @Nullable String initial) {
            return this.name(initial, Component.empty());
        }

        public Builder name(final @Nullable String initial, final Component errorText) {
            final Component label = switch (this.context) {
                case CREATION -> ShopInputs.this.translations.dialogCreationInputName(this.user, this.placeholders);
                case MODIFICATION -> ShopInputs.this.translations.dialogModificationInputName(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.text(DialogInputKeys.SHOP_NAME, withError(label, errorText))
                    .initial(Objects.requireNonNullElse(initial, ""))
                    .maxLength(ShopInputs.this.qsConfig.shopNameMaxLength())
                    .build();
            this.inputs.put(ShopInputType.NAME, input);
            return this;
        }

        public Builder tradeType(final List<TradeType> available, final TradeType initial) {
            final Component label = switch (this.context) {
                case CREATION -> ShopInputs.this.translations.dialogCreationInputTradeType(this.user, this.placeholders);
                case MODIFICATION -> ShopInputs.this.translations.dialogModificationInputTradeType(this.user, this.placeholders);
            };
            final var entries = available.stream()
                    .map(tradeType -> {
                        final Component optionLabel = switch (tradeType) {
                            case SELLING -> ShopInputs.this.translations.tradeTypeSelling(this.user);
                            case BUYING -> ShopInputs.this.translations.tradeTypeBuying(this.user);
                        };
                        return SingleOptionDialogInput.OptionEntry.create(tradeType.name(), optionLabel, tradeType == initial);
                    })
                    .toList();
            final DialogInput input = DialogInput.singleOption(DialogInputKeys.SHOP_TRADE_TYPE, label, entries)
                    .build();
            this.inputs.put(ShopInputType.TRADE_TYPE, input);
            return this;
        }

        public Builder currency(final @Nullable String initial) {
            final Component label = switch (this.context) {
                case CREATION -> ShopInputs.this.translations.dialogCreationInputCurrency(this.user, this.placeholders);
                case MODIFICATION -> ShopInputs.this.translations.dialogModificationInputCurrency(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.text(DialogInputKeys.SHOP_CURRENCY, label)
                    .initial(Objects.requireNonNullElse(initial, ""))
                    .build();
            this.inputs.put(ShopInputType.CURRENCY, input);
            return this;
        }

        public Builder quantity(final int max, final int initial) {
            final Component label = switch (this.context) {
                case CREATION -> ShopInputs.this.translations.dialogCreationInputQuantity(this.user, this.placeholders);
                case MODIFICATION -> ShopInputs.this.translations.dialogModificationInputQuantity(this.user, this.placeholders);
            };
            final String format = switch (this.context) {
                case CREATION -> ShopInputs.this.translations.dialogCreationInputQuantityFormat(this.user, this.placeholders);
                case MODIFICATION -> ShopInputs.this.translations.dialogModificationInputQuantityFormat(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.numberRange(DialogInputKeys.SHOP_QUANTITY, label, 1.0f, max)
                    .step(1.0f)
                    .initial((float) initial)
                    .labelFormat(format)
                    .build();
            this.inputs.put(ShopInputType.PRODUCT_QUANTITY, input);
            return this;
        }

        public Builder price(final BigDecimal initial) {
            return this.price(initial, Component.empty());
        }

        public Builder price(final BigDecimal initial, final Component errorText) {
            final Component label = switch (this.context) {
                case CREATION -> ShopInputs.this.translations.dialogCreationInputPrice(this.user, this.placeholders);
                case MODIFICATION -> ShopInputs.this.translations.dialogModificationInputPrice(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.text(DialogInputKeys.SHOP_PRICE, withError(label, errorText))
                    .initial(initial.toPlainString())
                    .build();
            this.inputs.put(ShopInputType.PRICE, input);
            return this;
        }

        public Builder status(final boolean initial) {
            final Component label = switch (this.context) {
                case CREATION -> ShopInputs.this.translations.dialogCreationInputStatus(this.user, this.placeholders);
                case MODIFICATION -> ShopInputs.this.translations.dialogModificationInputStatus(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.bool(DialogInputKeys.SHOP_AVAILABLE, label)
                    .initial(initial)
                    .build();
            this.inputs.put(ShopInputType.STATUS, input);
            return this;
        }

        public Builder display(final boolean initial) {
            final Component label = switch (this.context) {
                case CREATION -> ShopInputs.this.translations.dialogCreationInputDisplay(this.user, this.placeholders);
                case MODIFICATION -> ShopInputs.this.translations.dialogModificationInputDisplay(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.bool(DialogInputKeys.SHOP_DISPLAY_VISIBLE, label)
                    .initial(initial)
                    .build();
            this.inputs.put(ShopInputType.DISPLAY, input);
            return this;
        }

        public Builder stock(final boolean initial) {
            final Component label = switch (this.context) {
                case CREATION -> ShopInputs.this.translations.dialogCreationInputInfiniteStock(this.user, this.placeholders);
                case MODIFICATION -> ShopInputs.this.translations.dialogModificationInputInfiniteStock(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.bool(DialogInputKeys.SHOP_INFINITE_STOCK, label)
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
