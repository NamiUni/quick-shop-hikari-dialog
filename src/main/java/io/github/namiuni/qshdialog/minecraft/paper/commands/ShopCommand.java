package io.github.namiuni.qshdialog.minecraft.paper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopCreationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopModificationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopService;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.List;
import java.util.Optional;
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

    private final ShopService shopService;
    private final ShopCreationDialog creationDialog;
    private final ShopModificationDialog modificationDialog;

    public ShopCommand(
            final ShopService shopService,
            final ShopCreationDialog creationDialog,
            final ShopModificationDialog modificationDialog
    ) {
        this.shopService = shopService;
        this.creationDialog = creationDialog;
        this.modificationDialog = modificationDialog;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("dialogshop")
                .then(this.createNode())
                .then(this.modificationNode())
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
                .executes(context -> {
                    if (!(context.getSource().getExecutor() instanceof Player player)) {
                        return SINGLE_FAILED;
                    }
                    final UserSession user = UserSession.of(player);
                    final Block target = user.targetBlock();
                    if (target == null) {
                        // TODO: エラーメッセージ送信（見ているブロックがない）
                        return SINGLE_FAILED;
                    }

                    final ShopBlock shop = resolveShopEntityForCreation(user, target);
                    if (shop == null) {
                        // TODO: エラーメッセージ送信（コンテナでも看板でもない）
                        return SINGLE_FAILED;
                    }

                    final Optional<ShopBlock> existing = this.shopService.findShop(shop.container().getLocation());
                    if (existing.isPresent()) {
                        // TODO: エラーメッセージ送信（すでにショップが存在する）
                        return SINGLE_FAILED;
                    }

                    user.showDialog(this.creationDialog.createDialog(user, shop));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private CommandNode<CommandSourceStack> modificationNode() {
        return Commands.literal("modify")
                .executes(context -> {
                    if (!(context.getSource().getExecutor() instanceof Player player)) {
                        return SINGLE_FAILED;
                    }

                    final UserSession user = UserSession.of(player);
                    final Block target = user.targetBlock();
                    if (target == null) {
                        // TODO: エラーメッセージ送信（見ているブロックがない）
                        return SINGLE_FAILED;
                    }

                    final Container container = resolveContainer(target);
                    if (container == null) {
                        // TODO: エラーメッセージ送信（コンテナでも看板でもない）
                        return SINGLE_FAILED;
                    }

                    final Optional<ShopBlock> existing = this.shopService.findShop(container.getLocation());
                    if (existing.isEmpty()) {
                        // TODO: エラーメッセージ送信（ショップが存在しない）
                        return SINGLE_FAILED;
                    }

                    user.showDialog(this.modificationDialog.createDialog(user, existing.get()));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    // -------------------------------------------------------------------------
    // Block resolution helpers
    // -------------------------------------------------------------------------

    private static @Nullable ShopBlock resolveShopEntityForCreation(final UserSession user, final Block target) {
        if (target.getState() instanceof Container container) {
            return buildShopEntityFromContainer(user, container, target);
        }
        if (target.getState() instanceof Sign sign) {
            return buildShopEntityFromSign(user, sign, target);
        }
        return null;
    }

    private static ShopBlock buildShopEntityFromContainer(
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

    private static @Nullable ShopBlock buildShopEntityFromSign(
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
