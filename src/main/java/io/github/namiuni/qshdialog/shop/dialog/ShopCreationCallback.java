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
package io.github.namiuni.qshdialog.shop.dialog;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.shop.Shop;
import io.github.namiuni.qshdialog.shop.ContainerShopBuilder;
import io.github.namiuni.qshdialog.shop.Shops;
import io.github.namiuni.qshdialog.shop.TradeType;
import io.github.namiuni.qshdialog.shop.policy.ShopCreationContext;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import java.math.BigDecimal;
import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
final class ShopCreationCallback implements DialogActionCallback {

    private final ShopCreationContext context;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;

    ShopCreationCallback(final ShopCreationContext context, final BigDecimal minPrice, final BigDecimal maxPrice) {
        this.context = context;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public void accept(final DialogResponseView response, final Audience audience) {
        final ContainerShopBuilder builder = Shops.container(this.context.shopContainer())
                .owner(this.context.owner().quickShopUser())
                .product(this.context.product());

        Optional.ofNullable(response.getFloat("product_size"))
                .ifPresent(bundleSize -> builder.bundleSize(bundleSize.intValue()));

        Optional.ofNullable(response.getText("product_price"))
                .filter(price -> !price.isEmpty())
                .map(BigDecimal::new)
                .map(BigDecimal::doubleValue)
                .ifPresentOrElse(
                        builder::price,
                        () -> {
                            final Component label = TranslationMessages.productPriceLabel(this.context.owner(), this.minPrice, this.maxPrice);
                            final Component message = TranslationMessages.shopCreationErrorEmptyInput(this.context.owner(), label);
                            this.context.owner().sendMessage(message);
                        });

        Optional.ofNullable(response.getText("trade_type"))
                .map(TradeType::valueOf)
                .ifPresent(builder::type);

        Optional.ofNullable(response.getText("shop_name"))
                .ifPresent(name -> {
                    builder.name(name);

                    final World world = this.context.owner().quickShopUser().getBukkitPlayer().orElseThrow().getWorld();
                    QuickShopUtil.withdrawNamingCost(this.context.owner(), world, null);
                });

        Optional.ofNullable(response.getText("shop_currency"))
                .ifPresent(builder::currency);

        Optional.ofNullable(response.getBoolean("show_display"))
                .ifPresent(builder::showDisplay);

        Optional.ofNullable(response.getBoolean("unlimited_stock"))
                .ifPresent(builder::unlimited);

        final Shop shop = builder.build();
        final Block signLocationBlock = this.context.shopContainer().getBlock().getRelative(this.context.shopFace());

        QuickShop.getInstance().getShopManager().createShop(shop, signLocationBlock, false);
    }
}
