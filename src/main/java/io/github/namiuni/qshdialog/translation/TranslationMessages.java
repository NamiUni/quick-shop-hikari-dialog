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

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslationMessages {

    private TranslationMessages() {
    }

    /* Shop Creation Dialog */

    public static Component shopCreationTitle() {
        // "Shop Creation Menu"
        return Component.translatable("qsh_dialog.shop.creation.title");
    }

    public static Component shopCreationDescription() {
        // "Create your shop. Once you’ve filled out all the required fields, click <b><yellow>Create</yellow></b> to finish."
        return Component.translatable("qsh_dialog.shop.creation.description");
    }

    public static Component shopCreationPriceLabel() {
        return Component.translatable("qsh_dialog.shop.creation.price.label");
    }

    public static Component shopCreationTypeLabel() {
        return Component.translatable("qsh_dialog.shop.creation.type.label");
    }

    public static Component shopCreationTypeSell() {
        return Component.translatable("qsh_dialog.shop.creation.type.sell");
    }

    public static Component shopCreationTypeBuy() {
        return Component.translatable("qsh_dialog.shop.creation.type.buy");
    }

    public static Component shopCreationConfirmationCreate() {
        return Component.translatable("qsh_dialog.shop.creation.confirmation.create");
    }

    public static Component shopCreationConfirmationCancel() {
        return Component.translatable("qsh_dialog.shop.creation.confirmation.cancel");
    }

    /* Shop Modify Dialog */

    public static Component shopModificationTitle() {
        return Component.translatable("qsh_dialog.shop.modification.title");
    }

    public static Component shopModificationDescription() {
        return Component.translatable("qsh_dialog.shop.modification.description");
    }

    public static Component shopModificationConfirmationApply() {
        return Component.translatable("qsh_dialog.shop.modification.confirmation.apply");
    }

    public static Component shopModificationConfirmationCancel() {
        return Component.translatable("qsh_dialog.shop.modification.confirmation.cancel");
    }

    /* Shop Purchase Dialog */

    public static Component itemPurchaseTitle() {
        return Component.translatable("qsh_dialog.shop.purchase.title");
    }

    public static Component itemPurchaseDescription() {
        return Component.translatable("qsh_dialog.shop.purchase.description");
    }

    public static Component itemPurchaseConfirmationConfirm() {
        return Component.translatable("qsh_dialog.shop.purchase.confirmation.confirm");
    }

    public static Component itemPurchaseConfirmationCancel() {
        return Component.translatable("qsh_dialog.shop.purchase.confirmation.cancel");
    }

    /* Shop Sale Dialog */

    public static Component itemSaleTitle() {
        return Component.translatable("qsh_dialog.shop.sale.title");
    }

    public static Component itemSaleDescription() {
        return Component.translatable("qsh_dialog.shop.sale.description");
    }

    public static Component itemSaleConfirmationConfirm() {
        return Component.translatable("qsh_dialog.shop.sale.confirmation.confirm");
    }

    public static Component itemSaleConfirmationCancel() {
        return Component.translatable("qsh_dialog.shop.sale.confirmation.cancel");
    }
}
