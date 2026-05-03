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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QSPermissions {

    // General
    public static final String USE = "quickshop.use";

    // Create
    public static final String SHOP_TRADE_TYPE_SELLING = "quickshop.create.sell";
    public static final String SHOP_TRADE_TYPE_BUYING = "quickshop.create.buy";
    public static final String SHOP_TRADE_TYPE_SELLING_OTHER = "quickshop.other.sell";
    public static final String SHOP_TRADE_TYPE_BUYING_OTHER = "quickshop.other.buy";

    // Availability
    public static final String SHOP_TOGGLE_STATUS = "quickshop.togglefreeze";
    public static final String SHOP_TOGGLE_STATUS_OTHER = "quickshop.other.freeze";

    // Display
    public static final String SHOP_TOGGLE_DISPLAY = "quickshop.toggledisplay";
    public static final String SHOP_TOGGLE_DISPLAY_OTHER = "quickshop.other.toggledisplay";

    // Product quantity
    public static final String SHOP_PRODUCT_QUANTITY = "quickshop.create.stacks";
    // public static final String SHOP_PRODUCT_QUANTITY_OTHER = "quickshop.other.stacks";

    // Naming
    public static final String SHOP_NAMING = "quickshop.shopnaming";
    public static final String SHOP_NAMING_OTHER = "quickshop.other.shopnaming";

    // Currency
    public static final String SHOP_CURRENCY = "quickshop.currency";
    public static final String SHOP_CURRENCY_OTHER = "quickshop.other.currency";

    // Infinite stock
    public static final String SHOP_INFINITE_STOCK = "quickshop.unlimited";

    // Price
    public static final String SHOP_PRICE = "quickshop.create.changeprice";
    public static final String SHOP_PRICE_OTHER = "quickshop.other.price";

    // Bypass
    public static final String SHOP_BYPASS_COST_CREATE = "quickshop.bypasscreatefee";
    public static final String SHOP_BYPASS_COST_NAMING = "quickshop.bypass.namefee";
    // public static final String SHOP_BYPASS_COST_PRICING = "";

    private QSPermissions() {
    }
}
