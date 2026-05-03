package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop;

import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class ShopCostCalculator {

    private final QSConfiguration qsConfig;

    @Inject
    ShopCostCalculator(final QSConfiguration qsConfig) {
        this.qsConfig = qsConfig;
    }

    public BigDecimal calculateCreateCost(final UserSession user) {
        if (user.hasPermission(QSPermissions.SHOP_BYPASS_COST_CREATE)) {
            return BigDecimal.ZERO;
        }
        return this.qsConfig.shopCreateCost();
    }

    public BigDecimal calculateNamingCost(final UserSession user) {
        if (user.hasPermission(QSPermissions.SHOP_BYPASS_COST_NAMING)) {
            return BigDecimal.ZERO;
        }
        return this.qsConfig.shopModifyNameCost();
    }

    public BigDecimal calculatePricingCost(final UserSession user) {
        return this.qsConfig.shopModifyPriceCost();
    }
}
