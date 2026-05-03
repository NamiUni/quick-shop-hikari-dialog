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
package io.github.namiuni.qshdialog.minecraft.paper.commands;

import com.ghostchu.quickshop.api.event.Phase;
import com.ghostchu.quickshop.api.event.management.ShopCreateEvent;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopCreationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopModificationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopCreationFilter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.permission.QSHDialogPermissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import jakarta.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import net.kyori.adventure.dialog.DialogLike;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ShopCommand implements CommandFactory {

    private final TranslationService translations;
    private final ShopService shopService;
    private final ShopCreationFilter shopCreationFilter;
    private final PriceAnalytics priceAnalytics;
    private final ShopCreationDialog creationDialog;
    private final ShopModificationDialog modificationDialog;

    @Inject
    ShopCommand(
            final TranslationService translations,
            final ShopService shopService,
            final ShopCreationFilter shopCreationFilter,
            final PriceAnalytics priceAnalytics,
            final ShopCreationDialog creationDialog,
            final ShopModificationDialog modificationDialog
    ) {
        this.translations = translations;
        this.shopService = shopService;
        this.shopCreationFilter = shopCreationFilter;
        this.priceAnalytics = priceAnalytics;
        this.creationDialog = creationDialog;
        this.modificationDialog = modificationDialog;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("shopdialog")
                .then(this.createNode())
                .then(this.modificationNode())
                .build();
    }

    private CommandNode<CommandSourceStack> createNode() {
        return Commands.literal("create")
                .requires(source -> source.getExecutor() instanceof Player player
                        && player.hasPermission(QSPermissions.USE)
                        && player.hasPermission(QSHDialogPermissions.COMMAND_CREATE)
                )
                .executes(context -> {
                    final Player player = (Player) Objects.requireNonNull(context.getSource().getExecutor());
                    final UserSession user = UserSession.of(player);

                    final Block targetBlock = user.targetBlock();
                    if (targetBlock == null) {
                        user.sendMessage(this.translations.commandShopFailedNoTargetBlock(user));
                        return SINGLE_FAILED;
                    }

                    if (!this.shopCreationFilter.isWorldAllowed(targetBlock.getWorld())) {
                        user.sendMessage(this.translations.commandShopFailedWorldNotAllowed(user));
                        return SINGLE_FAILED;
                    }

                    final ItemStack handItem = user.inventory().getItemInMainHand();
                    if (!this.shopCreationFilter.isProductAllowed(handItem)) {
                        user.sendMessage(this.translations.commandShopFailedProductNotAllowed(user));
                        return SINGLE_FAILED;
                    }

                    final ShopBlock shop = this.resolveShopBlockForCreation(user, targetBlock);
                    if (shop == null) {
                        user.sendMessage(this.translations.commandShopFailedInvalidBlock(user));
                        return SINGLE_FAILED;
                    }

                    if (this.shopCreationFilter.isLimitReached(user)) {
                        user.sendMessage(this.translations.shopCreationFailedLimitReached(user));
                        return SINGLE_FAILED;
                    }

                    final Optional<ShopBlock> existing = this.shopService.findShop(shop.component().location());
                    if (existing.isPresent()) {
                        user.sendMessage(this.translations.shopCreationFailedAlreadyExists(user));
                        return SINGLE_FAILED;
                    }

                    final ShopCreateEvent event = new ShopCreateEvent(
                            Phase.PRE_CANCELLABLE,
                            null,
                            user.qsUser(),
                            shop.component().location()
                    );
                    if (!event.callEvent()) {
                        return SINGLE_FAILED;
                    }

                    final DialogLike dialog = this.creationDialog.createDialog(user, shop);
                    user.showDialog(dialog);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private CommandNode<CommandSourceStack> modificationNode() {
        return Commands.literal("modify")
                .requires(source -> source.getExecutor() instanceof Player player
                        && player.hasPermission(QSPermissions.USE)
                        && player.hasPermission(QSHDialogPermissions.COMMAND_MODIFY)
                )
                .executes(context -> {
                    final Player player = (Player) Objects.requireNonNull(context.getSource().getExecutor());
                    final UserSession user = UserSession.of(player);

                    final Block target = user.targetBlock();
                    if (target == null) {
                        user.sendMessage(this.translations.commandShopFailedNoTargetBlock(user));
                        return SINGLE_FAILED;
                    }

                    final Container container = resolveContainer(target);
                    if (container == null) {
                        user.sendMessage(this.translations.commandShopFailedInvalidBlock(user));
                        return SINGLE_FAILED;
                    }

                    final Optional<ShopBlock> existing = this.shopService.findShop(container.getLocation());
                    if (existing.isEmpty()) {
                        user.sendMessage(this.translations.shopModificationFailedShopNotFound(user));
                        return SINGLE_FAILED;
                    }

                    user.showDialog(this.modificationDialog.createDialog(user, existing.get()));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    // =========================================================================
    // Block resolution helpers
    // =========================================================================

    private @Nullable ShopBlock resolveShopBlockForCreation(final UserSession user, final Block target) {
        if (target.getState() instanceof Container container) {
            return this.buildShopBlockFromContainer(user, container, target);
        }
        if (target.getState() instanceof Sign sign) {
            return this.createShopBlockFromSign(user, sign, target);
        }
        return null;
    }

    private ShopBlock buildShopBlockFromContainer(
            final UserSession user,
            final Container container,
            final Block containerBlock
    ) {
        final Block frontBlock = containerBlock.getRelative(user.direction().getOppositeFace());
        final ItemStack item = user.inventory().getItemInMainHand();
        final ShopComponent shopComponent = ShopComponent.builder()
                .id(ShopComponent.NEW_ID)
                .location(containerBlock.getLocation())
                .owner(user)
                .product(item)
                .price(this.priceAnalytics.priceRange(user, item).min())
                .build();
        return new ShopBlock(container, frontBlock, shopComponent);
    }

    private @Nullable ShopBlock createShopBlockFromSign(
            final UserSession user,
            final Sign sign,
            final Block signBlock
    ) {
        if (!(sign.getBlockData() instanceof WallSign wallSign)) {
            return null;
        }
        final Block wallBlock = signBlock.getRelative(wallSign.getFacing().getOppositeFace());
        if (!(wallBlock.getState() instanceof Container container)) {
            return null;
        }
        final ItemStack item = user.inventory().getItemInMainHand();
        final ShopComponent shopComponent = ShopComponent.builder()
                .id(ShopComponent.NEW_ID)
                .location(wallBlock.getLocation())
                .owner(user)
                .product(item)
                .price(this.priceAnalytics.priceRange(user, item).min())
                .build();
        return new ShopBlock(container, sign.getBlock(), shopComponent);
    }

    private static @Nullable Container resolveContainer(final Block target) {
        if (target.getState() instanceof Container container) {
            return container;
        }
        if (target.getState() instanceof Sign sign && sign.getBlockData() instanceof WallSign wallSign) {
            final Block wallBlock = target.getRelative(wallSign.getFacing().getOppositeFace());
            if (wallBlock.getState() instanceof Container container) {
                return container;
            }
        }
        return null;
    }
}
