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

import java.util.Locale;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum ShopInputType {
    SHOP_NAME("shop_name"),
    TRADE_TYPE("trade_type"),
    CURRENCY("currency"),
    UNIT("unit"),
    PRICE("price"),
    STATUS("status"),
    DISPLAY("display"),
    UNLIMITED_STOCK("unlimited_stock");

    private final String name;

    ShopInputType(final String name) {
        this.name = name;
    }

    public static ShopInputType of(final String name) {
        return switch (name.toLowerCase(Locale.ROOT)) {
            case "shop_name" -> SHOP_NAME;
            case "trade_type" -> TRADE_TYPE;
            case "currency" -> CURRENCY;
            case "unit" -> UNIT;
            case "price" -> PRICE;
            case "status" -> STATUS;
            case "display" -> DISPLAY;
            case "unlimited_stock" -> UNLIMITED_STOCK;
            default -> throw new IllegalArgumentException();
        };
    }

    @Override
    public String toString() {
        return this.name;
    }
}
