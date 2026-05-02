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

import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.utilities.SignUpdater;
import io.github.namiuni.qshdialog.minecraft.paper.utilities.NumberRange;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class ShopService {

    private final ShopRepository shopRepository;
    private final SignUpdater signUpdater;
    private final PriceAnalytics priceAnalytics;
    private final EconomyService economyService;
    private final ShopCostCalculator costCalculator;
    private final QSConfiguration qsConfig;

    @Inject
    ShopService(
            final ShopRepository shopRepository,
            final SignUpdater signUpdater,
            final PriceAnalytics priceAnalytics,
            final EconomyService economyService,
            final ShopCostCalculator costCalculator,
            final QSConfiguration qsConfig
    ) {
        this.shopRepository = shopRepository;
        this.signUpdater = signUpdater;
        this.priceAnalytics = priceAnalytics;
        this.economyService = economyService;
        this.costCalculator = costCalculator;
        this.qsConfig = qsConfig;
    }

    public Optional<ShopBlock> findShop(final Location location) {
        return this.shopRepository.find(location);
    }

    public Result<ShopSuccess, Set<ShopFailure>> createShop(final UserSession user, final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        final Set<ShopFailure> failures = new HashSet<>();

        final Optional<ShopBlock> existing = this.shopRepository.find(shop.container().getLocation());
        existing.ifPresent(shopBlock -> failures.add(new ShopFailure.AlreadyExists(shopBlock)));

        if (!this.withinPriceRange(user, shopComponent)) {
            failures.add(new ShopFailure.PriceOutOfRange());
        }

        final String world = shopComponent.location().getWorld().getName();
        final BigDecimal createCost = this.costCalculator.calculateCreateCost(user);
        final BigDecimal balance = this.economyService.getBalance(user, world, shop.component().currency());
        if (balance.compareTo(createCost) < 0) {
            failures.add(new ShopFailure.OperatorInsufficientFunds(createCost));
        }

        if (failures.isEmpty()) {
            this.shopRepository.create(shop);
            return Result.success(new ShopSuccess(createCost));
        } else {
            return Result.error(failures);
        }
    }

    public Result<ShopSuccess, Set<ShopFailure>> updateShop(final UserSession user, final ShopBlock newShop) {
        final ShopComponent shopComponent = newShop.component();
        final Set<ShopFailure> failures = new HashSet<>();

        if (!this.withinPriceRange(user, shopComponent)) {
            failures.add(new ShopFailure.PriceOutOfRange());
        }

        final Optional<ShopBlock> oldShop = this.shopRepository.find(newShop.component().location());
        if (oldShop.isEmpty()) {
            failures.add(new ShopFailure.ShopNotFound());
        }

        final String world = shopComponent.location().getWorld().getName();
        final BigDecimal pricingCost = newShop.component().price().compareTo(oldShop.get().component().price()) == 0
                ? BigDecimal.ZERO
                : this.qsConfig.shopModifyPriceCost();
        final BigDecimal namingCost = Objects.equals(newShop.component().name(), oldShop.get().component().name())
                ? BigDecimal.ZERO
                : this.qsConfig.shopModifyNameCost();
        final BigDecimal totalCost = pricingCost.add(namingCost);
        final BigDecimal balance = this.economyService.getBalance(user, world, this.qsConfig.defaultCurrency());
        if (totalCost.compareTo(balance) > 0) {
            failures.add(new ShopFailure.OperatorInsufficientFunds(totalCost));
        }

        if (!failures.isEmpty()) {
            try {
                this.shopRepository.update(newShop);
                if (totalCost.compareTo(BigDecimal.ZERO) != 0) {
                    this.economyService.withdrawMoney(user, totalCost, world);
                }
            } catch (final ShopNotFoundException exception) {
                failures.add(new ShopFailure.ShopNotFound());
            }
        }

        this.signUpdater.update(newShop, user.locale());

        if (!failures.isEmpty()) {
            return Result.success(new ShopSuccess(totalCost));
        } else {
            return Result.error(failures);
        }
    }

    private boolean withinPriceRange(
            final UserSession user,
            final ShopComponent shopComponent
    ) {
        final NumberRange<BigDecimal> validRange = this.priceAnalytics.priceRange(user, shopComponent.product());
        final BigDecimal price = shopComponent.price();
        return 0 >= price.compareTo(validRange.min()) && price.compareTo(validRange.max()) <= 0;
    }
}
