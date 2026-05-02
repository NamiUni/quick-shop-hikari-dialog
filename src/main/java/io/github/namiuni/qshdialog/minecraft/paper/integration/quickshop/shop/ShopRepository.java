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

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.inventory.InventoryWrapperManager;
import com.ghostchu.quickshop.api.shop.IShopType;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopManager;
import com.ghostchu.quickshop.economy.QSBenefitProvider;
import com.ghostchu.quickshop.shop.ContainerShop;
import com.ghostchu.quickshop.shop.inventory.BukkitInventoryWrapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class ShopRepository {

    private static final long NEW_SHOP_ID = -1L;

    private final ShopManager shopManager;
    private final InventoryWrapperManager inventoryWrapperManager;
    private final QuickShop quickShop;

    @Inject
    ShopRepository(
            final ShopManager shopManager,
            final InventoryWrapperManager inventoryWrapperManager,
            final QuickShop quickShop
    ) {
        this.shopManager = shopManager;
        this.inventoryWrapperManager = inventoryWrapperManager;
        this.quickShop = quickShop;
    }

    public Optional<ShopBlock> find(final long id) {
        final Shop qsShop = this.shopManager.getShop(id);
        if (qsShop == null) {
            return Optional.empty();
        }
        return Optional.of(ShopConverter.toShopBlock(qsShop));
    }

    public Optional<ShopBlock> find(final org.bukkit.Location location) {
        final Shop qsShop = this.shopManager.getShopIncludeAttached(location);
        if (qsShop == null) {
            return Optional.empty();
        }
        return Optional.of(ShopConverter.toShopBlock(qsShop));
    }

    public Shop findNative(final ShopBlock shop) throws ShopNotFoundException {
        final Shop qsShop = this.shopManager.getShop(shop.component().id());
        if (qsShop == null) {
            throw new ShopNotFoundException("The shop does not exist!");
        }
        return qsShop;
    }

    public void create(final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        if (shopComponent.id() != NEW_SHOP_ID) {
            throw new ShopIDInvalidException("The shop ID must be -1!");
        }

        final IShopType shopType = ShopConverter.toQSShopType(shopComponent);
        final String symbolLink = this.inventoryWrapperManager.mklink(new BukkitInventoryWrapper(shop.container().getInventory()));
        final Map<UUID, String> permissionGroups = ShopConverter.toQSPermissionGroups(shopComponent.accessLevels());

        final Shop qsShop = new ContainerShop(
                this.quickShop,
                NEW_SHOP_ID,
                shopComponent.location(),
                shopComponent.price().doubleValue(),
                shopComponent.product(),
                shopComponent.owner().qsUser(),
                shopComponent.infiniteStock(),
                shopType,
                new YamlConfiguration(),
                shopComponent.currency(),
                !shopComponent.displayVisible(),
                null,
                this.quickShop.getJavaPlugin().getName(),
                symbolLink,
                shopComponent.name(),
                permissionGroups,
                new QSBenefitProvider()
        );

        this.shopManager.createShop(qsShop, shop.frontBlock(), false);
    }

    public void update(final ShopBlock shop) throws ShopNotFoundException {
        final ShopComponent shopComponent = shop.component();
        final Shop qsShop = this.shopManager.getShop(shopComponent.id());
        if (qsShop == null) {
            throw new ShopNotFoundException("The shop does not exist!");
        }

        if (!Objects.equals(shopComponent.name(), qsShop.getShopName())) {
            qsShop.setShopName(shopComponent.name());
        }

        if (!shopComponent.owner().qsUser().equals(qsShop.getOwner())) {
            qsShop.setOwner(shopComponent.owner().qsUser());
        }

        final IShopType shopType = ShopConverter.toQSShopType(shopComponent);
        if (!Objects.equals(shopType, qsShop.shopType())) {
            qsShop.shopType(shopType);
        }

        if (!Objects.equals(shopComponent.currency(), qsShop.getCurrency())) {
            qsShop.setCurrency(shopComponent.currency());
        }

        if (shopComponent.product().getAmount() != qsShop.getItem().getAmount()) {
            qsShop.setItem(shopComponent.product());
        }

        if (shopComponent.price().compareTo(BigDecimal.valueOf(qsShop.getPrice())) != 0) {
            qsShop.setPrice(shopComponent.price().doubleValue());
        }

        if (shopComponent.displayVisible() == qsShop.isDisableDisplay()) {
            qsShop.setDisableDisplay(!shopComponent.displayVisible());
        }

        if (shopComponent.infiniteStock() != qsShop.isUnlimited()) {
            qsShop.setUnlimited(shopComponent.infiniteStock());
        }

        shopComponent.accessLevels().forEach((uuid, level) -> qsShop.setPlayerGroup(uuid, ShopConverter.toQSPermissionGroup(level)));

        qsShop.update();
    }
}
