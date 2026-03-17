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
package io.github.namiuni.qshdialog.minecraft.paper.translation;

import io.github.namiuni.qshdialog.minecraft.paper.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.MiniPlaceholdersExtension;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.TradeType;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopFailure;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopSuccess;
import java.math.BigDecimal;
import java.util.Locale;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Translations {

    private final ConfigurationHolder<PrimaryConfiguration> primaryConfig;

    public Translations(final ConfigurationHolder<PrimaryConfiguration> primaryConfig) {
        this.primaryConfig = primaryConfig;
    }

    // -------------------------------------------------------------------------
    // Reload
    // -------------------------------------------------------------------------

    public Component configurationReloadSuccess(final Pointered target) {
        return this.translate("qsh_dialog.reload.config.success", target, TagResolver.empty());
    }

    public Component configurationReloadError(final Pointered target) {
        return this.translate("qsh_dialog.reload.config.error", target, TagResolver.empty());
    }

    public Component translationReloadSuccess(final Pointered target) {
        return this.translate("qsh_dialog.reload.translation.success", target, TagResolver.empty());
    }

    public Component translationReloadError(final Pointered target) {
        return this.translate("qsh_dialog.reload.translation.error", target, TagResolver.empty());
    }

    // -------------------------------------------------------------------------
    // Shop creation dialog
    // -------------------------------------------------------------------------

    public Component shopCreationTitle(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.shop.creation.title", target, placeholders);
    }

    public Component shopCreationDescription(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.shop.creation.description", target, placeholders);
    }

    public Component shopCreationConfirmButton(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.button.create", target, placeholders);
    }

    public Component shopCreationCancelButton(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.button.cancel", target, placeholders);
    }

    public Component shopCreationErrorPriceInvalid(final Pointered target, final TagResolver placeholders, final String input) {
        final TagResolver resolver = TagResolver.builder()
                .resolver(placeholders)
                .resolver(TagResolver.resolver("input", (argumentQueue, context) ->
                        Tag.inserting(Component.text(input))))
                .build();
        return this.translate("qsh_dialog.shop.creation.failure.price_invalid", target, resolver);
    }

    public Component shopModificationTitle(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.shop.modification.title", target, placeholders);
    }

    public Component shopModificationDescription(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.shop.modification.description", target, placeholders);
    }

    public Component shopModificationConfirmButton(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.button.apply", target, placeholders);
    }

    public Component shopModificationCancelButton(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.button.cancel", target, placeholders);
    }

    public Component shopModificationErrorPriceInvalid(final Pointered target, final TagResolver placeholders, final String input) {
        final TagResolver resolver = TagResolver.builder()
                .resolver(placeholders)
                .resolver(TagResolver.resolver("input", (argumentQueue, context) ->
                        Tag.inserting(Component.text(input))))
                .build();
        return this.translate("qsh_dialog.shop.modification.failure.price_invalid", target, resolver);
    }

    // -------------------------------------------------------------------------
    // Trade purchase dialog
    // -------------------------------------------------------------------------

    public Component tradePurchaseTitle(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.trade.purchase.title", target, placeholders);
    }

    public Component tradePurchaseDescription(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.trade.purchase.description", target, placeholders);
    }

    public Component tradePurchaseConfirmButton(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.button.buy", target, placeholders);
    }

    public Component tradePurchaseCancelButton(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.button.cancel", target, placeholders);
    }

    // -------------------------------------------------------------------------
    // Trade sell dialog
    // -------------------------------------------------------------------------

    public Component tradeSellTitle(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.trade.sell.title", target, placeholders);
    }

    public Component tradeSellDescription(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.trade.sell.description", target, placeholders);
    }

    public Component tradeSellConfirmButton(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.button.sell", target, placeholders);
    }

    public Component tradeSellCancelButton(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.button.cancel", target, placeholders);
    }

    // -------------------------------------------------------------------------
    // Input labels
    // -------------------------------------------------------------------------

    public Component inputLabelShopName(final Pointered target, final TagResolver placeholders, final BigDecimal namingCost) {
        return this.translate("qsh_dialog.dialog.input.shop.name", target, placeholders, Placeholder.parsed("naming_cost", namingCost.toPlainString()));
    }

    public Component inputLabelShopTradeType(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.input.shop.trade_type", target, placeholders);
    }

    public Component inputLabelShopStatus(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.input.shop.available", target, placeholders);
    }

    public Component inputLabelShopDisplay(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.input.shop.display_visible", target, placeholders);
    }

    public Component inputLabelShopCurrency(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.input.shop.currency", target, placeholders);
    }

    public Component inputLabelShopInfiniteStock(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.input.shop.infinite_stock", target, placeholders);
    }

    public Component inputLabelProductPrice(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.input.product.price", target, placeholders);
    }

    public Component inputLabelProductQuantity(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.input.product.quantity", target, placeholders);
    }

    public String inputFormatProductQuantity(final Pointered target, final TagResolver placeholders) {
        return this.translateToPlain("qsh_dialog.dialog.input.product.quantity.format", target, placeholders);
    }

    public Component inputLabelTradeQuantity(final Pointered target, final TagResolver placeholders) {
        return this.translate("qsh_dialog.dialog.input.trade.quantity", target, placeholders);
    }

    public String inputFormatTradeQuantity(final Pointered target, final TagResolver placeholders) {
        return this.translateToPlain("qsh_dialog.dialog.input.trade.quantity.format", target, placeholders);
    }

    public Component tradeType(final Pointered target, final TradeType tradeType, final TagResolver placeholders) {
        return switch (tradeType) {
            case SELLING -> this.translate("qsh_dialog.trade_type.selling", target, placeholders);
            case BUYING -> this.translate("qsh_dialog.trade_type.buying", target, placeholders);
        };
    }

    // -------------------------------------------------------------------------
    // Shop creation - result
    // -------------------------------------------------------------------------

    public Component shopCreationSuccess(final UserSession user, final TagResolver placeholders, final ShopSuccess success) {
        return this.translate("qsh_dialog.shop.creation.success", user, placeholders);
    }

    public Component shopCreationFailedInsufficientFunds(final UserSession user, final TagResolver placeholders, final ShopFailure.OperatorInsufficientFunds failure) {
        final TagResolver resolver = TagResolver.builder()
                .resolver(placeholders)
                .resolver(TagResolver.resolver("total_cost", (argumentQueue, context) ->
                        Tag.inserting(Component.text(failure.totalCost().toPlainString()))))
                .build();
        return this.translate("qsh_dialog.shop.creation.failure.insufficient_funds", user, resolver);
    }

    public Component shopCreationFailedPriceOutOfRange(final UserSession user, final TagResolver placeholders, final ShopFailure.PriceOutOfRange failure) {
        return this.translate("qsh_dialog.shop.creation.failure.price_out_of_range", user, placeholders);
    }

    public Component shopCreationFailedContainerNotFound(final UserSession user, final TagResolver placeholders, final ShopFailure.ContainerNotFound failure) {
        return this.translate("qsh_dialog.shop.creation.failure.container_not_found", user, placeholders);
    }

    // -------------------------------------------------------------------------
    // Shop modification - result
    // -------------------------------------------------------------------------

    public Component shopModificationSuccess(final UserSession user, final TagResolver placeholders, final ShopSuccess success) {
        return this.translate("qsh_dialog.shop.modification.success", user, placeholders);
    }

    public Component shopModificationFailedInsufficientFunds(final UserSession user, final TagResolver placeholders, final ShopFailure.OperatorInsufficientFunds failure) {
        final TagResolver resolver = TagResolver.builder()
                .resolver(placeholders)
                .resolver(TagResolver.resolver("total_cost", (argumentQueue, context) ->
                        Tag.inserting(Component.text(failure.totalCost().toPlainString()))))
                .build();
        return this.translate("qsh_dialog.shop.modification.failure.insufficient_funds", user, resolver);
    }

    public Component shopModificationFailedPriceOutOfRange(final UserSession user, final TagResolver placeholders, final ShopFailure.PriceOutOfRange failure) {
        final TagResolver resolver = TagResolver.builder()
                .resolver(placeholders)
                .resolver(TagResolver.resolver("min_price", (argumentQueue, context) ->
                        Tag.inserting(Component.text(failure.priceLimit().min().toPlainString()))))
                .resolver(TagResolver.resolver("max_price", (argumentQueue, context) ->
                        Tag.inserting(Component.text(failure.priceLimit().max().toPlainString()))))
                .build();
        return this.translate("qsh_dialog.shop.modification.failure.price_out_of_range", user, resolver);
    }

    public Component shopModificationFailedShopNotFound(final UserSession user, final TagResolver placeholders, final ShopFailure.ShopNotFound failure) {
        return this.translate("qsh_dialog.shop.modification.failure.shop_not_found", user, placeholders);
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private Component translate(final String key, final Pointered target, final TagResolver... placeholders) {
        final Component component = Component.translatable(
                key,
                Argument.target(target),
                Argument.tagResolver(placeholders),
                Argument.tagResolver(MiniPlaceholdersExtension.audienceGlobalPlaceholders())
        );
        if (this.primaryConfig.getConfig().translationSource() == PrimaryConfiguration.TranslationSource.PLUGIN) {
            final Locale locale = target.getOrDefault(Identity.LOCALE, this.primaryConfig.getConfig().defaultLocale());
            return GlobalTranslator.render(component, locale);
        }
        return component;
    }

    private String translateToPlain(final String key, final Pointered target, final TagResolver placeholders) {
        return PlainTextComponentSerializer.plainText().serialize(this.translate(key, target, placeholders));
    }
}
