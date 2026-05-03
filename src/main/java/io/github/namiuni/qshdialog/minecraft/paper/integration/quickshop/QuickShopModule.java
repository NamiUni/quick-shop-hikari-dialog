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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.RankLimiter;
import com.ghostchu.quickshop.api.economy.EconomyProvider;
import com.ghostchu.quickshop.api.inventory.InventoryWrapperManager;
import com.ghostchu.quickshop.api.localization.text.TextManager;
import com.ghostchu.quickshop.api.shop.ItemMatcher;
import com.ghostchu.quickshop.api.shop.PriceLimiter;
import com.ghostchu.quickshop.api.shop.ShopManager;
import com.ghostchu.quickshop.api.shop.interaction.InteractionManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QuickShopDataDirectory;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QuickShopModule extends AbstractModule {

    private final QuickShop quickShop;

    public QuickShopModule(final QuickShop quickShop) {
        this.quickShop = quickShop;
    }

    @Provides
    @SuppressWarnings("unused")
    QuickShop quickShop() {
        return this.quickShop;
    }

    @Override
    protected void configure() {
        this.bind(Path.class).annotatedWith(QuickShopDataDirectory.class).toInstance(this.quickShop.getJavaPlugin().getDataPath());
        this.bind(InventoryWrapperManager.class).toInstance(this.quickShop.getInventoryWrapperManager());
        this.bind(TextManager.class).toInstance(this.quickShop.text());
        this.bind(ShopManager.class).toInstance(this.quickShop.getShopManager());
        this.bind(EconomyProvider.class).toInstance(this.quickShop.getEconomyLoader().provider());
        this.bind(ItemMatcher.class).toInstance(this.quickShop.getItemMatcher());
        this.bind(InteractionManager.class).toInstance(this.quickShop.getInteractionManager());
        this.bind(RankLimiter.class).toInstance(this.quickShop.getRankLimiter());
        this.bind(PriceLimiter.class).toInstance(this.quickShop.getShopManager().getPriceLimiter());
    }
}
