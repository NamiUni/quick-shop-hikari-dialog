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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class QSConfigurations {

    private QSConfigurations() {
    }

    public static boolean supportsMultiCurrency() {
        return QuickShops.economyProvider().multiCurrency();
    }

    public static boolean supportsBulkTransaction() {
        return QuickShops.configuration().getBoolean("shop.allow-stacks");
    }

    public static int shopNameMaxLength() {
        return QuickShops.configuration().getInt("shop.name-max-length", 32);
    }

    public static BigDecimal shopNamingCost() {
        return BigDecimal.valueOf(QuickShops.configuration().getDouble("shop.name-fee", 0.0));
    }

    public static BigDecimal shopCreateCost() {
        return BigDecimal.valueOf(QuickShops.configuration().getDouble("shop.cost", 10.0));
    }

    public static BigDecimal shopPriceChangeCost() {
        if (!QuickShops.configuration().getBoolean("price-change-requires-fee", true)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(QuickShops.configuration().getDouble("shop.fee-for-price-change", 50.0));
    }

    public static @Nullable String getCurrency() {
        return QuickShops.configuration().getString("currency");
    }

    public static boolean requiresUnlimitedOwnerPayment() {
        return QuickShops.configuration().getBoolean("shop.pay-unlimited-shop-owners");
    }

    public static Set<Material> shopBlocks() {
        return QuickShops.configuration().getStringList("shop-blocks").stream()
                .map(Material::matchMaterial)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static List<String> whitelistWorlds() {
        return QuickShops.configuration().getStringList("shop.whitelist-world");
    }

    public static List<String> blacklistWorlds() {
        return QuickShops.configuration().getStringList("shop.blacklist-world");
    }

    public static List<Material> blacklistItems() {
        return QuickShops.configuration().getStringList("blacklist").stream()
                .map(Material::matchMaterial)
                .filter(Objects::nonNull)
                .toList();
    }

    public static List<String> blacklistLore() {
        return QuickShops.configuration().getStringList("shop.blacklist-lores");
    }
}
