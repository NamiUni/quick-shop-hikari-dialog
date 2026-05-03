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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade;

import com.ghostchu.quickshop.api.shop.ItemMatcher;
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopInventory;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TradeQuantityCalculator {

    private final ShopInventory shopInventory;
    private final ItemMatcher itemMatcher;
    private final EconomyService economyService;
    private final QSConfiguration qsConfig;

    @Inject
    TradeQuantityCalculator(
            final ShopInventory shopInventory,
            final ItemMatcher itemMatcher,
            final EconomyService economyService,
            final QSConfiguration qsConfig
    ) {
        this.shopInventory = shopInventory;
        this.itemMatcher = itemMatcher;
        this.economyService = economyService;
        this.qsConfig = qsConfig;
    }

    public Result<Integer, TradeQuantityFailure> purchasableQuantity(
            final UserSession customer,
            final ShopBlock shop
    ) {
        final Player bukkitCustomer = customer.bukkit().orElseThrow();
        final ShopComponent shopComponent = shop.component();

        final int shopStock = this.shopInventory.stockCount(shop);
        if (!shopComponent.infiniteStock() && shopStock < 1) {
            return Result.error(TradeQuantityFailure.SHOP_OUT_OF_STOCK);
        }

        final int customerSpace = this.inventorySpaceCount(bukkitCustomer.getInventory(), shopComponent.product());
        if (customerSpace < 1) {
            return Result.error(TradeQuantityFailure.CUSTOMER_INVENTORY_FULL);
        }

        final BigDecimal balance = this.economyService.getBalance(
                customer,
                shopComponent.location().getWorld().getName(),
                shopComponent.currency()
        );
        final int affordable = affordableQuantity(balance, shopComponent.price());
        if (affordable < 1) {
            return Result.error(TradeQuantityFailure.CUSTOMER_INSUFFICIENT_FUNDS);
        }

        final int quantity = shopComponent.infiniteStock()
                ? Math.min(customerSpace, affordable)
                : Math.min(Math.min(shopStock, customerSpace), affordable);

        return Result.success(quantity);
    }

    private int inventorySpaceCount(final PlayerInventory inventory, final ItemStack item) {
        final int maxStackSize = item.getMaxStackSize();
        int space = 0;
        for (final ItemStack stack : inventory.getStorageContents()) {
            if (stack == null) {
                space += maxStackSize;
            } else if (this.itemMatcher.matches(item, stack) && stack.getAmount() < maxStackSize) {
                space += maxStackSize - stack.getAmount();
            }
        }
        return space / item.getAmount();
    }

    public Result<Integer, TradeQuantityFailure> sellableQuantity(
            final UserSession customer,
            final ShopBlock shop
    ) {
        final ShopComponent shopComponent = shop.component();

        final int shopSpace = this.shopInventory.spaceCount(shop);
        if (!shopComponent.infiniteStock() && shopSpace < 1) {
            return Result.error(TradeQuantityFailure.SHOP_INVENTORY_FULL);
        }

        final boolean requiresOwnerBalanceCheck = !shopComponent.infiniteStock()
                || this.qsConfig.requiresUnlimitedOwnerPayment();

        final BigDecimal balance = this.economyService.getBalance(
                customer,
                shopComponent.location().getWorld().getName(),
                shopComponent.currency()
        );
        final int ownerAffordable = affordableQuantity(balance, shopComponent.price());
        if (requiresOwnerBalanceCheck && ownerAffordable < 1) {
            return Result.error(TradeQuantityFailure.SHOP_INSUFFICIENT_FUNDS);
        }

        final int customerItems = matchingItemCount(
                customer.inventory(),
                shopComponent.product(),
                this.itemMatcher
        );
        if (customerItems < 1) {
            return Result.error(TradeQuantityFailure.CUSTOMER_INSUFFICIENT_ITEMS);
        }

        final int quantity;
        if (shopComponent.infiniteStock()) {
            quantity = requiresOwnerBalanceCheck
                    ? Math.min(customerItems, ownerAffordable)
                    : customerItems;
        } else {
            quantity = Math.min(customerItems, Math.min(shopSpace, ownerAffordable));
        }

        return Result.success(quantity);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private static int affordableQuantity(final BigDecimal balance, final BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            return Integer.MAX_VALUE;
        }
        final BigDecimal quotient = balance.divide(price, 0, RoundingMode.FLOOR);
        if (quotient.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) >= 0) {
            return Integer.MAX_VALUE;
        }
        return quotient.intValue();
    }

    private static int matchingItemCount(
            final PlayerInventory inventory,
            final ItemStack item,
            final ItemMatcher itemMatcher
    ) {
        int count = 0;
        for (final ItemStack stack : inventory.getStorageContents()) {
            if (stack != null && itemMatcher.matches(item, stack)) {
                count += stack.getAmount();
            }
        }
        return count / item.getAmount();
    }
}
