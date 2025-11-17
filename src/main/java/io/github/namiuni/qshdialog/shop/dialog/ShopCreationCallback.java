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
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.shop.ContainerShopBuilder;
import io.github.namiuni.qshdialog.shop.ShopDisplay;
import io.github.namiuni.qshdialog.shop.ShopMode;
import io.github.namiuni.qshdialog.shop.ShopStatus;
import io.github.namiuni.qshdialog.shop.Shops;
import io.github.namiuni.qshdialog.shop.policy.ShopCreationContext;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import java.math.BigDecimal;
import java.util.Objects;
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

        final String priceInput = Objects.requireNonNull(response.getText("product_price"));
        if (priceInput.isEmpty()) {
            final Component message = TranslationMessages.shopCreationErrorPriceEmpty(this.context.owner(), priceInput);
            this.context.owner().sendMessage(message);
        }

        final BigDecimal price;
        try {
            price = new BigDecimal(priceInput);
        } catch (final NumberFormatException exception) {
            final Component errorMessage = TranslationMessages.shopCreationErrorPriceInvalid(this.context.owner(), priceInput);
            this.context.owner().sendMessage(errorMessage);
            return;
        }

        if (this.minPrice.compareTo(price) <= 0 && price.compareTo(this.maxPrice) <= 0) {
            builder.price(price);
        } else {
            final Component errorMessage = TranslationMessages.shopCreationErrorPriceOutOfRange(this.context.owner(), priceInput, this.minPrice, this.maxPrice);
            this.context.owner().sendMessage(errorMessage);
            return;
        }

        Optional.ofNullable(response.getText("shop_mode"))
                .map(ShopMode::valueOf)
                .ifPresent(builder::mode);

        Optional.ofNullable(response.getText("shop_status"))
                .map(ShopStatus::valueOf)
                .ifPresent(builder::status);

        Optional.ofNullable(response.getText("shop_display"))
                .map(ShopDisplay::valueOf)
                .ifPresent(builder::display);

        Optional.ofNullable(response.getText("shop_name"))
                .ifPresent(name -> {
                    final World world = this.context.owner().quickShopUser().getBukkitPlayer().orElseThrow().getWorld();
                    switch (QuickShopUtil.withdrawNamingCost(this.context.owner(), world)) {
                        case Result.Success<BigDecimal, Component>(BigDecimal result) -> builder.shopName(name);
                        case Result.Error<BigDecimal, Component>(Component errorMessage) ->
                            this.context.owner().sendMessage(errorMessage);
                    }
                });

        Optional.ofNullable(response.getText("shop_currency"))
                .ifPresent(builder::currency);

        Optional.ofNullable(response.getBoolean("unlimited_stock"))
                .ifPresent(builder::unlimited);

        final Shop shop = builder.build();
        final Block signLocationBlock = this.context.shopContainer().getBlock().getRelative(this.context.shopFace());

        QuickShop.getInstance().getShopManager().createShop(shop, signLocationBlock, false);
    }
}
