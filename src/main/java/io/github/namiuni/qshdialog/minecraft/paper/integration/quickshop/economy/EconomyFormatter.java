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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy;

import com.ghostchu.quickshop.api.economy.EconomyProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
public final class EconomyFormatter {
    
    private final EconomyProvider economyProvider;

    @Inject
    EconomyFormatter(final EconomyProvider economyProvider) {
        this.economyProvider = economyProvider;
    }

    public String format(final BigDecimal value, final String world) {
        return this.format(value, world, null);
    }

    public String format(final BigDecimal value, final String world, final @Nullable String currency) {
        return this.economyProvider.format(value, world, currency);
    }
}
