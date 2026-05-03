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
