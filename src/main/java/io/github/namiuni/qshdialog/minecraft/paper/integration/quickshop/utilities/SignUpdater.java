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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.utilities;

import com.ghostchu.quickshop.api.localization.text.TextManager;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopManager;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class SignUpdater {

    private final ShopManager shopManager;
    private final TextManager textManager;

    @Inject
    SignUpdater(
            final ShopManager shopManager,
            final TextManager textManager
    ) {
        this.shopManager = shopManager;
        this.textManager = textManager;
    }

    public void update(final ShopBlock shop, final Locale locale) {
        final Shop qsShop = this.shopManager.getShop(shop.container().getLocation());
        if (qsShop != null) {
            qsShop.setSignText(this.textManager.findRelativeLanguages(locale.toString()));
        }
    }
}
