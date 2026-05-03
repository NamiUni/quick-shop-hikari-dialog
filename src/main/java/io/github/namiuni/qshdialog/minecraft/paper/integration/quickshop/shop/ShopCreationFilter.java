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

import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class ShopCreationFilter {

    private final ComponentLogger logger;
    private final ShopCounter shopCounter;
    private final QSConfiguration qsConfig;

    @Inject
    ShopCreationFilter(
            final ComponentLogger logger,
            final ShopCounter shopCounter,
            final QSConfiguration qsConfig
    ) {
        this.logger = logger;
        this.shopCounter = shopCounter;
        this.qsConfig = qsConfig;
    }

    public boolean isLimitReached(final UserSession user) {
        if (!this.qsConfig.isShopLimitEnabled()) {
            return false;
        }
        final int current = this.shopCounter.currentShops(user);
        final int maximum = this.shopCounter.maximumShops(user);
        this.logger.debug("{}'s current shops: {}", user.name(), current);
        this.logger.debug("{}'s maximum shops: {}", user.name(), maximum);
        return current >= maximum;
    }

    public boolean isWorldAllowed(final World world) {
        final List<String> whitelist = this.qsConfig.whitelistWorlds();
        if (!whitelist.isEmpty()) {
            return whitelist.contains(world.getName());
        }
        return !this.qsConfig.blacklistWorlds().contains(world.getName());
    }

    public boolean isProductAllowed(final ItemStack handItem) {
        final Material type = handItem.getType();

        final boolean itemBlacklisted = this.qsConfig.blacklistItems().stream()
                .anyMatch(type::equals);
        if (itemBlacklisted) {
            return false;
        }

        final List<String> blacklistLore = this.qsConfig.blacklistLore();
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

    public boolean isBlockAllowed(final Block block) {
        return this.qsConfig.shopBlocks().contains(block.getType()) && block.getState() instanceof Container;
    }
}
