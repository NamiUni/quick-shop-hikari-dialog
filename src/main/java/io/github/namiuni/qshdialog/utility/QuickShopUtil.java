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
package io.github.namiuni.qshdialog.utility;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.economy.EconomyManager;
import com.ghostchu.quickshop.api.economy.EconomyProvider;
import com.ghostchu.quickshop.api.inventory.InventoryWrapper;
import com.ghostchu.quickshop.api.localization.text.Text;
import com.ghostchu.quickshop.api.obj.QUser;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopAction;
import com.ghostchu.quickshop.economy.transaction.QSEconomyTransaction;
import com.ghostchu.quickshop.shop.SimpleInfo;
import com.ghostchu.quickshop.shop.inventory.BukkitInventoryWrapper;
import io.github.namiuni.qshdialog.user.QSHUser;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class QuickShopUtil {

    private QuickShopUtil() {
    }

    public static double namingCost(final QSHUser user) {
        return user.hasPermission("quickshop.bypass.namefee")
                ? 0.0
                : QuickShop.getInstance().getConfig().getDouble("shop.name-fee", 0.0);
    }

    public static int maxNameLength() {
        return QuickShop.getInstance().getConfig().getInt("shop.name-max-length", 32);
    }

    public static void withdrawNamingCost(final QSHUser user, final World world, final @Nullable QUser taxAccount) {
        final QuickShop quickShop = QuickShop.getInstance();
        final double fee = quickShop.getConfig().getDouble("shop.name-fee", 0);

        if (fee > 0 && !user.hasPermission("quickshop.bypass.namefee")) {
            final QSEconomyTransaction transaction = QSEconomyTransaction.builder()
                    .world(world.getName())
                    .from(user.quickShopUser())
                    .to(taxAccount)
                    .currency(quickShop.getCurrency())
                    .taxer(taxAccount)
                    .tax(BigDecimal.ZERO)
                    .amount(BigDecimal.valueOf(fee))
                    .build();
            if (!transaction.completable()) {
                final Text message = quickShop.text().of(
                        user.quickShopUser(),
                        "you-cant-afford-shop-naming",
                        quickShop.getShopManager().format(fee, world, quickShop.getCurrency()));
                message.send();
            }
        }
    }

    public static void sellItem(final QSHUser seller, final Shop shop, final int quantity) {
        final Player bukkitSeller = seller.quickShopUser().getBukkitPlayer().orElseThrow();
        final InventoryWrapper inventory = new BukkitInventoryWrapper(bukkitSeller.getInventory());

        QuickShop.getInstance().getShopManager().actionBuying(
                bukkitSeller,
                inventory,
                Objects.requireNonNull(QuickShop.getInstance().getEconomyManager().provider()),
                new SimpleInfo(shop.getLocation(), ShopAction.PURCHASE_SELL, null, null, shop, false),
                shop,
                quantity
        );
    }

    public static void buyItem(final QSHUser buyer, final Shop shop, final int quantity) {
        final Player bukkitBuyer = buyer.quickShopUser().getBukkitPlayer().orElseThrow();
        final InventoryWrapper inventory = new BukkitInventoryWrapper(bukkitBuyer.getInventory());

        QuickShop.getInstance().getShopManager().actionSelling(
                bukkitBuyer,
                inventory,
                Objects.requireNonNull(QuickShop.getInstance().getEconomyManager().provider()),
                new SimpleInfo(shop.getLocation(), ShopAction.PURCHASE_BUY, null, null, shop, false),
                shop,
                quantity
        );
    }

    public static boolean supportsMultiCurrency() {
        final EconomyManager economyManager = QuickShop.getInstance().getEconomyManager();

        return Optional.ofNullable(economyManager.provider())
                .map(EconomyProvider::multiCurrency)
                .orElse(false);
    }
}
