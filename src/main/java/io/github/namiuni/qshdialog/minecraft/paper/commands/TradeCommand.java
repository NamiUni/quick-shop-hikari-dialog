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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.dialogs.TradePurchaseDialogFactory;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.dialogs.TradeSellDialogFactory;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TradeCommand implements CommandFactory {

    private final TranslationService translations;
    private final ShopService shopService;
    private final TradePurchaseDialogFactory purchaseDialog;
    private final TradeSellDialogFactory sellDialog;

    @Inject
    TradeCommand(
            final TranslationService translations,
            final ShopService shopService,
            final TradePurchaseDialogFactory purchaseDialog,
            final TradeSellDialogFactory sellDialog
    ) {
        this.translations = translations;
        this.shopService = shopService;
        this.purchaseDialog = purchaseDialog;
        this.sellDialog = sellDialog;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("tradedialog")
                .requires(source -> source.getExecutor() instanceof Player player
                        && player.hasPermission(QSPermissions.USE)
                        && player.hasPermission(QSHDialogPermissions.COMMAND_TRADE)
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

                    final Optional<ShopBlock> shopOpt = this.shopService.findShop(container.getLocation());
                    if (shopOpt.isEmpty()) {
                        user.sendMessage(this.translations.shopModificationFailedShopNotFound(user));
                        return SINGLE_FAILED;
                    }

                    final ShopBlock shop = shopOpt.get();
                    if (!shop.component().available()) {
                        user.sendMessage(this.translations.tradeFailedShopUnavailable(user));
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

    // =========================================================================
    // Block resolution helpers
    // =========================================================================

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
