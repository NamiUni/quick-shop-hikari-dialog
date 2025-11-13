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

import io.github.namiuni.qshdialog.user.QSHUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
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
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.creation.error.empty_input", Argument.component("input_label", inputLabel));
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

    /* Shop Purchase Dialog */

    public static Component itemPurchaseTitle(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.purchase.title");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component itemPurchaseDescription(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.purchase.description");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component itemPurchaseConfirmationConfirm(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.purchase.confirmation.buy");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component itemPurchaseConfirmationCancel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.purchase.confirmation.cancel");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    /* Shop Sale Dialog */

    public static Component itemSaleTitle(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.sale.title");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component itemSaleDescription(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.sale.description");
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

    public static Component shopInputBundleSize(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.bundle_size.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static String shopInputBundleFormat(final QSHUser qshUser) {
        final TranslatableComponent format = Component.translatable("qsh_dialog.shop.input.bundle_size.format");
        final Component component = GlobalTranslator.render(format, qshUser.locale());
        return PlainTextComponentSerializer.plainText().serialize(component); // Maybe bad approach
    }

    public static Component shopInputPriceLabel(final QSHUser qshUser, final double minPrice, final double maxPrice) {
        final TranslatableComponent component = Component.translatable(
                "qsh_dialog.shop.input.price.label",
                Argument.numeric("min_price", minPrice),
                Argument.numeric("max_price", maxPrice)
        );
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopInputTypeLabel(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.type.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopInputTypeSell(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.type.sell");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopInputTypeBuy(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.type.buy");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopInputShopName(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.name.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopInputCurrency(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.currency.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopInputDisableDisplay(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.display.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }

    public static Component shopInputUnlimited(final QSHUser qshUser) {
        final TranslatableComponent component = Component.translatable("qsh_dialog.shop.input.unlimited.label");
        return GlobalTranslator.render(component, qshUser.locale());
    }
}
