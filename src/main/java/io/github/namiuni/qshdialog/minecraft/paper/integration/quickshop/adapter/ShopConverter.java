package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter;

import com.ghostchu.quickshop.api.shop.IShopType;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.permission.BuiltInShopPermissionGroup;
import com.ghostchu.quickshop.shop.SimpleShopManager;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.AccessLevel;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.TradeType;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.jspecify.annotations.NullMarked;

/**
 * QS の型と自前モデルの間の変換を担う。
 * QS への依存はこのクラスに集約する。
 */
@NullMarked
public final class ShopConverter {

    private ShopConverter() {
    }

    // -------------------------------------------------------------------------
    // Shop
    // -------------------------------------------------------------------------

    public static ShopBlock toShopEntity(final Shop qsShop) {
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

    public static BuiltInShopPermissionGroup toQSPermissionGroup(final AccessLevel level) {
        return switch (level) {
            case ADMINISTRATOR -> BuiltInShopPermissionGroup.ADMINISTRATOR;
            case STAFF -> BuiltInShopPermissionGroup.STAFF;
            case EVERYONE -> BuiltInShopPermissionGroup.EVERYONE;
            case BLOCKED -> BuiltInShopPermissionGroup.BLOCKED;
        };
    }

    public static Map<UUID, String> toQSPermissionGroups(final ShopComponent shopComponent) {
        return shopComponent.accessLevels().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> toQSPermissionGroup(entry.getValue()).getNamespacedNode()
                ));
    }

    /**
     * 指定した {@link AccessLevel} が QS のパーミッション文字列を持っているかどうかを返す。
     *
     * <p>変換は {@link BuiltInShopPermissionGroup#hasPermission(String)} に委譲する。
     */
    public static boolean hasPermission(final AccessLevel level, final String qsPermission) {
        return toQSPermissionGroup(level).hasPermission(qsPermission);
    }

    public static AccessLevel toAccessLevel(final String namespacedNode) {
        if (namespacedNode.equals(BuiltInShopPermissionGroup.ADMINISTRATOR.getNamespacedNode())) {
            return AccessLevel.ADMINISTRATOR;
        }
        if (namespacedNode.equals(BuiltInShopPermissionGroup.STAFF.getNamespacedNode())) {
            return AccessLevel.STAFF;
        }
        if (namespacedNode.equals(BuiltInShopPermissionGroup.BLOCKED.getNamespacedNode())) {
            return AccessLevel.BLOCKED;
        }
        return AccessLevel.EVERYONE;
    }
}
