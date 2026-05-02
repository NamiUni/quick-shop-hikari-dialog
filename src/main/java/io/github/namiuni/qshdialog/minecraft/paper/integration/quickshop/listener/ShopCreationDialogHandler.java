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
import com.ghostchu.quickshop.api.event.Phase;
import com.ghostchu.quickshop.api.event.management.ShopCreateEvent;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.interaction.InteractionBehavior;
import com.ghostchu.quickshop.api.shop.interaction.InteractionClick;
import com.ghostchu.quickshop.api.shop.interaction.InteractionType;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopCreationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopCreationFilter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Optional;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
public final class ShopCreationDialogHandler implements InteractionBehavior {

    private static final String IDENTIFIER = "CREATION_DIALOG";

    private final TranslationService translations;
    private final ShopCreationDialog shopCreationDialog;
    private final ShopCreationFilter shopCreationFilter;
    private final PriceAnalytics priceAnalytics;

    @Inject
    ShopCreationDialogHandler(
            final TranslationService translations,
            final ShopCreationDialog shopCreationDialog,
            final ShopCreationFilter shopCreationFilter,
            final PriceAnalytics priceAnalytics
    ) {
        this.translations = translations;
        this.shopCreationDialog = shopCreationDialog;
        this.shopCreationFilter = shopCreationFilter;
        this.priceAnalytics = priceAnalytics;
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

        if (!this.shopCreationFilter.isWorldAllowed(clickedBlock.getWorld())) {
            return;
        }

        if (!this.shopCreationFilter.isProductAllowed(handItem)) {
            return;
        }

        final Optional<ShopBlock> shopBlock = this.resolveShopBlock(user, clickedObject, clickedBlock, handItem);
        if (shopBlock.isEmpty()) {
            return;
        }

        final ShopCreateEvent createEvent = new ShopCreateEvent(
                Phase.PRE_CANCELLABLE,
                null,
                user.qsUser(),
                shopBlock.get().component().location()
        );
        if (!createEvent.callEvent()) {
            return;
        }

        if (this.shopCreationFilter.isLimitReached(user)) {
            final Component message = this.translations.shopCreationLimitReached(user);
            user.sendMessage(message);
            return;
        }

        interactEvent.setCancelled(true);

        final DialogLike dialog = this.shopCreationDialog.createDialog(user, shopBlock.get());
        user.showDialog(dialog);
    }

    private Optional<ShopBlock> resolveShopBlock(
            final UserSession user,
            final InteractionClick clickedType,
            final Block clickedBlock,
            final ItemStack handItem
    ) {
        final Block shopBlock = switch (clickedType) {
            case InteractionClick.SIGN -> clickedBlock.getRelative(user.direction().getOppositeFace());
            case InteractionClick.CONTAINER -> clickedBlock;
            default -> null;
        };

        if (shopBlock == null) {
            return Optional.empty();
        }

        if (!this.shopCreationFilter.isBlockAllowed(shopBlock)) {
            return Optional.empty();
        }

        final Block frontBlock = switch (clickedType) {
            case InteractionClick.SIGN -> clickedBlock;
            default -> clickedBlock.getRelative(user.direction().getOppositeFace());
        };

        if (!(shopBlock.getState() instanceof Container container)) {
            return Optional.empty();
        }

        final ShopComponent shopComponent = ShopComponent.builder()
                .id(ShopComponent.NEW_ID)
                .location(shopBlock.getLocation())
                .owner(user)
                .product(handItem)
                .price(this.priceAnalytics.priceRange(user, handItem).min())
                .build();
        return Optional.of(new ShopBlock(container, frontBlock, shopComponent));
    }
}
