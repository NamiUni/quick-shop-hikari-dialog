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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// Note: ShopComponent は純粋なデータ表現であり、QuickShop-Hikari の API に依存しない。
//       QS との変換は ShopConverter が担う。
//       ShopManager.createShop() で登録済みの Shop インスタンスから setter を呼び出すと
//       update() なしで変更が即反映されるという QS の仕様上の混乱を避けるため、
//       ShopComponent は不変レコードとして設計している。
@NullMarked
public record ShopComponent(
        // Base
        long id,
        Location location,
        @Nullable String name,
        UserSession owner,
        TradeType tradeType,
        @Nullable String currency,

        // Product
        ItemStack product,
        BigDecimal price,

        // Status
        boolean available,
        boolean displayVisible,
        boolean infiniteStock,

        // Staff
        Map<UUID, AccessLevel> accessLevels
) {

    public static final long NEW_ID = -1L;

    public static Builder builder() {
        return new Builder();
    }

    public boolean isStaff(final UUID uuid) {
        final AccessLevel accessLevel = this.accessLevels().getOrDefault(uuid, AccessLevel.EVERYONE);
        return accessLevel == AccessLevel.STAFF || accessLevel == AccessLevel.ADMINISTRATOR;
    }

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .location(this.location)
                .owner(this.owner)
                .product(this.product)
                .name(this.name)
                .tradeType(this.tradeType)
                .currency(this.currency)
                .price(this.price)
                .available(this.available)
                .displayVisible(this.displayVisible)
                .infiniteStock(this.infiniteStock)
                .accessLevels(this.accessLevels);
    }

    // =========================================================================

    public static final class Builder {

        private @Nullable Long id = null;
        private @Nullable Location location = null;
        private @Nullable String name = null;
        private @Nullable UserSession owner = null;
        private TradeType tradeType = TradeType.SELLING;
        private @Nullable String currency = null;
        private @Nullable ItemStack product = null;
        private @Nullable BigDecimal price = null;
        private boolean available = true;
        private boolean displayVisible = true;
        private boolean infiniteStock = false;
        private Map<UUID, AccessLevel> accessLevels = Map.of();

        private Builder() {
        }

        public Builder id(final long id) {
            this.id = id;
            return this;
        }

        public Builder location(final Location location) {
            this.location = location;
            return this;
        }

        public Builder name(final @Nullable String name) {
            this.name = name;
            return this;
        }

        public Builder owner(final UserSession owner) {
            this.owner = owner;
            return this;
        }

        public Builder tradeType(final TradeType tradeType) {
            this.tradeType = Objects.requireNonNull(tradeType);
            return this;
        }

        public Builder currency(final @Nullable String currency) {
            this.currency = currency;
            return this;
        }

        public Builder product(final ItemStack product) {
            this.product = product.clone();
            return this;
        }

        public Builder price(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder available(final boolean available) {
            this.available = available;
            return this;
        }

        public Builder displayVisible(final boolean displayVisible) {
            this.displayVisible = displayVisible;
            return this;
        }

        public Builder infiniteStock(final boolean infiniteStock) {
            this.infiniteStock = infiniteStock;
            return this;
        }

        public Builder accessLevels(final Map<UUID, AccessLevel> accessLevels) {
            this.accessLevels = Map.copyOf(accessLevels);
            return this;
        }

        public ShopComponent build() {
            return new ShopComponent(
                    Objects.requireNonNull(this.id, "id"),
                    Objects.requireNonNull(this.location, "location"),
                    this.name,
                    Objects.requireNonNull(this.owner, "owner"),
                    Objects.requireNonNull(this.tradeType, "tradeType"),
                    this.currency,
                    Objects.requireNonNull(this.product, "product"),
                    Objects.requireNonNull(this.price, "price"),
                    this.available,
                    this.displayVisible,
                    this.infiniteStock,
                    Objects.requireNonNull(this.accessLevels, "accessLevels")
            );
        }
    }
}
