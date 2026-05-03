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
import io.github.namiuni.qshdialog.minecraft.paper.dialog.dialogs.ShopModificationDialogFactory;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopConverter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
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
public final class ShopModificationDialogHandler implements InteractionBehavior {

    private static final String IDENTIFIER = "MODIFICATION_DIALOG";

    private final ShopModificationDialogFactory shopModificationDialog;

    @Inject
    ShopModificationDialogHandler(final ShopModificationDialogFactory shopModificationDialog) {
        this.shopModificationDialog = shopModificationDialog;
    }

    @Override
    public String identifier() {
        return IDENTIFIER;
    }

    @Override
    public void handle(
            final QuickShopAPI quickShopAPI,
            final @Nullable Shop shop,
            final Player player,
            final PlayerInteractEvent interactEvent,
            final InteractionClick clickedObject,
            final @Nullable InteractionType interactionType
    ) {
        if (shop == null) {
            return;
        }

        final UserSession user = UserSession.of(player);
        if (!user.hasPermission(QSPermissions.USE)) {
            return;
        }

        interactEvent.setCancelled(true);

        if (!(shop.getLocation().getBlock().getState() instanceof Container container)) {
            return;
        }

        final ShopComponent shopComponent = ShopConverter.toShopComponent(shop);

        if (!canModify(user, shopComponent)) {
            return;
        }

        final List<Sign> signs = shop.getSigns();
        if (signs.isEmpty()) {
            return;
        }
        final Sign shopSign = signs.getFirst();
        final ShopBlock shopBlock = new ShopBlock(container, shopSign.getBlock(), shopComponent);
        final DialogLike dialog = this.shopModificationDialog.createDialog(user, shopBlock);

        user.showDialog(dialog);
    }

    private static boolean canModify(final UserSession user, final ShopComponent shopComponent) {
        return shopComponent.isStaff(user.uuid())
                || hasAnyModificationOtherPermission(user)
                || user.hasPermission(QSPermissions.SHOP_INFINITE_STOCK);
    }

    private static boolean hasAnyModificationOtherPermission(final UserSession user) {
        return user.hasPermission(QSPermissions.SHOP_NAMING_OTHER)
                || user.hasPermission(QSPermissions.SHOP_PRICE_OTHER)
                || user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING_OTHER)
                || user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING_OTHER)
                || user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS_OTHER)
                || user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY_OTHER)
                || user.hasPermission(QSPermissions.SHOP_CURRENCY_OTHER);
    }
}
