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

import io.github.namiuni.qshdialog.minecraft.paper.configuration.serializer.BigDecimalSerializer;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.serializer.MaterialSerializer;
import io.leangen.geantyref.TypeToken;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@NullMarked
public final class QSConfigurations {

    private static final YamlConfigurationLoader LOADER = YamlConfigurationLoader.builder()
            .path(QuickShops.quickShop().getJavaPlugin().getDataPath().resolve("config.yml"))
            .defaultOptions(options -> options
                    .serializers(builder -> builder
                            .register(Material.class, MaterialSerializer.INSTANCE)
                            .register(BigDecimal.class, BigDecimalSerializer.INSTANCE)
                    ))
            .build();
    private static final AtomicReference<ConfigurationNode> CONFIG_NODE;

    static {
        try {
            CONFIG_NODE = new AtomicReference<>(LOADER.load());
        } catch (final ConfigurateException exception) {
            throw new UncheckedIOException("Failed to load QuickShop-Hikari configuration", exception);
        }
    }

    private QSConfigurations() {
    }

    public static void reload() {
        try {
            CONFIG_NODE.set(LOADER.load());
        } catch (final ConfigurateException exception) {
            throw new UncheckedIOException("Failed to load QuickShop-Hikari configuration", exception);
        }
    }

    public static boolean supportsMultiCurrency() {
        return QuickShops.economyProvider().multiCurrency();
    }

    public static boolean supportsBulkTransaction() {
        return CONFIG_NODE.get().node("shop", "allow-stacks").getBoolean(false);
    }

    public static int shopNameMaxLength() {
        return CONFIG_NODE.get().node("shop", "name-max-length").getInt(32);
    }

    public static BigDecimal shopNamingCost() {
        try {
            return CONFIG_NODE.get().node("shop", "name-fee").get(BigDecimal.class, BigDecimal.valueOf(0.0));
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static BigDecimal shopCreateCost() {
        try {
            return CONFIG_NODE.get().node("shop", "cost").get(BigDecimal.class, BigDecimal.valueOf(10.0));
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static BigDecimal shopPriceChangeCost() {
        final boolean requiresFee = CONFIG_NODE.get().node("shop", "price-change-requires-fee").getBoolean(true);
        if (!requiresFee) {
            return BigDecimal.ZERO;
        }

        try {
            return CONFIG_NODE.get().node("shop", "fee-for-price-change").get(BigDecimal.class, BigDecimal.valueOf(50.0));
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static @Nullable String getCurrency() {
        return CONFIG_NODE.get().node("currency").getString();
    }

    public static boolean requiresUnlimitedOwnerPayment() {
        return CONFIG_NODE.get().node("shop", "pay-unlimited-shop-owners").getBoolean(false);
    }

    public static List<Material> shopBlocks() {
        try {
            return CONFIG_NODE.get().node("shop-blocks").getList(Material.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static List<String> whitelistWorlds() {
        try {
            return CONFIG_NODE.get().node("shop", "whitelist-world").getList(String.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static List<String> blacklistWorlds() {
        try {
            return CONFIG_NODE.get().node("shop", "blacklist-world").getList(String.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static List<Material> blacklistItems() {
        try {
            return CONFIG_NODE.get().node("blacklist").getList(Material.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static List<String> blacklistLore() {
        try {
            return CONFIG_NODE.get().node("shop", "blacklist-lores").getList(String.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static boolean isShopLimitEnabled() {
        return CONFIG_NODE.get().node("limits", "use").getBoolean(false);
    }

    public static int shopDefaultLimit() {
        return CONFIG_NODE.get().node("limits", "default").getInt(10);
    }

    public static boolean isShopLimitOldAlgorithm() {
        return CONFIG_NODE.get().node("limits", "old-algorithm").getBoolean(false);
    }

    public static Map<String, Integer> shopPermissionsLimit() {
        try {
            return CONFIG_NODE.get().node("limits", "ranks").get(
                    new TypeToken<>() { },
                    Map.of("quickshop.example", 10)
            );
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
