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

import com.ghostchu.quickshop.api.inventory.InventoryWrapper;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopAction;
import com.ghostchu.quickshop.shop.SimpleInfo;
import com.ghostchu.quickshop.shop.inventory.BukkitInventoryWrapper;
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopNotFoundException;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopRepository;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.SignUpdater;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TradeService {

    private TradeService() {
    }

    @SuppressWarnings("DataFlowIssue")
    public static Result<Void, ShopFailure.ShopNotFound> purchase(
            final UserSession customer,
            final ShopBlock shop,
            final int quantity
    ) {
        final Shop qsShop;
        try {
            qsShop = ShopRepository.findNative(shop);
        } catch (final ShopNotFoundException e) {
            return Result.error(new ShopFailure.ShopNotFound());
        }

        final Player bukkitCustomer = customer.bukkit().orElseThrow();
        final InventoryWrapper inventory = new BukkitInventoryWrapper(bukkitCustomer.getInventory());

        QuickShops.shopManager().actionSelling(
                bukkitCustomer,
                inventory,
                QuickShops.economyProvider(),
                new SimpleInfo(shop.component().location(), ShopAction.PURCHASE_BUY, null, null, qsShop, false),
                qsShop,
                quantity
        );
        SignUpdater.update(shop, customer.locale());

        return Result.success(null);
    }

    @SuppressWarnings("DataFlowIssue")
    public static Result<Void, ShopFailure.ShopNotFound> sell(
            final UserSession customer,
            final ShopBlock shop,
            final int quantity
    ) {
        final Shop qsShop;
        try {
            qsShop = ShopRepository.findNative(shop);
        } catch (final ShopNotFoundException e) {
            return Result.error(new ShopFailure.ShopNotFound());
        }

        final Player bukkitCustomer = customer.bukkit().orElseThrow();
        final InventoryWrapper inventory = new BukkitInventoryWrapper(bukkitCustomer.getInventory());

        QuickShops.shopManager().actionBuying(
                bukkitCustomer,
                inventory,
                QuickShops.economyProvider(),
                new SimpleInfo(shop.component().location(), ShopAction.PURCHASE_SELL, null, null, qsShop, false),
                qsShop,
                quantity
        );
        SignUpdater.update(shop, customer.locale());

        return Result.success(null);
    }
}
