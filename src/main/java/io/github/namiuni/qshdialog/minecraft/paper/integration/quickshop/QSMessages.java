package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop;

import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.EconomyFormatter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopInventory;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QSMessages {

    private QSMessages() {
    }

    public static Component errorShopUnavailable(final UserSession customer) {
        return QuickShops.textManager()
                .of(customer.qsUser(), "shop-cannot-trade-when-freezing")
                .forLocale(customer.locale().toString());
    }

    public static Component errorShopOutOfStock(final UserSession customer, final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        return QuickShops.textManager()
                .of(customer.qsUser(), "shop-stock-too-low",
                        ShopInventory.stockCount(shop),
                        shopComponent.product().effectiveName())
                .forLocale(customer.locale().toString());
    }

    public static Component errorShopInventoryFull(final UserSession customer, final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        return QuickShops.textManager()
                .of(customer.qsUser(), "shop-has-no-space",
                        ShopInventory.spaceCount(shop),
                        shopComponent.product().effectiveName())
                .forLocale(customer.locale().toString());
    }

    public static Component errorShopInsufficientFunds(final UserSession customer, final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        final String world = shopComponent.location().getWorld().getName();
        final String formattedPrice = EconomyFormatter.format(shopComponent.price(), world, shopComponent.currency());
        final String formattedBalance = EconomyFormatter.format(
                shopComponent.owner().balance(world, shopComponent.currency()), world, shopComponent.currency());
        return QuickShops.textManager()
                .of(customer.qsUser(), "the-owner-cant-afford-to-buy-from-you", formattedPrice, formattedBalance)
                .forLocale(customer.locale().toString());
    }

    public static Component errorCustomerInventoryFull(final UserSession customer, final int actualSpace) {
        return QuickShops.textManager()
                .of(customer.qsUser(), "not-enough-space", actualSpace)
                .forLocale(customer.locale().toString());
    }

    public static Component errorCustomerInsufficientFunds(final UserSession customer, final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        final String world = shopComponent.location().getWorld().getName();
        final String formattedPrice = EconomyFormatter.format(shopComponent.price(), world, shopComponent.currency());
        final String formattedBalance = EconomyFormatter.format(
                customer.balance(world, shopComponent.currency()), world, shopComponent.currency());
        return QuickShops.textManager()
                .of(customer.qsUser(), "you-cant-afford-to-buy", formattedPrice, formattedBalance)
                .forLocale(customer.locale().toString());
    }

    public static Component errorCustomerInsufficientItems(final UserSession customer, final ShopBlock shop) {
        return QuickShops.textManager()
                .of(customer.qsUser(), "you-dont-have-that-many-items",
                        0,
                        shop.component().product().effectiveName())
                .forLocale(customer.locale().toString());
    }
}
