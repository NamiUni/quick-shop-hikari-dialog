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
