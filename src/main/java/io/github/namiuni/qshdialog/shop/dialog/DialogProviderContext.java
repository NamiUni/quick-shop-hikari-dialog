package io.github.namiuni.qshdialog.shop.dialog;

import io.github.namiuni.qshdialog.user.QSHUser;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record DialogProviderContext(QSHUser user, ItemStack product, TagResolver tagResolver) {

    //                .replace("<current_price>", price.current().toPlainString())
    //                .replace("<max_price>", price.max().toPlainString())
    //                .replace("<min_price>", price.min().toPlainString())
    //                .replace("<highest_price>", price.highest().toPlainString())
    //                .replace("<lowest_price>", price.lowest().toEngineeringString())
    //                .replace("<current_quantity>", Integer.toString(quantity.current()))
    //                .replace("<max_quantity>", Integer.toString(quantity.max()))
    //                .replace("<min_quantity>", Integer.toString(quantity.min()))
}
