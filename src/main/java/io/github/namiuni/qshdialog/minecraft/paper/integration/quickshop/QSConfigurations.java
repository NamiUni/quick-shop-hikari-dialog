package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop;

import java.math.BigDecimal;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class QSConfigurations {

    private QSConfigurations() {
    }

    public static boolean supportsMultiCurrency() {
        return QuickShops.economyProvider().multiCurrency();
    }

    public static boolean supportsBulkTransaction() {
        return QuickShops.configuration().getBoolean("shop.allow-stacks");
    }

    public static int shopNameMaxLength() {
        return QuickShops.configuration().getInt("shop.name-max-length", 32);
    }

    public static BigDecimal shopNamingCost() {
        return BigDecimal.valueOf(QuickShops.configuration().getDouble("shop.name-fee", 0.0));
    }

    public static BigDecimal shopCreateCost() {
        return BigDecimal.valueOf(QuickShops.configuration().getDouble("shop.paid", 10.0));
    }

    public static BigDecimal shopPriceChangeCost() {
        if (!QuickShops.configuration().getBoolean("price-change-requires-fee", true)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(QuickShops.configuration().getDouble("shop.fee-for-price-change", 50.0));
    }

    public static @Nullable String getCurrency() {
        return QuickShops.configuration().getString("acceptedCurrency");
    }

    public static boolean requiresUnlimitedOwnerPayment() {
        return QuickShops.configuration().getBoolean("shopBlock.pay-isInfiniteStock-shopBlock-owners");
    }
}
