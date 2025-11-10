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
package io.github.namiuni.qshdialog.shop.behavior;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.QuickShopAPI;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.interaction.InteractionBehavior;
import com.ghostchu.quickshop.api.shop.interaction.InteractionClick;
import com.ghostchu.quickshop.api.shop.interaction.InteractionType;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.user.QSHUserService;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class QSContainerClickHandler implements InteractionBehavior {

    private final QSHUserService userService;

    public QSContainerClickHandler(final QSHUserService userService) {
        this.userService = userService;
    }

    @Override
    public String identifier() {
        return "ITEM_TRADING_DIALOG";
    }

    @Override
    public void handle(
            final QuickShopAPI quickShopAPI,
            final @Nullable Shop shop,
            final Player player,
            final PlayerInteractEvent interactEvent,
            final InteractionClick interactionClick,
            final @Nullable InteractionType interactionType
    ) {

        // Show Shop Creation Dialog
        final ItemStack usingItem = interactEvent.getItem();
        final QSHUser qshUser = this.userService.getUser(player);
        final Block clickedBlock = interactEvent.getClickedBlock();
        if (shop == null && usingItem != null && clickedBlock != null) {
            interactEvent.setCancelled(true);

            qshUser.showShopCreationDialog(clickedBlock);
            return;
        }

        if (shop != null) {
            interactEvent.setCancelled(true);
            shop.setSignText(((QuickShop) quickShopAPI).text().findRelativeLanguages(player));
            switch (shop.getShopType()) {
                case SELLING -> qshUser.showItemPurchaseDialog(shop);
                case BUYING -> qshUser.showItemSaleDialog(shop);
                case FROZEN -> ((QuickShop) quickShopAPI).text()
                        .of(interactEvent.getPlayer(), "shop-cannot-trade-when-freezing")
                        .send();
            }
        }
    }
}
