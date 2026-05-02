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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.listener;

import com.ghostchu.quickshop.api.QuickShopAPI;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.interaction.InteractionBehavior;
import com.ghostchu.quickshop.api.shop.interaction.InteractionClick;
import com.ghostchu.quickshop.api.shop.interaction.InteractionType;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradePurchaseDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradeSellDialog;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopConverter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.utilities.SignUpdater;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import net.kyori.adventure.dialog.DialogLike;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
public final class TradeDialogHandler implements InteractionBehavior {

    private static final String IDENTIFIER = "TRADE_DIALOG";

    private final TranslationService translations;
    private final TradePurchaseDialog purchaseDialog;
    private final TradeSellDialog sellDialog;
    private final SignUpdater signUpdater;

    @Inject
    TradeDialogHandler(
            final TranslationService translations,
            final TradePurchaseDialog purchaseDialog,
            final TradeSellDialog sellDialog,
            final SignUpdater signUpdater
    ) {
        this.translations = translations;
        this.purchaseDialog = purchaseDialog;
        this.sellDialog = sellDialog;
        this.signUpdater = signUpdater;
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
        if (qsShop == null || !player.hasPermission(QSPermissions.USE)) {
            return;
        }

        if (!(qsShop.getLocation().getBlock().getState() instanceof Container container)) {
            return;
        }

        interactEvent.setCancelled(true);

        final UserSession customer = UserSession.of(player);
        final ShopComponent shopComponent = ShopConverter.toShopComponent(qsShop);

        if (!shopComponent.available()) {
            customer.sendMessage(this.translations.tradeErrorShopUnavailable(customer));
            return;
        }

        final List<Sign> signs = qsShop.getSigns();
        if (signs.isEmpty()) {
            return;
        }
        final Sign shopSign = signs.getFirst();
        final ShopBlock shop = new ShopBlock(container, shopSign.getBlock(), shopComponent);
        this.signUpdater.update(shop, player.locale());

        final DialogLike dialog = switch (shopComponent.tradeType()) {
            case SELLING -> this.purchaseDialog.createDialog(customer, shop);
            case BUYING -> this.sellDialog.createDialog(customer, shop);
        };

        if (dialog != null) {
            customer.showDialog(dialog);
        }
    }
}
