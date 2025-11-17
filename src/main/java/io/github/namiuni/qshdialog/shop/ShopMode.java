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
import com.ghostchu.quickshop.shop.SimpleShopManager;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum ShopMode {

    BUYING(SimpleShopManager.BUYING_TYPE, Component.translatable("qsh_dialog.shop.mode.buying")),
    SELLING(SimpleShopManager.SELLING_TYPE, Component.translatable("qsh_dialog.shop.mode.selling"));

    private final IShopType shopType;
    private final Component displayName;

    ShopMode(final IShopType buyingType, final Component displayName) {
        this.shopType = buyingType;
        this.displayName = displayName;
    }

    public static ShopMode of(final IShopType shopType) {
        return switch (shopType) {
            case BuyingType ignored -> BUYING;
            default -> SELLING;
        };
    }

    public IShopType shopType() {
        return this.shopType;
    }

    public Component displayName() {
        return this.displayName;
    }
}
