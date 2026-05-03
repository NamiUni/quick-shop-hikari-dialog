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
