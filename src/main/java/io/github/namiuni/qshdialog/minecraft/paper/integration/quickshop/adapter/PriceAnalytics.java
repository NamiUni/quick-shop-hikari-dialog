package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.shop.PriceLimiter;
import com.ghostchu.quickshop.api.shop.PriceLimiterCheckResult;
import io.github.namiuni.qshdialog.common.utilities.NumberRange;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import java.math.BigDecimal;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

// TODO: Cache
@NullMarked
public final class PriceAnalytics {

    private static final PriceLimiter PRICE_LIMITER = QuickShop.getInstance().getShopManager().getPriceLimiter();

    private PriceAnalytics() {
    }

    public static NumberRange<BigDecimal> getPriceLimit(final UserSession user, final ItemStack product) {
        final PriceLimiterCheckResult result = PRICE_LIMITER.check(user.qsUser(), product, null, 1.0);
        return new NumberRange<>(
                BigDecimal.valueOf(result.getMin()),
                BigDecimal.valueOf(result.getMax())
        );
    }
}
