package io.github.namiuni.qshdialog.shop;

import com.ghostchu.quickshop.api.shop.Shop;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum ShopDisplay {
    SHOW(Component.translatable("qsh_dialog.dialog.option.shop.display_item.show")),
    HIDE(Component.translatable("qsh_dialog.dialog.option.shop.display_item.hide"));

    private final Component displayName;

    ShopDisplay(final Component displayName) {
        this.displayName = displayName;
    }

    public static ShopDisplay of(final Shop shop) {
        if (shop.isDisableDisplay()) {
            return HIDE;
        }

        return SHOW;
    }

    public Component displayName() {
        return this.displayName;
    }
}
