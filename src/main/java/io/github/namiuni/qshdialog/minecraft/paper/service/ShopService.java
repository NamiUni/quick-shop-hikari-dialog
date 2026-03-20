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
package io.github.namiuni.qshdialog.minecraft.paper.service;

import com.ghostchu.quickshop.api.shop.Shop;
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.common.utilities.NumberRange;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSConfigurations;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopNotFoundException;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopRepository;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.SignUpdater;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ShopService {

    public ShopService() {
    }

    // -------------------------------------------------------------------------
    // Query
    // -------------------------------------------------------------------------

    public Optional<ShopBlock> findShop(final long shopId) {
        return ShopRepository.find(shopId);
    }

    public Optional<ShopBlock> findShop(final Location location) {
        return ShopRepository.find(location);
    }

    // -------------------------------------------------------------------------
    // Mutation
    // -------------------------------------------------------------------------

    // TODO: 同一コンテナへの重複ショップ作成を防ぐエラーが必要か確認する
    public Result<ShopSuccess, Set<ShopFailure>> createShop(final UserSession user, final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        final Set<ShopFailure> failures = new HashSet<>();

        validatePriceRange(user, shopComponent, failures);

        final String world = shopComponent.location().getWorld().getName();
        // 作成時は名前の有無にかかわらず命名コストが発生する可能性があるため、常に計上する
        final BigDecimal totalCost = QSConfigurations.shopCreateCost()
                .add(namingCost(user));
        validateBalance(user, world, totalCost, failures);

        if (!failures.isEmpty()) {
            return Result.error(failures);
        }

        user.withdrawMoney(totalCost, world);
        ShopRepository.create(shop);
        return Result.success(new ShopSuccess(totalCost));
    }

    public Result<ShopSuccess, Set<ShopFailure>> updateShop(final UserSession user, final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        final Set<ShopFailure> failures = new HashSet<>();

        validatePriceRange(user, shopComponent, failures);

        final String world = shopComponent.location().getWorld().getName();

        final Optional<ShopBlock> existing = ShopRepository.find(shopComponent.id());
        final BigDecimal priceChangeCost = existing
                .filter(e -> shopComponent.price().compareTo(e.component().price()) == 0)
                .map(e -> BigDecimal.ZERO).orElse(QSConfigurations.shopPriceChangeCost());

        final BigDecimal namingCost = existing
                .filter(e -> Objects.equals(shopComponent.name(), e.component().name()))
                .map(e -> BigDecimal.ZERO).orElse(namingCost(user));

        final BigDecimal totalCost = priceChangeCost.add(namingCost);
        validateBalance(user, world, totalCost, failures);

        if (!failures.isEmpty()) {
            return Result.error(failures);
        }

        // update を先に試みて、成功した場合のみ課金する（失敗時の課金を防ぐ）
        try {
            ShopRepository.update(shop);
            if (totalCost.compareTo(BigDecimal.ZERO) != 0) {
                user.withdrawMoney(totalCost, world);
            }
        } catch (final ShopNotFoundException exception) {
            failures.add(new ShopFailure.ShopNotFound());
            return Result.error(failures);
        }

        SignUpdater.update(shop, user.locale());
        return Result.success(new ShopSuccess(totalCost));
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private static void validatePriceRange(
            final UserSession user,
            final ShopComponent shopComponent,
            final Set<ShopFailure> failures
    ) {
        final NumberRange<BigDecimal> validRange = PriceAnalytics.getPriceLimit(user, shopComponent.product());
        final BigDecimal price = shopComponent.price();
        if (price.compareTo(validRange.min()) < 0 || price.compareTo(validRange.max()) > 0) {
            failures.add(new ShopFailure.PriceOutOfRange(validRange));
        }
    }

    private static void validateBalance(
            final UserSession user,
            final String world,
            final BigDecimal totalCost,
            final Set<ShopFailure> failures
    ) {
        if (totalCost.compareTo(BigDecimal.ZERO) <= 0) {
            return; // 無料なら残高チェック不要
        }
        final BigDecimal balance = user.balance(world, QSConfigurations.getCurrency());
        if (balance.compareTo(totalCost) < 0) {
            failures.add(new ShopFailure.OperatorInsufficientFunds(totalCost));
        }
    }

    private static BigDecimal namingCost(final UserSession user) {
        if (user.hasPermission(QSPermissions.SHOP_NAMING_BYPASS_COST)) {
            return BigDecimal.ZERO;
        }
        return QSConfigurations.shopNamingCost();
    }
}
