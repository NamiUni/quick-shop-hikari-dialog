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

import io.github.namiuni.qshdialog.shop.dialog.ProductPurchaseDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ProductSaleDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopCreationDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopModificationDialogFactory;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QSHUserService {

    private final ShopCreationDialogFactory shopCreationDialogFactory;
    private final ShopModificationDialogFactory shopModificationDialogFactory;
    private final ProductPurchaseDialogFactory productPurchaseDialogFactory;
    private final ProductSaleDialogFactory productSaleDialogFactory;

    public QSHUserService(
            final ShopCreationDialogFactory shopCreationDialogFactory,
            final ShopModificationDialogFactory shopModificationDialogFactory,
            final ProductPurchaseDialogFactory productPurchaseDialogFactory,
            final ProductSaleDialogFactory productSaleDialogFactory
    ) {
        this.shopCreationDialogFactory = shopCreationDialogFactory;
        this.shopModificationDialogFactory = shopModificationDialogFactory;
        this.productPurchaseDialogFactory = productPurchaseDialogFactory;
        this.productSaleDialogFactory = productSaleDialogFactory;
    }

    public QSHUser getUser(final UUID userID) {
        return this.getUser(Objects.requireNonNull(Bukkit.getPlayer(userID)));
    }

    public QSHUser getUser(final Player player) {
        return new QSHUserImpl(
                player,
                this.shopCreationDialogFactory,
                this.shopModificationDialogFactory,
                this.productPurchaseDialogFactory,
                this.productSaleDialogFactory
        );
    }
}
