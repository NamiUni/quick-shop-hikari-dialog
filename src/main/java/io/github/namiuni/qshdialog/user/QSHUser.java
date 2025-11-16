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
package io.github.namiuni.qshdialog.user;

import com.ghostchu.quickshop.api.obj.QUser;
import com.ghostchu.quickshop.api.shop.Shop;
import java.util.Locale;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.permission.PermissionChecker;
import org.bukkit.block.Container;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface QSHUser extends Audience {

    QUser quickShopUser();

    default UUID uuid() {
        return this.get(Identity.UUID).orElseThrow();
    }

    default Locale locale() {
        return this.get(Identity.LOCALE).orElse(Locale.US);
    }

    default boolean hasPermission(final String permission) {
        return this.get(PermissionChecker.POINTER)
                .map(checker -> checker.test(permission))
                .orElseThrow();
    }

    void showShopCreationDialog(Container container);

    void showShopModificationDialog(Shop shop);

    void showProductPurchaseDialog(Shop shop);

    void showProductSaleDialog(Shop shop);

}
