package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter;

import com.ghostchu.quickshop.api.shop.ItemMatcher;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * ショップのコンテナに対するインベントリ計算を担う。
 */
@NullMarked
public final class ShopInventory {

    private ShopInventory() {
    }

    /**
     * ショップの在庫数を**取引単位**で返す。
     *
     * <p>無限在庫の場合は {@link Integer#MAX_VALUE} を返す。
     */
    public static int stockCount(final ShopBlock shop) {
        if (shop.component().infiniteStock()) {
            return Integer.MAX_VALUE;
        }
        final ItemStack item = shop.component().product();
        final ItemMatcher itemMatcher = QuickShops.itemMatcher();
        int count = 0;
        for (final ItemStack stack : shop.container().getInventory().getStorageContents()) {
            if (stack != null && itemMatcher.matches(item, stack)) {
                count += stack.getAmount();
            }
        }
        return count / item.getAmount();
    }

    /**
     * ショップのコンテナに追加できるスペースを**取引単位**で返す。
     *
     * <p>無限在庫の場合は {@link Integer#MAX_VALUE} を返す。
     */
    public static int spaceCount(final ShopBlock shop) {
        if (shop.component().infiniteStock()) {
            return Integer.MAX_VALUE;
        }
        final ItemStack item = shop.component().product();
        final ItemMatcher itemMatcher = QuickShops.itemMatcher();
        final int maxStackSize = item.getMaxStackSize();
        int space = 0;
        for (final ItemStack stack : shop.container().getInventory().getStorageContents()) {
            if (stack == null) {
                space += maxStackSize;
            } else if (itemMatcher.matches(item, stack) && stack.getAmount() < maxStackSize) {
                space += maxStackSize - stack.getAmount();
            }
        }
        return space / item.getAmount();
    }
}
