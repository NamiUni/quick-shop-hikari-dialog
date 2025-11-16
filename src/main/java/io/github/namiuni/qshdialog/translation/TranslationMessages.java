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

import com.ghostchu.quickshop.api.shop.Shop;
import io.github.namiuni.qshdialog.user.QSHUser;
import java.math.BigDecimal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslationMessages {

    private TranslationMessages() {
    }

    public static Component configurationReloadSuccess() {
        return Component.translatable("qsh_dialog.command.reload.config.success");
    }

    public static Component configurationReloadError() {
        return Component.translatable("qsh_dialog.command.reload.config.error");
    }

    public static Component translationReloadSuccess() {
        return Component.translatable("qsh_dialog.command.reload.translation.success");
    }

    public static Component translationReloadError() {
        return Component.translatable("qsh_dialog.command.reload.translation.error");
    }

    /* Shop Creation Dialog */

    public static Component shopCreationTitle(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.creation.title");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationDescription(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.creation.description");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationConfirmationCreate(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.creation.confirmation.create");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationConfirmationCancel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.creation.confirmation.cancel");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCreationErrorEmptyInput(final QSHUser qshUser, final Component inputLabel) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.shop.creation.error.empty_input",
                Argument.component("input_label", inputLabel)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    /* Shop Modify Dialog */

    public static Component shopModificationTitle(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.modification.title");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationDescription(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.modification.description");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationConfirmationApply(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.modification.confirmation.apply");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationConfirmationCancel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.modification.confirmation.cancel");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopModificationErrorEmptyInput(final QSHUser qshUser, final Component inputLabel) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.shop.modification.error.empty_input",
                Argument.component("input_label", inputLabel)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    /* Product Purchase Dialog */

    public static Component tradeBuyTitle(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.purchase.title");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component tradeBuyConfirmationBuy(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.purchase.confirmation.buy");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component tradeBuyConfirmationCancel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.purchase.confirmation.cancel");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    /* Product Sale Dialog */

    public static Component itemSaleTitle(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.sale.title");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component itemSaleDescription(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.body.sale_description");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component itemSaleConfirmationConfirm(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.sale.confirmation.sell");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component itemSaleConfirmationCancel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.sale.confirmation.cancel");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    /* Shop General Dialog inputs */

    public static Component shopProductBundleSize(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.bundle_size.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static String shopProductBundleFormat(final QSHUser qshUser) {
        final TranslatableComponent format = Component.translatable("qsh_dialog.shop.input.bundle_size.format");
        final Component component = GlobalTranslator.render(format, qshUser.locale());
        return PlainTextComponentSerializer.plainText().serialize(component); // Maybe bad approach
    }

    public static Component productPriceLabel(final QSHUser qshUser, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final TranslatableComponent translatableLabel = Component.translatable(
                "qsh_dialog.shop.input.price.label",
                Argument.string("min_price", minPrice.toPlainString()),
                Argument.string("max_price", maxPrice.toPlainString())
        );

        final Component rendered = GlobalTranslator.render(translatableLabel, qshUser.locale());

        final TextReplacementConfig config = TextReplacementConfig.builder()
                .match("^.{32,}$")
                .replacement(builder -> {
                    final TranslatableComponent label = Component.translatable(
                            "qsh_dialog.shop.input.price.label",
                            Argument.numeric("min_price", minPrice),
                            Argument.numeric("max_price", maxPrice)
                    );

                    return GlobalTranslator.render(label, qshUser.locale());
                })
                .build();

        return rendered.replaceText(config).compact();
    }

    public static Component shopTradeTypeLabel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.type.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopTradeTypeSell(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.type.sell");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopTradeTypeBuy(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.type.buy");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopNameLabel(final QSHUser qshUser, final double namingCost) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.shop.input.name.label",
                Argument.numeric("naming_cost", namingCost)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopCurrencyLabel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.currency.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopShowDisplayLabel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.display.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopUnlimitedStockLabel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.unlimited.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopQuantityLabel(final QSHUser qshUser, final Shop shop) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.shop.input.quantity.label"
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static String shopQuantityFormat(final QSHUser qshUser, final Shop shop) {
        final TranslatableComponent format = Component.translatable("qsh_dialog.shop.input.quantity.format");
        final Component component = GlobalTranslator.render(format, qshUser.locale());
        return PlainTextComponentSerializer.plainText().serialize(component); // Maybe bad approach
    }

    // Dialog Body

    public static Component tradeBodyBuyDescription(final QSHUser qshUser, final Shop shop) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.body.purchase_description");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component tradeBodyDefaultShopName(final QSHUser customer, final Shop shop) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.shop.body.default_shop_name",
                Argument.component("owner_name", shop.getOwner().getBukkitPlayer()
                        .map(CommandSender::name)
                        .orElse(Component.text("Unknown"))
                ),
                Argument.component("owner_display_name", shop.getOwner().getBukkitPlayer()
                        .map(Player::displayName)
                        .orElse(Component.text("Unknown"))
                )
        );

        return GlobalTranslator.render(component, customer.locale());
    }

    public static Component tradeBodyProductName(final QSHUser customer, final Shop shop) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.shop.body.product_name",
                Argument.component("product_effective_name", shop.getItem().effectiveName()),
                Argument.component("product_display_name", shop.getItem().displayName()),
                Argument.numeric("product_bundle_size", shop.getItem().getAmount())
        );

        return GlobalTranslator.render(component, customer.locale());
    }

    public static Component tradeBodyPrice(final QSHUser customer, final Shop shop) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.shop.body.product_price",
                Argument.numeric("price", shop.getPrice())
        );

        return GlobalTranslator.render(component, customer.locale());
    }
}
