package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter;

import com.ghostchu.quickshop.api.inventory.InventoryWrapperManager;
import com.ghostchu.quickshop.api.shop.IShopType;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.economy.QSBenefitProvider;
import com.ghostchu.quickshop.shop.ContainerShop;
import com.ghostchu.quickshop.shop.inventory.BukkitInventoryWrapper;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ShopRepository {

    private static final long NEW_SHOP_ID = -1L;

    private ShopRepository() {
    }

    public static Optional<ShopBlock> find(final long id) {
        final Shop qsShop = QuickShops.shopManager().getShop(id);
        if (qsShop == null) {
            return Optional.empty();
        }
        return Optional.of(ShopConverter.toShopEntity(qsShop));
    }

    public static Optional<ShopBlock> find(final org.bukkit.Location location) {
        final Shop qsShop = QuickShops.shopManager().getShopIncludeAttached(location);
        if (qsShop == null) {
            return Optional.empty();
        }
        return Optional.of(ShopConverter.toShopEntity(qsShop));
    }

    public static Shop findNative(final ShopBlock shop) throws ShopNotFoundException {
        final Shop qsShop = QuickShops.shopManager().getShop(shop.component().id());
        if (qsShop == null) {
            throw new ShopNotFoundException("The shop does not exist!");
        }
        return qsShop;
    }

    public static void create(final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        if (shopComponent.id() != NEW_SHOP_ID) {
            throw new ShopIDInvalidException("The shop ID must be -1!");
        }

        final IShopType shopType = ShopConverter.toQSShopType(shopComponent);
        final InventoryWrapperManager manager = QuickShops.inventoryWrapperManager();
        final String symbolLink = manager.mklink(new BukkitInventoryWrapper(shop.container().getInventory()));
        final Map<UUID, String> permissionGroups = ShopConverter.toQSPermissionGroups(shopComponent);

        final Shop qsShop = new ContainerShop(
                QuickShops.quickShop(),
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
                QuickShops.quickShop().getJavaPlugin().getName(),
                symbolLink,
                shopComponent.name(),
                permissionGroups,
                new QSBenefitProvider()
        );

        QuickShops.shopManager().createShop(qsShop, shop.frontBlock(), false);
    }

    public static void update(final ShopBlock shop) throws ShopNotFoundException {
        final ShopComponent shopComponent = shop.component();
        final Shop qsShop = QuickShops.shopManager().getShop(shopComponent.id());
        if (qsShop == null) {
            throw new ShopNotFoundException("The shop does not exist!");
        }

        if (shopComponent.product().getAmount() != qsShop.getItem().getAmount()) {
            qsShop.setItem(shopComponent.product());
        }

        if (!shopComponent.owner().qsUser().equals(qsShop.getOwner())) {
            qsShop.setOwner(shopComponent.owner().qsUser());
        }

        if (shopComponent.infiniteStock() != qsShop.isUnlimited()) {
            qsShop.setUnlimited(shopComponent.infiniteStock());
        }

        final IShopType shopType = ShopConverter.toQSShopType(shopComponent);
        if (!Objects.equals(shopType, qsShop.shopType())) {
            qsShop.shopType(shopType);
        }

        if (!Objects.equals(shopComponent.currency(), qsShop.getCurrency())) {
            qsShop.setCurrency(shopComponent.currency());
        }

        if (shopComponent.displayVisible() == qsShop.isDisableDisplay()) {
            qsShop.setDisableDisplay(!shopComponent.displayVisible());
        }

        if (!Objects.equals(shopComponent.name(), qsShop.getShopName())) {
            qsShop.setShopName(shopComponent.name());
        }

        shopComponent.accessLevels().forEach(
                (uuid, level) -> qsShop.setPlayerGroup(uuid, ShopConverter.toQSPermissionGroup(level)));

        qsShop.update();
    }
}
