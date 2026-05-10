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

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DialogInputKeys {

    public static final String SHOP_PRICE = "shop_price";
    public static final String SHOP_UNIT = "shop_unit";
    public static final String SHOP_TRADE_TYPE = "shop_trade_type";
    public static final String SHOP_STATUS = "shop_status";
    public static final String SHOP_NAME = "shop_name";
    public static final String SHOP_CURRENCY = "shop_currency";
    public static final String SHOP_DISPLAY = "shop_display";
    public static final String SHOP_UNLIMITED_STOCK = "shop_unlimited_stock";

    public static final String TRADE_QUANTITY = "trade_unit";

    private DialogInputKeys() {
    }
}
