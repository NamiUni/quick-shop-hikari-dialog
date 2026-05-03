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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration;

import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.serializer.BigDecimalSerializer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@Singleton
@NullMarked
public final class QSConfiguration {

    private final YamlConfigurationLoader loader;
    private final AtomicReference<ConfigurationNode> configNode;

    @Inject
    QSConfiguration(final @QuickShopDataDirectory Path dataDirectory) {
        this.loader = YamlConfigurationLoader.builder()
                .path(dataDirectory.resolve("config.yml"))
                .defaultOptions(options -> options
                        .serializers(builder -> builder
                                .register(Material.class, MaterialSerializer.INSTANCE)
                                .register(BigDecimal.class, BigDecimalSerializer.INSTANCE)
                        ))
                .build();

        try {
            this.configNode = new AtomicReference<>(this.loader.load());
        } catch (final ConfigurateException exception) {
            throw new UncheckedIOException("Failed to load QuickShop-Hikari configuration", exception);
        }
    }

    public void reload() {
        try {
            this.configNode.set(this.loader.load());
        } catch (final ConfigurateException exception) {
            throw new UncheckedIOException("Failed to load QuickShop-Hikari configuration", exception);
        }
    }

    public boolean supportsUnitTransaction() {
        return this.configNode.get().node("shop", "allow-stacks").getBoolean(false);
    }

    public int shopNameMaxLength() {
        return this.configNode.get().node("shop", "name-max-length").getInt(32);
    }

    public BigDecimal shopModifyNameCost() {
        try {
            return this.configNode.get().node("shop", "name-fee").get(BigDecimal.class, BigDecimal.valueOf(0.0));
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public BigDecimal shopCreateCost() {
        try {
            return this.configNode.get().node("shop", "cost").get(BigDecimal.class, BigDecimal.valueOf(10.0));
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public BigDecimal shopModifyPriceCost() {
        final boolean requiresFee = this.configNode.get().node("shop", "price-change-requires-fee").getBoolean(true);
        if (!requiresFee) {
            return BigDecimal.ZERO;
        }

        try {
            return this.configNode.get().node("shop", "fee-for-price-change").get(BigDecimal.class, BigDecimal.valueOf(50.0));
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public @Nullable String defaultCurrency() {
        return this.configNode.get().node("currency").getString();
    }

    public boolean requiresUnlimitedOwnerPayment() {
        return this.configNode.get().node("shop", "pay-unlimited-shop-owners").getBoolean(false);
    }

    public List<Material> shopBlocks() {
        try {
            return this.configNode.get().node("shop-blocks").getList(Material.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public List<String> whitelistWorlds() {
        try {
            return this.configNode.get().node("shop", "whitelist-world").getList(String.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public List<String> blacklistWorlds() {
        try {
            return this.configNode.get().node("shop", "blacklist-world").getList(String.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public List<Material> blacklistItems() {
        try {
            return this.configNode.get().node("blacklist").getList(Material.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public List<String> blacklistLore() {
        try {
            return this.configNode.get().node("shop", "blacklist-lores").getList(String.class, List.of());
        } catch (final SerializationException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public boolean isShopLimitEnabled() {
        return this.configNode.get().node("limits", "use").getBoolean(false);
    }

    public boolean isShopLimitOldAlgorithm() {
        return this.configNode.get().node("limits", "old-algorithm").getBoolean(false);
    }
}
