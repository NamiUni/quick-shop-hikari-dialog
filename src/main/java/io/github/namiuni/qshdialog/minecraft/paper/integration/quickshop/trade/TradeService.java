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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade;

import com.ghostchu.quickshop.api.economy.EconomyProvider;
import com.ghostchu.quickshop.api.inventory.InventoryWrapper;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopAction;
import com.ghostchu.quickshop.api.shop.ShopManager;
import com.ghostchu.quickshop.shop.SimpleInfo;
import com.ghostchu.quickshop.shop.inventory.BukkitInventoryWrapper;
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopNotFoundException;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopRepository;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class TradeService {

    private final ShopRepository shopRepository;
    private final ShopManager shopManager;
    private final EconomyProvider economyProvider;

    @Inject
    TradeService(
            final ShopRepository shopRepository,
            final ShopManager shopManager,
            final EconomyProvider economyProvider
    ) {
        this.shopRepository = shopRepository;
        this.shopManager = shopManager;
        this.economyProvider = economyProvider;
    }

    @SuppressWarnings("DataFlowIssue")
    public Result<Void, TradeFailure> purchase(
            final UserSession customer,
            final ShopBlock shop,
            final int quantity
    ) {
        final Shop qsShop;
        try {
            qsShop = this.shopRepository.findNative(shop);
        } catch (final ShopNotFoundException exception) {
            return Result.error(new TradeFailure.ShopNotFound());
        }

        final Player bukkitCustomer = customer.bukkit().orElseThrow();
        final InventoryWrapper inventory = new BukkitInventoryWrapper(bukkitCustomer.getInventory());

        final boolean success = this.shopManager.actionSelling(
                bukkitCustomer,
                inventory,
                this.economyProvider,
                new SimpleInfo(shop.component().location(), ShopAction.PURCHASE_BUY, null, null, qsShop, false),
                qsShop,
                quantity
        );

        if (success) {
            return Result.success(null);
        } else {
            return Result.error(new TradeFailure.Unknown());
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public Result<Void, TradeFailure> sell(
            final UserSession customer,
            final ShopBlock shop,
            final int quantity
    ) {
        final Shop qsShop;
        try {
            qsShop = this.shopRepository.findNative(shop);
        } catch (final ShopNotFoundException exception) {
            return Result.error(new TradeFailure.ShopNotFound());
        }

        final Player bukkitCustomer = customer.bukkit().orElseThrow();
        final InventoryWrapper inventory = new BukkitInventoryWrapper(bukkitCustomer.getInventory());

        final boolean success = this.shopManager.actionBuying(
                bukkitCustomer,
                inventory,
                this.economyProvider,
                new SimpleInfo(shop.component().location(), ShopAction.PURCHASE_SELL, null, null, qsShop, false),
                qsShop,
                quantity
        );
        if (success) {
            return Result.success(null);
        } else {
            return Result.error(new TradeFailure.Unknown());
        }
    }
}
