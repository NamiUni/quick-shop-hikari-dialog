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
import com.ghostchu.quickshop.api.shop.PriceLimiterCheckResult;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.shop.SimpleShopManager;
import com.ghostchu.quickshop.util.ShopUtil;
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.shop.ShopDisplay;
import io.github.namiuni.qshdialog.shop.ShopMode;
import io.github.namiuni.qshdialog.shop.ShopStatus;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
final class ShopModificationCallback implements DialogActionCallback {

    private final QSHUser user;
    private final Shop shop;
    private final TagResolver shopTags;

    ShopModificationCallback(final QSHUser user, final Shop shop, final TagResolver shopTags) {
        this.user = user;
        this.shop = shop;
        this.shopTags = shopTags;
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

        final PriceLimiterCheckResult priceRange = QuickShop.getInstance().getShopManager().getPriceLimiter()
                .check(this.user.quickShopUser(), this.shop.getItem(), this.shop.getCurrency(), this.shop.getPrice());
        final BigDecimal minPrice = BigDecimal.valueOf(priceRange.getMin());
        final BigDecimal maxPrice = BigDecimal.valueOf(priceRange.getMax());

        final String priceInput = Objects.requireNonNull(response.getText("product_price"));
        if (priceInput.isEmpty()) {
            final Component message = TranslationMessages.shopModificationErrorPriceEmpty(this.user, this.shopTags);
            this.user.sendMessage(message);
        }

        final BigDecimal price;
        try {
            price = new BigDecimal(priceInput);
        } catch (final NumberFormatException exception) {
            final Component errorMessage = TranslationMessages.shopModificationErrorPriceInvalid(this.user, this.shopTags, priceInput);
            this.user.sendMessage(errorMessage);
            return;
        }

        if (minPrice.compareTo(price) <= 0 && price.compareTo(maxPrice) <= 0) {
            ShopUtil.setPrice(QuickShop.getInstance(), this.user.quickShopUser(), price.doubleValue(), this.shop);
        } else {
            final Component errorMessage = TranslationMessages.shopModificationErrorPriceOutOfRange(this.user, this.shopTags, priceInput, minPrice, maxPrice);
            this.user.sendMessage(errorMessage);
            return;
        }

        Optional.ofNullable(response.getText("shop_status"))
                .map(ShopStatus::valueOf)
                .filter(Predicate.isEqual(ShopStatus.UNAVAILABLE))
                .ifPresentOrElse(
                        ignored -> this.shop.shopType(SimpleShopManager.FROZEN_TYPE),
                        () -> {
                            final ShopMode mode = Optional.ofNullable(response.getText("shop_mode"))
                                    .map(ShopMode::valueOf)
                                    .orElseThrow();
                            this.shop.shopType(mode == ShopMode.BUYING ? SimpleShopManager.BUYING_TYPE : SimpleShopManager.SELLING_TYPE);
                        }
                );

        Optional.ofNullable(response.getText("shop_display"))
                .map(ShopDisplay::valueOf)
                .ifPresent(display -> this.shop.setDisableDisplay(display == ShopDisplay.HIDE));

        Optional.ofNullable(response.getText("shop_name"))
                .filter(Predicate.not(Predicate.isEqual(this.shop.getShopName())))
                .ifPresent(name -> {
                    final World world = this.user.quickShopUser().getBukkitPlayer().orElseThrow().getWorld();
                    switch (QuickShopUtil.withdrawNamingCost(this.user, world)) {
                        case Result.Success<BigDecimal, Component>(BigDecimal result) -> {
                            this.shop.setShopName(name);
                            QuickShop.getInstance().text().of(this.user.quickShopUser(), "shop-name-success", name).send();
                        }
                        case Result.Error<BigDecimal, Component>(Component errorMessage) ->
                                this.user.sendMessage(errorMessage);
                    }
                });

        Optional.ofNullable(response.getText("shop_currency"))
                .filter(Predicate.not(Predicate.isEqual(this.shop.getCurrency())))
                .ifPresent(this.shop::setCurrency);

        Optional.ofNullable(response.getBoolean("unlimited_stock"))
                .filter(Predicate.not(Predicate.isEqual(this.shop.isUnlimited())))
                .ifPresent(this.shop::setUnlimited);
    }
}
