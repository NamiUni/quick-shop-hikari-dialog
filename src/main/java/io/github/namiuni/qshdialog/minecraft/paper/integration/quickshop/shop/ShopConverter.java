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

import com.ghostchu.quickshop.api.shop.IShopType;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.permission.BuiltInShopPermissionGroup;
import com.ghostchu.quickshop.shop.SimpleShopManager;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.AccessLevel;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeType;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ShopConverter {

    private ShopConverter() {
    }

    // -------------------------------------------------------------------------
    // Shop
    // -------------------------------------------------------------------------

    public static ShopBlock toShopBlock(final Shop qsShop) {
        final ShopComponent shopComponent = toShopComponent(qsShop);
        final BlockState blockState = shopComponent.location().getBlock().getState();
        if (!(blockState instanceof Container container)) {
            throw new IllegalStateException(
                    "Shop at " + shopComponent.location() + " is not backed by a Container");
        }
        final Sign sign = qsShop.getSigns().getFirst();
        return new ShopBlock(container, sign.getBlock(), shopComponent);
    }

    public static ShopComponent toShopComponent(final Shop qsShop) {
        final TradeType tradeType = qsShop.shopType() == SimpleShopManager.SELLING_TYPE
                ? TradeType.SELLING
                : TradeType.BUYING;

        final Map<UUID, AccessLevel> accessLevels = qsShop.getPermissionAudiences().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> toAccessLevel(entry.getValue())
                ));

        return ShopComponent.builder()
                .id(qsShop.getShopId())
                .location(qsShop.getLocation())
                .owner(UserSession.of(qsShop.getOwner()))
                .product(qsShop.getItem())
                .name(qsShop.getShopName())
                .tradeType(tradeType)
                .currency(qsShop.getCurrency())
                .price(BigDecimal.valueOf(qsShop.getPrice()))
                .available(qsShop.shopType() != SimpleShopManager.FROZEN_TYPE)
                .displayVisible(!qsShop.isDisableDisplay())
                .infiniteStock(qsShop.isUnlimited())
                .accessLevels(accessLevels)
                .build();
    }

    public static IShopType toQSShopType(final ShopComponent shopComponent) {
        if (!shopComponent.available()) {
            return SimpleShopManager.FROZEN_TYPE;
        }
        return switch (shopComponent.tradeType()) {
            case SELLING -> SimpleShopManager.SELLING_TYPE;
            case BUYING -> SimpleShopManager.BUYING_TYPE;
        };
    }

    // -------------------------------------------------------------------------
    // AccessLevel
    // -------------------------------------------------------------------------

    public static Map<UUID, String> toQSPermissionGroups(final Map<UUID, AccessLevel> accessLevels) {
        return accessLevels.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), toQSPermissionGroup(entry.getValue()).getNamespacedNode()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static BuiltInShopPermissionGroup toQSPermissionGroup(final AccessLevel level) {
        return switch (level) {
            case ADMINISTRATOR -> BuiltInShopPermissionGroup.ADMINISTRATOR;
            case STAFF -> BuiltInShopPermissionGroup.STAFF;
            case EVERYONE -> BuiltInShopPermissionGroup.EVERYONE;
            case BLOCKED -> BuiltInShopPermissionGroup.BLOCKED;
        };
    }

    private static AccessLevel toAccessLevel(final String namespacedNode) {
        if (Objects.equals(namespacedNode, BuiltInShopPermissionGroup.ADMINISTRATOR.getNamespacedNode())) {
            return AccessLevel.ADMINISTRATOR;
        }
        if (Objects.equals(namespacedNode, BuiltInShopPermissionGroup.STAFF.getNamespacedNode())) {
            return AccessLevel.STAFF;
        }
        if (Objects.equals(namespacedNode, BuiltInShopPermissionGroup.BLOCKED.getNamespacedNode())) {
            return AccessLevel.BLOCKED;
        }
        return AccessLevel.EVERYONE;
    }
}
