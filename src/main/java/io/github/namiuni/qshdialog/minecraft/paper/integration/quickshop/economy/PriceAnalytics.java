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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy;

import com.ghostchu.quickshop.api.shop.PriceLimiter;
import com.ghostchu.quickshop.api.shop.PriceLimiterCheckResult;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.utilities.NumberRange;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class PriceAnalytics {

    private final PriceLimiter priceLimiter;

    @Inject
    PriceAnalytics(final PriceLimiter priceLimiter) {
        this.priceLimiter = priceLimiter;
    }

    public NumberRange<BigDecimal> priceRange(final UserSession user, final ItemStack product) {
        final PriceLimiterCheckResult result = this.priceLimiter.check(user.qsUser(), product, null, 1.0);
        return new NumberRange<>(
                BigDecimal.valueOf(result.getMin()),
                BigDecimal.valueOf(result.getMax())
        );
    }
}
