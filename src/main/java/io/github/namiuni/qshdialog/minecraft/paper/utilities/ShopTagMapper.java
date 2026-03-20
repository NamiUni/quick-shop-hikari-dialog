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
package io.github.namiuni.qshdialog.minecraft.paper.utilities;

import io.github.namiuni.qshdialog.common.utilities.NumberRange;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.EconomyFormatter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopInventory;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.translation.Translations;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ShopTagMapper {

    private final Translations translations;

    public ShopTagMapper(final Translations translations) {
        this.translations = translations;
    }

    public TagResolver shopPlaceholders(final UserSession user, final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        final String worldName = shop.container().getWorld().getName();

        final String nameOrDefault = Optional.ofNullable(shopComponent.name())
                .filter(it -> !it.isEmpty())
                .orElse(shopComponent.owner().name() + "'s Shop");
        final Component tradeTypeComponent = this.translations.tradeType(
                user, shopComponent.tradeType(), TagResolver.empty());
        final BigDecimal ownerBalance = shopComponent.owner().balance(worldName, shopComponent.currency());

        return TagResolver.builder()
                .resolver(Placeholder.component("shop_product_name", shopComponent.product().getItemMeta().itemName()))
                .resolver(Placeholder.component("shop_product_display_name", shopComponent.product().effectiveName()))
                .resolver(Placeholder.parsed("shop_product_key", shopComponent.product().getType().key().asString()))
                .resolver(Placeholder.parsed("shop_owner_name", Objects.requireNonNullElse(shopComponent.owner().name(), "")))
                .resolver(Formatter.number("shop_owner_balance", ownerBalance))
                .resolver(Placeholder.parsed("shop_owner_balance_formatted", EconomyFormatter.format(ownerBalance, worldName)))
                .resolver(Formatter.number("shop_price", shopComponent.price()))
                .resolver(Placeholder.parsed("shop_price_formatted", EconomyFormatter.format(shopComponent.price(), worldName)))
                .resolver(Placeholder.component("shop_trade_type", tradeTypeComponent))
                .resolver(Placeholder.parsed("shop_name", Objects.requireNonNullElse(shopComponent.name(), "")))
                .resolver(Placeholder.parsed("shop_name_or_default", nameOrDefault))
                .resolver(Placeholder.parsed("shop_currency", shopComponent.currency() == null ? "" : shopComponent.currency()))
                .resolver(Placeholder.parsed("shop_display_visible", String.valueOf(!shopComponent.displayVisible())))
                .resolver(Placeholder.parsed("shop_infinite_stock", String.valueOf(shopComponent.infiniteStock())))
                .resolver(Placeholder.parsed("shop_stock", String.valueOf(ShopInventory.stockCount(shop))))
                .resolver(Placeholder.parsed("shop_space", String.valueOf(ShopInventory.spaceCount(shop))))
                .build();
    }

    public static TagResolver pricePlaceholders() {
        return TagResolver.resolver("price", (queue, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            }

            final List<String> parts = new ArrayList<>();
            while (queue.hasNext()) {
                parts.add(queue.pop().value());
            }

            final String qualifier = parts.getLast();
            if (!"min".equals(qualifier) && !"max".equals(qualifier)) {
                return Tag.preProcessParsed("");
            }
            final ItemStack item = RegistryAccess.registryAccess()
                    .getRegistry(RegistryKey.ITEM)
                    .getOrThrow(Key.key(parts.getFirst()))
                    .createItemStack();
            final NumberRange<BigDecimal> range = PriceAnalytics.getPriceLimit(user, item);
            final BigDecimal value = "min".equals(parts.getLast()) ? range.min() : range.max();
            return Tag.preProcessParsed(value.toPlainString());
        });
    }

    public static TagResolver quickshopPlaceholders() {
        return TagResolver.resolver("quickshop", (queue, context) -> {
            if (!queue.hasNext()) {
                return Tag.preProcessParsed("");
            }
            final String key = queue.pop().value();
            final Pointered target = context.targetOrThrow();
            if (!(target instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            }

            final List<String> args = new ArrayList<>();
            final PlainTextComponentSerializer plain = PlainTextComponentSerializer.plainText();
            while (queue.hasNext()) {
                final String rawArg = queue.pop().value();
                args.add(plain.serialize(context.deserialize(rawArg)));
            }

            final Component message = QuickShops.textManager()
                    .of(user.qsUser(), key, args.toArray())
                    .forLocale(user.locale().toString());
            return Tag.selfClosingInserting(message);
        });
    }
}
