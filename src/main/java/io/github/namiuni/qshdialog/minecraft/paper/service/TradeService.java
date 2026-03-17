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
