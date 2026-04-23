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
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradePurchaseDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradeSellDialog;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.permission.Permissions;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopCreationFilter;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopService;
import io.github.namiuni.qshdialog.minecraft.paper.translation.Translations;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.List;
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
public final class ShopCommand implements QSHCommand {

    private final Translations translations;
    private final ShopService shopService;
    private final ShopCreationDialog creationDialog;
    private final ShopModificationDialog modificationDialog;
    private final TradePurchaseDialog purchaseDialog;
    private final TradeSellDialog sellDialog;

    public ShopCommand(
            final Translations translations,
            final ShopService shopService,
            final ShopCreationDialog creationDialog,
            final ShopModificationDialog modificationDialog,
            final TradePurchaseDialog purchaseDialog,
            final TradeSellDialog sellDialog
    ) {
        this.translations = translations;
        this.shopService = shopService;
        this.creationDialog = creationDialog;
        this.modificationDialog = modificationDialog;
        this.purchaseDialog = purchaseDialog;
        this.sellDialog = sellDialog;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("shopdialog")
                .then(this.createNode())
                .then(this.modificationNode())
                .then(this.tradeNode())
                .build();
    }

    @Override
    public List<String> aliases() {
        return QSHCommand.super.aliases();
    }

    @Override
    public String description() {
        return QSHCommand.super.description();
    }

    // -------------------------------------------------------------------------

    private CommandNode<CommandSourceStack> createNode() {
        return Commands.literal("create")
                .requires(source -> source.getExecutor() instanceof Player player
                        && player.hasPermission(QSPermissions.USE)
                        && player.hasPermission(Permissions.COMMAND_CREATE)
                )
                .executes(context -> {
                    final Player player = (Player) Objects.requireNonNull(context.getSource().getExecutor());
                    final UserSession user = UserSession.of(player);

                    final Block target = user.targetBlock();
                    if (target == null) {
                        user.sendMessage(this.translations.shopCommandNoTargetBlock(user));
                        return SINGLE_FAILED;
                    }

                    final ItemStack handItem = user.inventory().getItemInMainHand();
                    if (!ShopCreationFilter.isCreationAllowed(target.getWorld(), handItem)) {
                        return SINGLE_FAILED;
                    }

                    if (ShopCreationFilter.isLimitReached(user)) {
                        user.sendMessage(this.translations.shopCreationLimitReached(user));
                        return SINGLE_FAILED;
                    }

                    final ShopBlock shop = resolveShopBlockForCreation(user, target);
                    if (shop == null) {
                        user.sendMessage(this.translations.shopCommandInvalidBlock(user));
                        return SINGLE_FAILED;
                    }

                    final Optional<ShopBlock> existing = this.shopService.findShop(shop.container().getLocation());
                    if (existing.isPresent()) {
                        user.sendMessage(this.translations.shopCreationCommandAlreadyExists(user));
                        return SINGLE_FAILED;
                    }

                    final ShopCreateEvent event = new ShopCreateEvent(Phase.PRE_CANCELLABLE, null, user.qsUser(), shop.component().location());
                    if (event.callEvent()) {
                        final DialogLike dialog = this.creationDialog.createDialog(user, shop);
                        user.showDialog(dialog);
                        return Command.SINGLE_SUCCESS;
                    }

                    return SINGLE_FAILED;
                })
                .build();
    }

    private CommandNode<CommandSourceStack> modificationNode() {
        return Commands.literal("modify")
                .requires(source -> source.getExecutor() instanceof Player player
                        && player.hasPermission(QSPermissions.USE)
                        && player.hasPermission(Permissions.COMMAND_MODIFY)
                )
                .executes(context -> {
                    final Player player = (Player) Objects.requireNonNull(context.getSource().getExecutor());
                    final UserSession user = UserSession.of(player);

                    final Block target = user.targetBlock();
                    if (target == null) {
                        user.sendMessage(this.translations.shopCommandNoTargetBlock(user));
                        return SINGLE_FAILED;
                    }

                    final Container container = resolveContainer(target);
                    if (container == null) {
                        user.sendMessage(this.translations.shopCommandInvalidBlock(user));
                        return SINGLE_FAILED;
                    }

                    final Optional<ShopBlock> existing = this.shopService.findShop(container.getLocation());
                    if (existing.isEmpty()) {
                        user.sendMessage(this.translations.shopModificationCommandShopNotFound(user));
                        return SINGLE_FAILED;
                    }

                    user.showDialog(this.modificationDialog.createDialog(user, existing.get()));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private CommandNode<CommandSourceStack> tradeNode() {
        return Commands.literal("trade")
                .requires(source -> source.getExecutor() instanceof Player player
                        && player.hasPermission(QSPermissions.USE)
                        && player.hasPermission(Permissions.COMMAND_TRADE)
                )
                .executes(context -> {
                    final Player player = (Player) Objects.requireNonNull(context.getSource().getExecutor());
                    final UserSession user = UserSession.of(player);

                    final Block target = user.targetBlock();
                    if (target == null) {
                        user.sendMessage(this.translations.shopCommandNoTargetBlock(user));
                        return SINGLE_FAILED;
                    }

                    final Container container = resolveContainer(target);
                    if (container == null) {
                        user.sendMessage(this.translations.shopCommandInvalidBlock(user));
                        return SINGLE_FAILED;
                    }

                    final Optional<ShopBlock> shopOpt = this.shopService.findShop(container.getLocation());
                    if (shopOpt.isEmpty()) {
                        user.sendMessage(this.translations.shopModificationCommandShopNotFound(user));
                        return SINGLE_FAILED;
                    }

                    final ShopBlock shop = shopOpt.get();
                    if (!shop.component().available()) {
                        user.sendMessage(this.translations.tradeErrorShopUnavailable(user));
                        return SINGLE_FAILED;
                    }

                    final DialogLike dialog = switch (shop.component().tradeType()) {
                        case SELLING -> this.purchaseDialog.createDialog(user, shop);
                        case BUYING -> this.sellDialog.createDialog(user, shop);
                    };

                    if (dialog == null) {
                        return SINGLE_FAILED;
                    }

                    user.showDialog(dialog);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    // -------------------------------------------------------------------------
    // Block resolution helpers
    // -------------------------------------------------------------------------

    private static @Nullable ShopBlock resolveShopBlockForCreation(final UserSession user, final Block target) {
        if (target.getState() instanceof Container container) {
            return buildShopBlockFromContainer(user, container, target);
        }
        if (target.getState() instanceof Sign sign) {
            return createShopBlockFromSign(user, sign, target);
        }
        return null;
    }

    private static ShopBlock buildShopBlockFromContainer(
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
                .price(PriceAnalytics.getPriceLimit(user, item).min())
                .build();
        return new ShopBlock(container, frontBlock, shopComponent);
    }

    private static @Nullable ShopBlock createShopBlockFromSign(
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
                .price(PriceAnalytics.getPriceLimit(user, item).min())
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
