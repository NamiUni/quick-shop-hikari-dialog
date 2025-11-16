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
import com.ghostchu.quickshop.api.economy.EconomyProvider;
import com.ghostchu.quickshop.api.inventory.InventoryWrapper;
import com.ghostchu.quickshop.api.localization.text.Text;
import com.ghostchu.quickshop.api.localization.text.TextManager;
import com.ghostchu.quickshop.api.obj.QUser;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.ShopAction;
import com.ghostchu.quickshop.api.shop.ShopManager;
import com.ghostchu.quickshop.economy.transaction.QSEconomyTransaction;
import com.ghostchu.quickshop.shop.SimpleInfo;
import com.ghostchu.quickshop.shop.inventory.BukkitInventoryWrapper;
import com.ghostchu.quickshop.util.Util;
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.user.QSHUser;
import java.math.BigDecimal;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class QuickShopUtil {

    private static final QuickShop QUICK_SHOP = QuickShop.getInstance();
    private static final FileConfiguration CONFIG = QUICK_SHOP.getConfig();
    private static final TextManager TEXT_MANAGER = QUICK_SHOP.text();
    private static final ShopManager SHOP_MANAGER = QUICK_SHOP.getShopManager();
    private static final EconomyProvider ECONOMY_PROVIDER = Objects.requireNonNull(QUICK_SHOP.getEconomyManager().provider());

    private QuickShopUtil() {
    }

    public static double namingCost(final QSHUser user) {
        return user.hasPermission("quickshop.bypass.namefee")
                ? 0.0
                : CONFIG.getDouble("shop.name-fee", 0.0);
    }

    public static int maxNameLength() {
        return CONFIG.getInt("shop.name-max-length", 32);
    }

    public static void withdrawNamingCost(final QSHUser user, final World world, final @Nullable QUser taxAccount) {
        final double fee = CONFIG.getDouble("shop.name-fee", 0);

        if (fee > 0 && !user.hasPermission("quickshop.bypass.namefee")) {
            final QSEconomyTransaction transaction = QSEconomyTransaction.builder()
                    .world(world.getName())
                    .from(user.quickShopUser())
                    .to(taxAccount)
                    .currency(QUICK_SHOP.getCurrency())
                    .taxer(taxAccount)
                    .tax(BigDecimal.ZERO)
                    .amount(BigDecimal.valueOf(fee))
                    .build();
            if (!transaction.completable()) {
                final Text message = TEXT_MANAGER.of(
                        user.quickShopUser(),
                        "you-cant-afford-shop-naming",
                        SHOP_MANAGER.format(fee, world, QUICK_SHOP.getCurrency()));
                message.send();
            }
        }
    }

    public static void sellItem(final QSHUser seller, final Shop shop, final int quantity) {
        final Player bukkitSeller = seller.quickShopUser().getBukkitPlayer().orElseThrow();
        final InventoryWrapper inventory = new BukkitInventoryWrapper(bukkitSeller.getInventory());

        SHOP_MANAGER.actionBuying(
                bukkitSeller,
                inventory,
                ECONOMY_PROVIDER,
                new SimpleInfo(shop.getLocation(), ShopAction.PURCHASE_SELL, null, null, shop, false),
                shop,
                quantity
        );
    }

    public static void buyItem(final QSHUser buyer, final Shop shop, final int quantity) {
        final Player bukkitBuyer = buyer.quickShopUser().getBukkitPlayer().orElseThrow();
        final InventoryWrapper inventory = new BukkitInventoryWrapper(bukkitBuyer.getInventory());

        SHOP_MANAGER.actionSelling(
                bukkitBuyer,
                inventory,
                ECONOMY_PROVIDER,
                new SimpleInfo(shop.getLocation(), ShopAction.PURCHASE_BUY, null, null, shop, false),
                shop,
                quantity
        );
    }

    public static boolean supportsMultiCurrency() {
        return ECONOMY_PROVIDER.multiCurrency();
    }

    public static Result<Integer, Component> availableQuantityForPurchase(final QSHUser customer, final Shop shop) {
        final Player bukkitCustomer = customer.quickShopUser().getBukkitPlayer().orElseThrow();

        final int shopHaveItems = shop.getRemainingStock();
        final int customerHaveSpaces = Util.countSpace(new BukkitInventoryWrapper(bukkitCustomer.getInventory()), shop);

        if (customerHaveSpaces < 1) {
            final Component errorMessage = TEXT_MANAGER
                    .of(bukkitCustomer, "not-enough-space", customerHaveSpaces)
                    .forLocale();
            return Result.error(errorMessage);
        }

        final double price = shop.getPrice();
        final double balance = ECONOMY_PROVIDER.balance(customer.quickShopUser(), shop.getLocation().getWorld().getName(), shop.getCurrency()).doubleValue();
        final int quantity = shop.isUnlimited()
                ? Math.min(customerHaveSpaces, (int) Math.floor(balance / price))
                : Math.min(Math.min(shopHaveItems, customerHaveSpaces), (int) Math.floor(balance / price));

        if (0 < quantity) {
            return Result.success(quantity);
        }

        if (!shop.isUnlimited() && shopHaveItems < 1) {
            final Component errorMessage = TEXT_MANAGER.of(bukkitCustomer, "shop-stock-too-low",
                            shop.getRemainingStock(),
                            Util.getItemStackName(shop.getItem()))
                    .forLocale(customer.locale().toString());
            return Result.error(errorMessage);
        } else {
            final Component errorMessage = TEXT_MANAGER.of(bukkitCustomer, "you-cant-afford-to-buy",
                            SHOP_MANAGER.format(price, shop.getLocation().getWorld(), shop.getCurrency()),
                            SHOP_MANAGER.format(balance, shop.getLocation().getWorld(), shop.getCurrency()))
                    .forLocale(customer.locale().toString());
            return Result.error(errorMessage);
        }
    }

    public static Result<Integer, Component> availableQuantityForSale(final QSHUser customer, final Shop shop) {
        final Player bukkitCustomer = customer.quickShopUser().getBukkitPlayer().orElseThrow();

        if (!shop.isUnlimited() && shop.getRemainingSpace() == 0) {
            final Component message = TEXT_MANAGER.of(
                    bukkitCustomer,
                    "shop-has-no-space",
                    shop.getRemainingSpace(),
                    shop.getItem().effectiveName()
            ).forLocale(customer.locale().toString());
            return Result.error(message);
        }

        final boolean payUnlimitedOwners = CONFIG.getBoolean("shop.pay-unlimited-shop-owners");
        final boolean needsBalanceCheck = !shop.isUnlimited() || payUnlimitedOwners;
        final double ownerBalance = ECONOMY_PROVIDER
                .balance(shop.getOwner(), shop.getLocation().getWorld().getName(), shop.getCurrency())
                .doubleValue();
        final int ownerCanAfford = (0 < shop.getPrice())
                ? (int) (ownerBalance / shop.getPrice())
                : Integer.MAX_VALUE;

        if (needsBalanceCheck && ownerCanAfford == 0) {
            return Result.error(TEXT_MANAGER.of(
                    bukkitCustomer,
                    "the-owner-cant-afford-to-buy-from-you",
                    SHOP_MANAGER.format(shop.getPrice(), shop.getLocation().getWorld(), shop.getCurrency()),
                    SHOP_MANAGER.format(ownerBalance, shop.getLocation().getWorld(), shop.getCurrency())
            ).forLocale(customer.locale().toString()));
        }

        final BukkitInventoryWrapper wrappedInventory = new BukkitInventoryWrapper(bukkitCustomer.getInventory());
        final int customerAvailableQuantity = Util.countItems(wrappedInventory, shop);

        final int quantity = calculateAvailableQuantityForSale(shop, customerAvailableQuantity, ownerCanAfford);
        if (0 < quantity) {
            return Result.success(quantity);
        }

        final Component message = TEXT_MANAGER.of(
                bukkitCustomer,
                "you-dont-have-that-many-items",
                0,
                shop.getItem().effectiveName()
        ).forLocale(customer.locale().toString());
        return Result.error(message);
    }

    private static int calculateAvailableQuantityForSale(
            final Shop shop,
            final int customerAvailableQuantity,
            final int ownerCanAfford
    ) {
        final boolean payUnlimitedOwners = CONFIG.getBoolean("shop.pay-unlimited-shop-owners");
        if (shop.isUnlimited() && payUnlimitedOwners) {
            return Math.min(customerAvailableQuantity, ownerCanAfford);
        }

        if (shop.isUnlimited()) {
            return customerAvailableQuantity;
        }

        return Math.min(customerAvailableQuantity, Math.min(shop.getRemainingSpace(), ownerCanAfford));
    }
}
