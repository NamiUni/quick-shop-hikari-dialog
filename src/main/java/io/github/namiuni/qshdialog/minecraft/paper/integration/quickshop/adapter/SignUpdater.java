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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter;

import com.ghostchu.quickshop.api.shop.Shop;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SignUpdater {

    private SignUpdater() {
    }

    public static void update(final ShopBlock shop, final Locale locale) {
        final Shop qsShop = QuickShops.shopManager().getShop(shop.container().getLocation());
        if (qsShop != null) {
            qsShop.setSignText(QuickShops.textManager().findRelativeLanguages(locale.toString()));
        }
    }
}
