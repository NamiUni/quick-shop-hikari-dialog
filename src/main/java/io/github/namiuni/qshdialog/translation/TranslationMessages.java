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
package io.github.namiuni.qshdialog.translation;

import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.namiuni.qshdialog.user.QSHUser;
import java.math.BigDecimal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslationMessages {

    private TranslationMessages() {
    }

    public static Component configurationReloadSuccess() {
        return Component.translatable("qsh_dialog.reload.config.success");
    }

    public static Component configurationReloadError() {
        return Component.translatable("qsh_dialog.reload.config.error");
    }

    public static Component translationReloadSuccess() {
        return Component.translatable("qsh_dialog.reload.translation.success");
    }

    public static Component translationReloadError() {
        return Component.translatable("qsh_dialog.reload.translation.error");
    }

    /* Shop Creation Dialog */

    public static Component shopCreationTitle(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.dialog.shop_creation.title");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationDescription(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.dialog.shop_creation.description");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationConfirmationCreate(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.dialog.shop_creation.confirmation.confirm");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationConfirmationCancel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.dialog.shop_creation.confirmation.cancel");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationErrorPriceEmpty(final QSHUser qshUser, final String input) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_creation.error.price.empty",
                Argument.string("input_label", input)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationErrorPriceInvalid(final QSHUser qshUser, final String input) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_creation.error.price.invalid",
                Argument.string("input", input)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationErrorPriceOutOfRange(final QSHUser qshUser, final String input, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_creation.error.price.out_of_range",
                Argument.string("input", input),
                Argument.string("min_price", minPrice.toPlainString()),
                Argument.string("max_price", maxPrice.toPlainString())
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopTradeTypeNoPermissionError(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.dialog.shop_creation.error.create.no_permission");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    /* Shop Modification Dialog */

    public static Component shopModificationTitle(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_modification.title",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationDescription(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_modification.description",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationConfirmationApply(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_modification.confirmation.confirm",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationConfirmationCancel(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_modification.confirmation.cancel",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationErrorPriceEmpty(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_modification.error.price.empty",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationErrorPriceInvalid(final QSHUser qshUser, final TagResolver shopTags, final String input) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_modification.error.price.invalid",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.string("input", input),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationErrorPriceOutOfRange(final QSHUser qshUser, final TagResolver shopTags, final String input, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.shop_modification.error.price.out_of_range",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.string("input", input),
                Argument.string("min_price", minPrice.toPlainString()),
                Argument.string("max_price", maxPrice.toPlainString()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    /* Product Purchase Dialog */

    public static Component tradeBuyTitle(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.product_purchase.title",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component tradeBodyBuyDescription(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.product_purchase.description",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component tradeBuyConfirmationBuy(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.product_purchase.confirmation.confirm",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component tradeBuyConfirmationCancel(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.product_purchase.confirmation.cancel",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    /* Product Sellback Dialog */

    public static Component itemSaleTitle(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.product_sellback.title",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component productSaleDescription(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.product_sellback.description",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component productSaleConfirmationSell(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.product_sellback.confirmation.confirm",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component productSaleConfirmationCancel(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.product_sellback.confirmation.cancel",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    /* Shop General Dialog inputs */

    public static Component shopProductBundleSize(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.label.product.bundle_size",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static String shopProductBundleFormat(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent format = Component.translatable(
                "qsh_dialog.dialog.label.product.bundle_size.format",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        final Component component = GlobalTranslator.render(format, qshUser.locale());
        return PlainTextComponentSerializer.plainText().serialize(component); // Maybe bad approach
    }

    public static Component productPriceLabel(final QSHUser qshUser, final TagResolver shopTags, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final TranslatableComponent translatableLabel = Component.translatable(
                "qsh_dialog.dialog.label.product.price",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.string("min_price", minPrice.toPlainString()),
                Argument.string("max_price", maxPrice.toPlainString()),
                Argument.target(qshUser)
        );

        final Component rendered = GlobalTranslator.render(translatableLabel, qshUser.locale());

        final TextReplacementConfig config = TextReplacementConfig.builder()
                .match("^.{32,}$")
                .replacement(builder -> {
                    final TranslatableComponent label = Component.translatable(
                            "qsh_dialog.dialog.label.product.price",
                            Argument.numeric("min_price", minPrice),
                            Argument.numeric("max_price", maxPrice)
                    );

                    return GlobalTranslator.render(label, qshUser.locale());
                })
                .build();

        return rendered.replaceText(config).compact();
    }

    public static Component shopTradeTypeLabel(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.label.shop.mode",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopNameLabel(final QSHUser qshUser, final TagResolver shopTags, final double namingCost) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.label.shop.name",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.numeric("naming_cost", namingCost),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCurrencyLabel(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.label.currency",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopShowDisplayLabel(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.label.display_item",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopUnlimitedStockLabel(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.label.unlimited",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopQuantityLabel(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.label.trade.quantity",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static String shopQuantityFormat(final QSHUser qshUser, final TagResolver shopTags) {
        final TranslatableComponent format = Component.translatable(
                "qsh_dialog.dialog.label.trade.quantity.format",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(qshUser)
        );
        final Component component = GlobalTranslator.render(format, qshUser.locale());
        return PlainTextComponentSerializer.plainText().serialize(component); // Maybe bad approach
    }

    public static Component shopToggleAvailableLabel(final QSHUser user, final TagResolver shopTags) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.dialog.label.shop.status",
                Argument.tagResolver(shopTags, MiniPlaceholders.audienceGlobalPlaceholders()),
                Argument.target(user)
        );
        return GlobalTranslator.render(component, user.locale());
    }
}
