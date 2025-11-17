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
package io.github.namiuni.qshdialog.shop.dialog;

import com.ghostchu.quickshop.api.shop.Shop;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.utility.PlayerHead;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DialogBodies {

    private DialogBodies() {
    }

    public static DialogBody shopName(final QSHUser customer, final Shop shop, final TagResolver shopTags) {
        final ItemStack ownerHead = Optional.ofNullable(shop.getOwner().getUniqueId())
                .flatMap(uuid -> Optional.ofNullable(Bukkit.getPlayer(uuid)))
                .map(PlayerHead::of)
                .orElse(PlayerHead.steve(MiniMessage.miniMessage().deserialize("<owner_name>", customer, shopTags)));

        final Component shopName = MiniMessage.miniMessage().deserialize("<shop_name>", customer, shopTags);

        return DialogBody.item(ownerHead)
                .description(DialogBody.plainMessage(shopName))
                .build();
    }

    public static DialogBody productSize(final QSHUser customer, final Shop shop, final TagResolver shopTags) {
        final ItemStack icon = shop.getItem().asOne();
        final Component text = MiniMessage.miniMessage().deserialize("x <product_size>", customer, shopTags);
        return DialogBody.item(icon)
                .description(DialogBody.plainMessage(text))
                .build();
    }

    public static DialogBody price(final QSHUser customer, final TagResolver shopTags) {
        final ItemStack priceIcon = ItemStack.of(Material.EMERALD);
        final Component text = MiniMessage.miniMessage().deserialize("<product_price>", customer, shopTags);

        return DialogBody.item(priceIcon)
                .description(DialogBody.plainMessage(text))
                .showTooltip(false)
                .build();
    }
}
