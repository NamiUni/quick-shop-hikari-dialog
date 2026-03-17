package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter;

import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import java.math.BigDecimal;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class EconomyFormatter {

    private EconomyFormatter() {
    }

    public static String format(final BigDecimal value, final String world) {
        return EconomyFormatter.format(value, world, null);
    }

    public static String format(final BigDecimal value, final String world, final @Nullable String currency) {
        return QuickShops.economyProvider().format(value, world, currency);
    }
}
