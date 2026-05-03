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
