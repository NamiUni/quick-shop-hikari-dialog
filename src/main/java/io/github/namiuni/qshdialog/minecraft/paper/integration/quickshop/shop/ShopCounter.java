package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop;

import com.ghostchu.quickshop.api.RankLimiter;
import com.ghostchu.quickshop.api.shop.ShopManager;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class ShopCounter {

    private final ShopManager shopManager;
    private final RankLimiter rankLimiter;
    private final QSConfiguration qsConfig;

    @Inject
    ShopCounter(
            final ShopManager shopManager,
            final RankLimiter rankLimiter,
            final QSConfiguration qsConfig
    ) {
        this.shopManager = shopManager;
        this.rankLimiter = rankLimiter;
        this.qsConfig = qsConfig;
    }

    public int currentShops(final UserSession user) {
        final UUID playerUuid = user.uuid();
        final boolean oldAlgorithm = this.qsConfig.isShopLimitOldAlgorithm();
        return (int) this.shopManager.getAllShops().stream()
                .filter(shop -> playerUuid.equals(shop.getOwner().getUniqueId()))
                .filter(shop -> oldAlgorithm || !shop.isUnlimited())
                .count();
    }

    public int maximumShops(final UserSession user) {
        return this.rankLimiter.getShopLimit(user.qsUser());
    }
}
