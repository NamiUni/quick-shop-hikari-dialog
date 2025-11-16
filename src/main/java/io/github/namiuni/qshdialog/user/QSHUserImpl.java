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
package io.github.namiuni.qshdialog.user;

import com.ghostchu.quickshop.api.obj.QUser;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.obj.QUserImpl;
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.shop.dialog.ProductPurchaseDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ProductSaleDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopCreationDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopModificationDialogFactory;
import io.github.namiuni.qshdialog.shop.policy.ShopCreationContext;
import io.papermc.paper.dialog.Dialog;
import java.util.Objects;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record QSHUserImpl(
        Player player,
        ShopCreationDialogFactory shopCreationDialogFactory,
        ShopModificationDialogFactory shopModificationDialogFactory,
        ProductPurchaseDialogFactory productPurchaseDialogFactory,
        ProductSaleDialogFactory productSaleDialogFactory
) implements QSHUser, ForwardingAudience.Single {

    @Override
    public Audience audience() {
        return this.player;
    }

    @Override
    public QUser quickShopUser() {
        return QUserImpl.createFullFilled(this.player);
    }

    @Override
    public void showShopCreationDialog(final Container container) {
        final BlockFace signCreationFace;
        if (container.getBlockData() instanceof Directional directional) {
            signCreationFace = directional.getFacing();
        } else {
            final int interactionRange = (int) this.getBlockInteractionRange();
            final BlockFace blockFace = this.player.getTargetBlockFace(interactionRange);
            signCreationFace = Objects.requireNonNullElse(blockFace, this.getDirection().getOppositeFace());
        }

        final ShopCreationContext context = new ShopCreationContext(this, this.player.getInventory().getItemInMainHand(), container, signCreationFace);
        final Dialog dialog = this.shopCreationDialogFactory.create(context);
        this.showDialog(dialog);
    }

    @Override
    public void showShopModificationDialog(final Shop shop) {
        final Dialog dialog = this.shopModificationDialogFactory.create(shop, this);
        this.showDialog(dialog);
    }

    @Override
    public void showProductPurchaseDialog(final Shop shop) {
        final Dialog dialog = this.productPurchaseDialogFactory.create(this, shop);
        this.showDialog(dialog);
    }

    @Override
    public void showProductSaleDialog(final Shop shop) {
        final Result<Dialog, Component> result = this.productSaleDialogFactory.create(this, shop);
        switch (result) {
            case Result.Success<Dialog, Component>(Dialog dialog) -> this.showDialog(dialog);
            case Result.Error<Dialog, Component>(Component errorMessage) -> this.sendActionBar(errorMessage);
        }
    }

    private double getBlockInteractionRange() {
        final AttributeInstance clickDistance = Objects.requireNonNull(this.player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE));
        return clickDistance.getValue();
    }

    private BlockFace getDirection() {
        final float yaw = ((this.player.getYaw() % 360) + 360) % 360;

        return switch ((int) ((yaw + 45) / 90) % 4) {
            case 1 -> BlockFace.WEST;
            case 2 -> BlockFace.NORTH;
            case 3 -> BlockFace.EAST;
            default -> BlockFace.SOUTH;
        };
    }
}
