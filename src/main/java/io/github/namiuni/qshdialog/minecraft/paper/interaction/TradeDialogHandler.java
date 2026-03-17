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
package io.github.namiuni.qshdialog.minecraft.paper.interaction;

import com.ghostchu.quickshop.api.QuickShopAPI;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.interaction.InteractionBehavior;
import com.ghostchu.quickshop.api.shop.interaction.InteractionClick;
import com.ghostchu.quickshop.api.shop.interaction.InteractionType;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradePurchaseDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradeSellDialog;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSMessages;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopConverter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import net.kyori.adventure.dialog.DialogLike;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TradeDialogHandler implements InteractionBehavior {

    private static final String IDENTIFIER = "TRADE_DIALOG";

    private final TradePurchaseDialog purchaseDialog;
    private final TradeSellDialog sellDialog;

    public TradeDialogHandler(final TradePurchaseDialog purchaseDialog, final TradeSellDialog sellDialog) {
        this.purchaseDialog = purchaseDialog;
        this.sellDialog = sellDialog;
    }

    @Override
    public String identifier() {
        return IDENTIFIER;
    }

    @Override
    public void handle(
            final QuickShopAPI quickShopAPI,
            final @Nullable Shop qsShop,
            final Player player,
            final PlayerInteractEvent interactEvent,
            final InteractionClick clickedObject,
            final @Nullable InteractionType interactionType
    ) {
        if (qsShop == null || !player.hasPermission("quickshop.use")) {
            return;
        }

        if (!(qsShop.getLocation().getBlock().getState() instanceof Container container)) {
            return;
        }

        qsShop.setSignText(QuickShops.textManager().findRelativeLanguages(player));
        interactEvent.setCancelled(true);

        final UserSession customer = UserSession.of(player);
        final ShopComponent shopComponent = ShopConverter.toShopComponent(qsShop);

        if (!shopComponent.available()) {
            customer.sendMessage(QSMessages.errorShopUnavailable(customer));
            return;
        }

        final Sign shopSign = qsShop.getSigns().getFirst();
        final ShopBlock shop = new ShopBlock(container, shopSign.getBlock(), shopComponent);

        final DialogLike dialog = switch (shopComponent.tradeType()) {
            case SELLING -> this.purchaseDialog.createDialog(customer, shop);
            case BUYING -> this.sellDialog.createDialog(customer, shop);
        };

        if (dialog != null) {
            customer.showDialog(dialog);
        }
    }
}
