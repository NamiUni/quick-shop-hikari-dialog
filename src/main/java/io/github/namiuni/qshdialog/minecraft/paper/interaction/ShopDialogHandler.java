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
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopCreationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopModificationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopConverter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.dialog.DialogLike;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ShopDialogHandler implements InteractionBehavior {

    private static final String IDENTIFIER = "CONTROL_DIALOG";

    private final ShopCreationDialog shopCreationDialog;
    private final ShopModificationDialog shopModificationDialog;

    public ShopDialogHandler(
            final ShopCreationDialog shopCreationDialog,
            final ShopModificationDialog shopModificationDialog
    ) {
        this.shopCreationDialog = shopCreationDialog;
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
        final UserSession user = UserSession.of(player);
        if (!user.hasPermission(QSPermissions.USE)) {
            return;
        }

        interactEvent.setCancelled(true);

        if (shop != null) {
            this.handleModification(shop, user);
        } else {
            final ItemStack handItem = interactEvent.getItem();
            final Block clickedBlock = interactEvent.getClickedBlock();
            this.handleCreation(user, handItem, clickedBlock, clickedObject);
        }
    }

    private void handleModification(final Shop qsShop, final UserSession user) {
        if (!(qsShop.getLocation().getBlock().getState() instanceof Container container)) {
            return;
        }

        final ShopComponent shopComponent = ShopConverter.toShopComponent(qsShop);

        if (!canModify(user, shopComponent)) {
            return;
        }

        final List<Sign> signs = qsShop.getSigns();
        if (signs.isEmpty()) {
            return;
        }
        final Sign shopSign = signs.getFirst();
        final ShopBlock shop = new ShopBlock(container, shopSign.getBlock(), shopComponent);
        final DialogLike dialog = this.shopModificationDialog.createDialog(user, shop);

        user.showDialog(dialog);
    }

    private void handleCreation(
            final UserSession user,
            final @Nullable ItemStack handItem,
            final @Nullable Block clickedBlock,
            final InteractionClick clickedObject
    ) {

        if (handItem == null || clickedBlock == null) {
            return;
        }

        resolveShopBlock(user, clickedObject, clickedBlock, handItem)
                .ifPresent(shop -> {
                    final DialogLike dialog = this.shopCreationDialog.createDialog(user, shop);
                    user.showDialog(dialog);
                });
    }

    private static Optional<ShopBlock> resolveShopBlock(
            final UserSession user,
            final InteractionClick clickedObject,
            final Block clickedBlock,
            final ItemStack handItem
    ) {
        if (clickedObject == InteractionClick.CONTAINER && clickedBlock.getState() instanceof Container container) {
            return Optional.of(resolveFromContainer(user, clickedBlock, container, handItem));
        }

        if (clickedObject == InteractionClick.SIGN && clickedBlock.getState() instanceof Sign sign) {
            return resolveFromSign(user, clickedBlock, sign, handItem);
        }

        return Optional.empty();
    }

    private static ShopBlock resolveFromContainer(
            final UserSession user,
            final Block clickedBlock,
            final Container container,
            final ItemStack handItem
    ) {
        final Block frontBlock = clickedBlock.getBlockData() instanceof Directional directional
                ? clickedBlock.getRelative(directional.getFacing())
                : clickedBlock.getRelative(user.direction().getOppositeFace());

        final ShopComponent shopComponent = createShopComponent(user, container.getLocation(), handItem);
        return new ShopBlock(container, frontBlock, shopComponent);
    }

    private static Optional<ShopBlock> resolveFromSign(
            final UserSession user,
            final Block clickedBlock,
            final Sign sign,
            final ItemStack handItem
    ) {
        if (!(sign.getBlockData() instanceof WallSign wallSign)) {
            return Optional.empty();
        }

        final Block wallBlock = clickedBlock.getRelative(wallSign.getFacing().getOppositeFace());
        if (!(wallBlock.getState() instanceof Container container)) {
            return Optional.empty();
        }

        final ShopComponent shopComponent = createShopComponent(user, wallBlock.getLocation(), handItem);
        final ShopBlock shop = new ShopBlock(container, sign.getBlock(), shopComponent);
        return Optional.of(shop);
    }

    private static ShopComponent createShopComponent(final UserSession user, final Location location, final ItemStack handItem) {
        return ShopComponent.builder()
                .id(ShopComponent.NEW_ID)
                .location(location)
                .owner(user)
                .product(handItem)
                .price(PriceAnalytics.getPriceLimit(user, handItem).min())
                .build();
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
