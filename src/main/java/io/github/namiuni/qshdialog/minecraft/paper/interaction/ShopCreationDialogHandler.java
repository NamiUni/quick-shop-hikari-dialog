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
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSConfigurations;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopCreationFilter;
import io.github.namiuni.qshdialog.minecraft.paper.translation.Translations;
import java.util.Optional;
import java.util.Set;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
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
public final class ShopCreationDialogHandler implements InteractionBehavior {

    private static final String IDENTIFIER = "CREATION_DIALOG";

    private final Translations translations;
    private final ShopCreationDialog shopCreationDialog;

    public ShopCreationDialogHandler(final Translations translations, final ShopCreationDialog shopCreationDialog) {
        this.translations = translations;
        this.shopCreationDialog = shopCreationDialog;
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
        if (shop != null) {
            return;
        }

        final UserSession user = UserSession.of(player);
        if (!user.hasPermission(QSPermissions.USE)) {
            return;
        }

        final ItemStack handItem = interactEvent.getItem();
        final Block clickedBlock = interactEvent.getClickedBlock();

        if (handItem == null || clickedBlock == null) {
            return;
        }

        if (!ShopCreationFilter.isCreationAllowed(clickedBlock.getWorld(), handItem)) {
            return;
        }

        final Optional<ShopBlock> resolvedShopOpt = resolveShopBlock(user, clickedObject, clickedBlock, handItem);
        if (resolvedShopOpt.isEmpty()) {
            return;
        }

        interactEvent.setCancelled(true);

        if (ShopCreationFilter.isLimitReached(user)) {
            final Component message = this.translations.shopCreationLimitReached(user);
            user.sendMessage(message);
            return;
        }

        final DialogLike dialog = this.shopCreationDialog.createDialog(user, resolvedShopOpt.get());
        user.showDialog(dialog);
    }

    private static Optional<ShopBlock> resolveShopBlock(
            final UserSession user,
            final InteractionClick clickedObject,
            final Block clickedBlock,
            final ItemStack handItem
    ) {
        final Set<Material> allowedBlocks = QSConfigurations.shopBlocks();

        if (clickedObject == InteractionClick.CONTAINER
                && allowedBlocks.contains(clickedBlock.getType())
                && clickedBlock.getState() instanceof Container container) {
            return Optional.of(resolveFromContainer(user, clickedBlock, container, handItem));
        }

        if (clickedObject == InteractionClick.SIGN && clickedBlock.getState() instanceof Sign sign) {
            return resolveFromSign(user, clickedBlock, sign, handItem, allowedBlocks);
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
            final ItemStack handItem,
            final Set<Material> allowedBlocks
    ) {
        if (!(sign.getBlockData() instanceof WallSign wallSign)) {
            return Optional.empty();
        }

        final Block wallBlock = clickedBlock.getRelative(wallSign.getFacing().getOppositeFace());
        if (allowedBlocks.contains(wallBlock.getType()) && wallBlock.getState() instanceof Container container) {
            final ShopComponent shopComponent = createShopComponent(user, wallBlock.getLocation(), handItem);
            return Optional.of(new ShopBlock(container, sign.getBlock(), shopComponent));
        } else {
            return Optional.empty();
        }
    }

    private static ShopComponent createShopComponent(
            final UserSession user,
            final Location location,
            final ItemStack handItem
    ) {
        return ShopComponent.builder()
                .id(ShopComponent.NEW_ID)
                .location(location)
                .owner(user)
                .product(handItem)
                .price(PriceAnalytics.getPriceLimit(user, handItem).min())
                .build();
    }
}
