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
import com.ghostchu.quickshop.api.economy.EconomyProvider;
import com.ghostchu.quickshop.api.inventory.InventoryWrapperManager;
import com.ghostchu.quickshop.api.localization.text.TextManager;
import com.ghostchu.quickshop.api.shop.ItemMatcher;
import com.ghostchu.quickshop.api.shop.ShopManager;
import com.ghostchu.quickshop.api.shop.interaction.InteractionManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QuickShops {

    private QuickShops() {
    }

    public static QuickShop quickShop() {
        return QuickShop.getInstance();
    }

    public static YamlDocument configuration() {
        return quickShop().getConfig();
    }

    public static InventoryWrapperManager inventoryWrapperManager() {
        return quickShop().getInventoryWrapperManager();
    }

    public static TextManager textManager() {
        return quickShop().text();
    }

    public static ShopManager shopManager() {
        return quickShop().getShopManager();
    }

    public static EconomyProvider economyProvider() {
        return Objects.requireNonNull(quickShop().getEconomyManager().provider());
    }

    public static ItemMatcher itemMatcher() {
        return quickShop().getItemMatcher();
    }

    public static InteractionManager interactionManager() {
        return quickShop().getInteractionManager();
    }
}
