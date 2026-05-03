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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop;

import com.ghostchu.quickshop.api.shop.ItemMatcher;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class ShopInventory {

    private final ItemMatcher itemMatcher;

    @Inject
    ShopInventory(final ItemMatcher itemMatcher) {
        this.itemMatcher = itemMatcher;
    }

    public int stockCount(final ShopBlock shop) {
        if (shop.component().infiniteStock()) {
            return Integer.MAX_VALUE;
        }
        final ItemStack item = shop.component().product();
        int count = 0;
        for (final ItemStack stack : shop.container().getInventory().getStorageContents()) {
            if (stack != null && this.itemMatcher.matches(item, stack)) {
                count += stack.getAmount();
            }
        }
        return count / item.getAmount();
    }

    public int spaceCount(final ShopBlock shop) {
        if (shop.component().infiniteStock()) {
            return Integer.MAX_VALUE;
        }
        final ItemStack item = shop.component().product();
        final int maxStackSize = item.getMaxStackSize();
        int space = 0;
        for (final ItemStack stack : shop.container().getInventory().getStorageContents()) {
            if (stack == null) {
                space += maxStackSize;
            } else if (this.itemMatcher.matches(item, stack) && stack.getAmount() < maxStackSize) {
                space += maxStackSize - stack.getAmount();
            }
        }
        return space / item.getAmount();
    }
}
