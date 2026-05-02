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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum TradeQuantityFailure {
    CUSTOMER_INVENTORY_FULL,
    SHOP_OUT_OF_STOCK,
    CUSTOMER_INSUFFICIENT_FUNDS,
    SHOP_INVENTORY_FULL,
    SHOP_INSUFFICIENT_FUNDS,
    CUSTOMER_INSUFFICIENT_ITEMS,
}
