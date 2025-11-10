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

import com.ghostchu.quickshop.api.shop.Shop;
import io.github.namiuni.qshdialog.shop.dialog.ItemPurchaseDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ItemSaleDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopCreationDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopModificationDialogFactory;
import io.papermc.paper.dialog.Dialog;
import java.util.Locale;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record QSHUserImpl(
        Player player,
        ShopCreationDialogFactory shopCreationDialogFactory,
        ShopModificationDialogFactory shopModificationDialogFactory,
        ItemPurchaseDialogFactory itemPurchaseDialogFactory,
        ItemSaleDialogFactory itemSaleDialogFactory
) implements QSHUser, ForwardingAudience.Single {

    @Override
    public Audience audience() {
        return this.player;
    }

    @Override
    public void showShopCreationDialog(final Block targetBlock) {
        final Dialog dialog = this.shopCreationDialogFactory.create(targetBlock, this);
        this.showDialog(dialog);
    }

    @Override
    public void showShopModificationDialog(final Shop shop) {
        final Dialog dialog = this.shopModificationDialogFactory.create(shop, this);
        this.showDialog(dialog);
    }

    @Override
    public void showItemPurchaseDialog(final Shop shop) {
        final Dialog dialog = this.itemPurchaseDialogFactory.create(shop, this);
        this.showDialog(dialog);
    }

    @Override
    public void showItemSaleDialog(final Shop shop) {
        final Dialog dialog = this.itemSaleDialogFactory.create(shop, this);
        this.showDialog(dialog);
    }

    @Override
    public ItemStack mainHandItem() {
        return this.player.getInventory().getItemInMainHand();
    }

    @Override
    public Locale locale() {
        return this.player.locale();
    }
}
