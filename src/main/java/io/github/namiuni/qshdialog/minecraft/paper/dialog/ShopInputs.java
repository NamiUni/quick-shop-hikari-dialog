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
        CREATE,
        EDIT,
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

    public Builder forCreate(final UserSession user, final TagResolver placeholders) {
        return new Builder(user, placeholders, DialogContext.CREATE);
    }

    public Builder forEdit(final UserSession user, final TagResolver placeholders) {
        return new Builder(user, placeholders, DialogContext.EDIT);
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

        public Builder shopName(final @Nullable String initial) {
            final Component label = switch (this.context) {
                case CREATE -> ShopInputs.this.translations.shopCreationInputName(this.user, this.placeholders);
                case EDIT -> ShopInputs.this.translations.shopModificationInputName(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.text(DialogInputKeys.SHOP_NAME, label)
                    .initial(Objects.requireNonNullElse(initial, ""))
                    .maxLength(ShopInputs.this.qsConfig.shopNameMaxLength())
                    .build();
            this.inputs.put(ShopInputType.SHOP_NAME, input);
            return this;
        }

        public Builder tradeType(final List<TradeType> available, final TradeType initial) {
            final Component label = switch (this.context) {
                case CREATE -> ShopInputs.this.translations.shopCreationInputTradeType(this.user, this.placeholders);
                case EDIT -> ShopInputs.this.translations.shopModificationInputTradeType(this.user, this.placeholders);
            };
            final var entries = available.stream()
                    .map(tradeType -> {
                        final Component optionLabel = switch (this.context) {
                            case CREATE -> switch (tradeType) {
                                case SELLING -> ShopInputs.this.translations.shopCreationInputTradeTypeSelling(this.user);
                                case BUYING -> ShopInputs.this.translations.shopCreationInputTradeTypeBuying(this.user);
                            };
                            case EDIT -> switch (tradeType) {
                                case SELLING -> ShopInputs.this.translations.shopModificationInputTradeTypeSelling(this.user);
                                case BUYING -> ShopInputs.this.translations.shopModificationInputTradeTypeBuying(this.user);
                            };
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
                case CREATE -> ShopInputs.this.translations.shopCreationInputCurrency(this.user, this.placeholders);
                case EDIT -> ShopInputs.this.translations.shopModificationInputCurrency(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.text(DialogInputKeys.SHOP_CURRENCY, label)
                    .initial(Objects.requireNonNullElse(initial, ""))
                    .build();
            this.inputs.put(ShopInputType.CURRENCY, input);
            return this;
        }

        public Builder unit(final int max, final int initial) {
            final Component label = switch (this.context) {
                case CREATE -> ShopInputs.this.translations.shopCreationInputQuantity(this.user, this.placeholders);
                case EDIT -> ShopInputs.this.translations.shopModificationInputQuantity(this.user, this.placeholders);
            };
            final String format = switch (this.context) {
                case CREATE -> ShopInputs.this.translations.shopCreationInputQuantityFormat(this.user, this.placeholders);
                case EDIT -> ShopInputs.this.translations.shopModificationInputQuantityFormat(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.numberRange(DialogInputKeys.SHOP_QUANTITY, label, 1.0f, max)
                    .step(1.0f)
                    .initial((float) initial)
                    .labelFormat(format)
                    .build();
            this.inputs.put(ShopInputType.UNIT, input);
            return this;
        }

        public Builder price(final BigDecimal initial) {
            final Component label = switch (this.context) {
                case CREATE -> ShopInputs.this.translations.shopCreationInputPrice(this.user, this.placeholders);
                case EDIT -> ShopInputs.this.translations.shopModificationInputPrice(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.text(DialogInputKeys.SHOP_PRICE, label)
                    .initial(initial.toPlainString())
                    .build();
            this.inputs.put(ShopInputType.PRICE, input);
            return this;
        }

        public Builder status(final boolean initial) {
            final Component label = switch (this.context) {
                case CREATE -> ShopInputs.this.translations.shopCreationInputStatus(this.user, this.placeholders);
                case EDIT -> ShopInputs.this.translations.shopModificationInputStatus(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.bool(DialogInputKeys.SHOP_AVAILABLE, label)
                    .initial(initial)
                    .build();
            this.inputs.put(ShopInputType.STATUS, input);
            return this;
        }

        public Builder display(final boolean initial) {
            final Component label = switch (this.context) {
                case CREATE -> ShopInputs.this.translations.shopCreationInputDisplay(this.user, this.placeholders);
                case EDIT -> ShopInputs.this.translations.shopModificationInputDisplay(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.bool(DialogInputKeys.SHOP_DISPLAY_VISIBLE, label)
                    .initial(initial)
                    .build();
            this.inputs.put(ShopInputType.DISPLAY, input);
            return this;
        }

        public Builder unlimitedStock(final boolean initial) {
            final Component label = switch (this.context) {
                case CREATE -> ShopInputs.this.translations.shopCreationInputUnlimitedStock(this.user, this.placeholders);
                case EDIT -> ShopInputs.this.translations.shopModificationInputUnlimitedStock(this.user, this.placeholders);
            };
            final DialogInput input = DialogInput.bool(DialogInputKeys.SHOP_INFINITE_STOCK, label)
                    .initial(initial)
                    .build();
            this.inputs.put(ShopInputType.UNLIMITED_STOCK, input);
            return this;
        }

        public List<DialogInput> build() {
            return List.copyOf(this.inputs.values());
        }
    }
}
