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
package io.github.namiuni.qshdialog.shop;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.inventory.InventoryWrapperManager;
import com.ghostchu.quickshop.api.obj.QUser;
import com.ghostchu.quickshop.economy.QSBenefitProvider;
import com.ghostchu.quickshop.shop.ContainerShop;
import com.ghostchu.quickshop.shop.SimpleShopManager;
import com.ghostchu.quickshop.shop.inventory.BukkitInventoryWrapper;
import com.ghostchu.quickshop.shop.inventory.BukkitInventoryWrapperManager;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ContainerShopBuilder {

    private final long id;
    private final Container container;
    private @MonotonicNonNull BigDecimal price;
    private @MonotonicNonNull ItemStack product;
    private int bundleSize = 1;
    private @MonotonicNonNull QUser owner;
    private boolean unlimited = false;
    private ShopMode type = ShopMode.SELLING;
    private ShopStatus status = ShopStatus.AVAILABLE;
    private @Nullable String currency = null;
    private ShopDisplay display = ShopDisplay.SHOW;
    private @Nullable String name = null;
    private Map<UUID, String> playerGroup = Map.of();

    ContainerShopBuilder(final Container container) {
        this.id = -1;
        this.container = container;
    }

    public ContainerShopBuilder price(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public ContainerShopBuilder product(final ItemStack product) {
        this.product = product;
        return this;
    }

    public ContainerShopBuilder bundleSize(final int bundleSize) {
        this.bundleSize = bundleSize;
        return this;
    }

    public ContainerShopBuilder owner(final QUser owner) {
        this.owner = owner;
        return this;
    }

    public ContainerShopBuilder unlimited(final boolean unlimited) {
        this.unlimited = unlimited;
        return this;
    }

    public ContainerShopBuilder mode(final ShopMode mode) {
        this.type = mode;
        return this;
    }

    public ContainerShopBuilder status(final ShopStatus status) {
        this.status = status;
        return this;
    }

    public ContainerShopBuilder currency(final @Nullable String currency) {
        this.currency = currency;
        return this;
    }

    public ContainerShopBuilder display(final ShopDisplay display) {
        this.display = display;
        return this;
    }

    public ContainerShopBuilder shopName(final @Nullable String shopName) {
        this.name = shopName;
        return this;
    }

    public ContainerShopBuilder playerGroup(final Map<UUID, String> playerGroup) {
        this.playerGroup = playerGroup;
        return this;
    }

    public ContainerShop build() {
        Objects.requireNonNull(this.product, "itemStack");
        Objects.requireNonNull(this.owner, "user");
        this.product.setAmount(this.bundleSize);

        final QuickShop quickShop = QuickShop.getInstance();

        final InventoryWrapperManager manager = quickShop.getInventoryWrapperManager();
        final String symbolLink = manager instanceof BukkitInventoryWrapperManager bukkitInventoryWrapperManager
                ? bukkitInventoryWrapperManager.mklink(this.container.getLocation())
                : manager.mklink(new BukkitInventoryWrapper(this.container.getInventory()));

        return new ContainerShop(
                quickShop,
                this.id,
                this.container.getLocation(),
                this.price.doubleValue(),
                this.product,
                this.owner,
                this.unlimited,
                this.status == ShopStatus.AVAILABLE
                        ? this.type == ShopMode.BUYING ? SimpleShopManager.BUYING_TYPE : SimpleShopManager.SELLING_TYPE
                        : SimpleShopManager.FROZEN_TYPE,
                new YamlConfiguration(),
                this.currency,
                this.display == ShopDisplay.HIDE,
                null,
                quickShop.getJavaPlugin().getName(),
                symbolLink,
                this.name,
                this.playerGroup,
                new QSBenefitProvider()
        );
    }
}
