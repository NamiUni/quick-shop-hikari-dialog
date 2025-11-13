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
import com.ghostchu.quickshop.util.ShopUtil;
import io.github.namiuni.qshdialog.shop.TradeType;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;
import net.kyori.adventure.audience.Audience;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
final class ShopModificationCallback implements DialogActionCallback {

    private final Shop shop;
    private final QSHUser user;

    ShopModificationCallback(final Shop shop, final QSHUser user) {
        this.shop = shop;
        this.user = user;
    }

    @Override
    public void accept(final DialogResponseView response, final Audience audience) {

        Optional.ofNullable(response.getFloat("product_size"))
                .map(Float::intValue)
                .filter(Predicate.not(Predicate.isEqual(this.shop.getItem().getAmount())))
                .ifPresent(bundleSize -> {
                    final ItemStack product = this.shop.getItem();
                    product.setAmount(bundleSize);
                    this.shop.setItem(product);
                });

        Optional.ofNullable(response.getText("product_price"))
                .map(BigDecimal::new)
                .map(BigDecimal::doubleValue)
                .filter(Predicate.not(Predicate.isEqual(this.shop.getPrice())))
                .ifPresent(price -> ShopUtil.setPrice(QuickShop.getInstance(), this.user.quickShopUser(), price, this.shop));

        Optional.ofNullable(response.getText("trade_type"))
                .map(TradeType::valueOf)
                .map(TradeType::shopType)
                .filter(Predicate.not(Predicate.isEqual(this.shop.shopType())))
                .ifPresent(this.shop::shopType);

        Optional.ofNullable(response.getText("shop_name"))
                .filter(Predicate.not(Predicate.isEqual(this.shop.getShopName())))
                .ifPresent(name -> {
                    this.shop.setShopName(name);

                    final World world = this.user.quickShopUser().getBukkitPlayer().orElseThrow().getWorld();
                    QuickShopUtil.withdrawNamingCost(this.user, world, null);
                });

        Optional.ofNullable(response.getText("shop_currency"))
                .filter(Predicate.not(Predicate.isEqual(this.shop.getCurrency())))
                .ifPresent(this.shop::setCurrency);

        Optional.ofNullable(response.getBoolean("show_display"))
                .map(showDisplay -> !showDisplay)
                .filter(Predicate.not(Predicate.isEqual(this.shop.isDisableDisplay())))
                .ifPresent(this.shop::setDisableDisplay);

        Optional.ofNullable(response.getBoolean("unlimited_stock"))
                .filter(Predicate.not(Predicate.isEqual(this.shop.isUnlimited())))
                .ifPresent(this.shop::setUnlimited);
    }
}
