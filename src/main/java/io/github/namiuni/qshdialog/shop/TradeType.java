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
package io.github.namiuni.qshdialog.shop;

import com.ghostchu.quickshop.api.shop.IShopType;
import com.ghostchu.quickshop.api.shop.type.BuyingType;
import com.ghostchu.quickshop.api.shop.type.SellingType;
import com.ghostchu.quickshop.shop.SimpleShopManager;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum TradeType {

    BUY(SimpleShopManager.BUYING_TYPE),
    SELL(SimpleShopManager.SELLING_TYPE);

    private final IShopType shopType;

    TradeType(final IShopType buyingType) {
        this.shopType = buyingType;
    }

    public static TradeType of(final IShopType shopType) {
        return switch (shopType) {
            case BuyingType ignored -> BUY;
            case SellingType ignored -> SELL;
            default -> throw new IllegalArgumentException();
        };
    }

    public IShopType shopType() {
        return this.shopType;
    }
}
