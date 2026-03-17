package io.github.namiuni.qshdialog.minecraft.paper.service;

import com.ghostchu.quickshop.api.shop.ItemMatcher;
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSConfigurations;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopInventory;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TradeQuantityCalculator {

    private TradeQuantityCalculator() {
    }

    public static Result<Integer, TradeQuantityFailure> purchasableQuantity(
            final UserSession customer,
            final ShopBlock shop
    ) {
        final Player bukkitCustomer = customer.bukkit().orElseThrow();
        final ShopComponent shopComponent = shop.component();

        final int shopStock = ShopInventory.stockCount(shop);
        if (!shopComponent.infiniteStock() && shopStock < 1) {
            return Result.error(TradeQuantityFailure.SHOP_OUT_OF_STOCK);
        }

        final int customerSpace = inventorySpaceCount(bukkitCustomer.getInventory(), shopComponent.product());
        if (customerSpace < 1) {
            return Result.error(TradeQuantityFailure.CUSTOMER_INVENTORY_FULL);
        }

        final int affordable = affordableQuantity(
                customer.balance(shopComponent.location().getWorld().getName(), shopComponent.currency()),
                shopComponent.price());
        if (affordable < 1) {
            return Result.error(TradeQuantityFailure.CUSTOMER_INSUFFICIENT_FUNDS);
        }

        final int quantity = shopComponent.infiniteStock()
                ? Math.min(customerSpace, affordable)
                : Math.min(Math.min(shopStock, customerSpace), affordable);

        return Result.success(quantity);
    }

    public static Result<Integer, TradeQuantityFailure> sellableQuantity(
            final UserSession customer,
            final ShopBlock shop
    ) {
        final Player bukkitCustomer = customer.bukkit().orElseThrow();
        final ShopComponent shopComponent = shop.component();

        final int shopSpace = ShopInventory.spaceCount(shop);
        if (!shopComponent.infiniteStock() && shopSpace < 1) {
            return Result.error(TradeQuantityFailure.SHOP_INVENTORY_FULL);
        }

        final boolean requiresOwnerBalanceCheck = !shopComponent.infiniteStock()
                || QSConfigurations.requiresUnlimitedOwnerPayment();

        final int ownerAffordable = affordableQuantity(
                shopComponent.owner().balance(shop.container().getWorld().getName(), shopComponent.currency()),
                shopComponent.price());
        if (requiresOwnerBalanceCheck && ownerAffordable < 1) {
            return Result.error(TradeQuantityFailure.SHOP_INSUFFICIENT_FUNDS);
        }

        final int customerItems = matchingItemCount(
                bukkitCustomer.getInventory(),
                shopComponent.product(),
                QuickShops.itemMatcher());
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
        return balance.divide(price, 0, RoundingMode.FLOOR).intValue();
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

    private static int inventorySpaceCount(final PlayerInventory inventory, final ItemStack item) {
        final ItemMatcher itemMatcher = QuickShops.itemMatcher();
        final int maxStackSize = item.getMaxStackSize();
        int space = 0;
        for (final ItemStack stack : inventory.getStorageContents()) {
            if (stack == null) {
                space += maxStackSize;
            } else if (itemMatcher.matches(item, stack) && stack.getAmount() < maxStackSize) {
                space += maxStackSize - stack.getAmount();
            }
        }
        return space / item.getAmount();
    }
}
