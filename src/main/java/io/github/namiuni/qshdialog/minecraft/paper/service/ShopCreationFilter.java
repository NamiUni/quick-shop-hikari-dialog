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
package io.github.namiuni.qshdialog.minecraft.paper.service;

import io.github.namiuni.qshdialog.common.utilities.QSHDialogLogger;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSConfigurations;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ShopCreationFilter {

    private ShopCreationFilter() {
    }

    public static boolean isCreationAllowed(final World world, final ItemStack handItem) {
        return isWorldAllowed(world) && isItemAllowed(handItem);
    }

    public static boolean isLimitReached(final UserSession user) {
        if (!QSConfigurations.isShopLimitEnabled()) {
            return false;
        }
        final int count = currentShopCount(user);
        final int limit = shopLimit(user);
        QSHDialogLogger.logger().debug("{}'s shop count: {}", user.name(), count);
        QSHDialogLogger.logger().debug("{}'s shop limit: {}", user.name(), limit);
        return count >= limit;
    }

    public static int currentShopCount(final UserSession user) {
        final UUID playerUuid = user.uuid();
        final boolean oldAlgorithm = QSConfigurations.isShopLimitOldAlgorithm();
        return (int) QuickShops.shopManager().getAllShops().stream()
                .filter(shop -> playerUuid.equals(shop.getOwner().getUniqueId()))
                .filter(shop -> oldAlgorithm || !shop.isUnlimited())
                .count();
    }

    public static int shopLimit(final UserSession user) {
        return QuickShops.rankLimiter().getShopLimit(user.qsUser());
    }

    private static boolean isWorldAllowed(final World world) {
        final List<String> whitelist = QSConfigurations.whitelistWorlds();
        if (!whitelist.isEmpty()) {
            return whitelist.contains(world.getName());
        }
        return !QSConfigurations.blacklistWorlds().contains(world.getName());
    }

    private static boolean isItemAllowed(final ItemStack handItem) {
        final Material type = handItem.getType();

        final boolean itemBlacklisted = QSConfigurations.blacklistItems().stream()
                .anyMatch(type::equals);
        if (itemBlacklisted) {
            return false;
        }

        final List<String> blacklistLore = QSConfigurations.blacklistLore();
        if (blacklistLore.isEmpty()) {
            return true;
        }
        final List<Component> lore = handItem.lore();
        if (lore == null || lore.isEmpty()) {
            return true;
        }

        final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        return lore.stream()
                .map(serializer::serialize)
                .noneMatch(line -> blacklistLore.stream().anyMatch(line::contains));
    }
}
