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
