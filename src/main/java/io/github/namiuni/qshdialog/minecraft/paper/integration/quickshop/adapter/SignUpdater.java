package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter;

import com.ghostchu.quickshop.api.shop.Shop;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SignUpdater {

    private SignUpdater() {
    }

    public static void update(final ShopBlock shop, final Locale locale) {
        final Shop qsShop = QuickShops.shopManager().getShop(shop.container().getLocation());
        if (qsShop != null) {
            qsShop.setSignText(QuickShops.textManager().findRelativeLanguages(locale.toString()));
        }
    }
}
