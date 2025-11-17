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
package io.github.namiuni.qshdialog.shop;

import com.ghostchu.quickshop.api.shop.Shop;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.block.Container;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Shops {

    private Shops() {
    }

    public static ContainerShopBuilder builder(final Container container) {
        return new ContainerShopBuilder(container);
    }

    public static TagResolver tagResolver(final Shop shop) {
        return TagResolver.builder()
                .resolver(Shops.productPrice(shop))
                .resolver(Shops.productName(shop))
                .resolver(Shops.productSize(shop))
                .resolver(Shops.displayed(shop))
                .resolver(Shops.ownerName(shop))
                .resolver(Shops.unlimited(shop))
                .resolver(Shops.type(shop))
                .resolver(Shops.currency(shop))
                .resolver(Shops.shopName(shop))
                .build();
    }

    private static TagResolver productName(final Shop shop) {
        return Placeholder.component("product_name", shop.getItem().effectiveName());
    }

    private static TagResolver productPrice(final Shop shop) {
        return Placeholder.component("product_price", Component.text(shop.getPrice()));
    }

    private static TagResolver productSize(final Shop shop) {
        return Placeholder.component("product_size", Component.text(shop.getItem().getAmount()));
    }

    private static TagResolver displayed(final Shop shop) {
        return Placeholder.component("displayed", Component.text(!shop.isDisableDisplay()));
    }

    private static TagResolver ownerName(final Shop shop) {
        return TagResolver.resolver("owner_name", (queue, context) -> {
            final Component ownerName = shop.ownerName();
            return Tag.selfClosingInserting(ownerName);
        });
    }

    private static TagResolver unlimited(final Shop shop) {
        return Placeholder.component("unlimited", Component.text(shop.isUnlimited()));
    }

    private static TagResolver type(final Shop shop) {
        final ShopMode shopMode = ShopMode.of(shop.shopType());
        return Placeholder.component("shop_type", shopMode.displayName());
    }

    private static TagResolver currency(final Shop shop) {
        return Optional.ofNullable(shop.getCurrency())
                .<Component>map(Component::text)
                .<TagResolver>map(currency -> Placeholder.component("currency", currency))
                .orElse(TagResolver.empty());
    }

    private static TagResolver shopName(final Shop shop) {
        return TagResolver.resolver("shop_name", (queue, context) -> {
            final String shopNameString = Objects.requireNonNull(shop.getShopName(), "");
            final Component shopName;
            if (shopNameString.isEmpty()) {
                final TranslatableComponent translatable = Component.translatable(
                        "qsh_dialog.shop.default_shop_name",
                        Argument.component("owner_name", shop.ownerName()),
                        Argument.target(context.targetOrThrow())
                );
                shopName = GlobalTranslator.render(translatable, context.targetOrThrow().getOrDefault(Identity.LOCALE, Locale.US));
            } else {
                shopName = Component.text(shopNameString);
            }

            return Tag.inserting(shopName);
        });
    }
}
