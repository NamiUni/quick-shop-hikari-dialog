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
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopInventory;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.translation.Translations;
import io.papermc.paper.datacomponent.DataComponentTypes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopTagMapper {

    private final Translations translations;

    public ShopTagMapper(final Translations translations) {
        this.translations = translations;
    }

    public TagResolver shopPlaceholders(final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        return TagResolver.resolver("shop", (queue, context) -> {
            if (!queue.hasNext()) {
                return Tag.preProcessParsed("You need to provide a shop information");
            }

            final String value = queue.pop().value();
            return switch (value) {
                case "product_name" -> Tag.selfClosingInserting(shopComponent.product().getItemMeta().itemName());
                case "product_display_name" -> Tag.selfClosingInserting(shopComponent.product().effectiveName());
                case "price" -> Tag.preProcessParsed(shopComponent.price().toPlainString());
                case "owner_name" -> Tag.preProcessParsed(Objects.requireNonNullElse(shopComponent.owner().name(), ""));
                case "owner_display_name" -> Tag.selfClosingInserting(shopComponent.owner().displayName());
                case "owner_balance" -> Tag.preProcessParsed(shopComponent.owner().balance(shop.container().getWorld().getName(), shopComponent.currency()).toPlainString());
                case "trade_type" -> {
                    final Pointered target = Objects.requireNonNullElse(context.target(), Audience.empty());
                    final Component component = this.translations.tradeType(target, shopComponent.tradeType(), TagResolver.empty());
                    yield Tag.selfClosingInserting(component);
                }
                case "name" -> Tag.preProcessParsed(Objects.requireNonNullElse(shopComponent.name(), ""));
                case "name_or_default" -> {
                    final String shopName = Optional.ofNullable(shopComponent.name())
                            .filter(it -> !it.isEmpty())
                            .orElse(shopComponent.owner().name() + "'s Shop");
                    yield Tag.preProcessParsed(shopName);
                }
                case "currency" -> Tag.preProcessParsed(shopComponent.currency() == null ? "" : shopComponent.currency());
                case "display_visible" -> Tag.preProcessParsed(String.valueOf(!shopComponent.displayVisible()));
                case "infinite_stock" -> Tag.preProcessParsed(String.valueOf(shopComponent.infiniteStock()));
                case "stock" -> Tag.preProcessParsed(String.valueOf(ShopInventory.stockCount(shop)));
                case "space" -> Tag.preProcessParsed(String.valueOf(ShopInventory.spaceCount(shop)));
                default -> Tag.preProcessParsed("Unknown shop information");
            };
        });
    }

    public TagResolver itemPlaceholders(final ItemStack product) {
        final AtomicReference<@Nullable NumberRange<BigDecimal>> priceRange = new AtomicReference<>();
        return TagResolver.resolver("item", (queue, context) -> {
            if (!queue.hasNext()) {
                return Tag.preProcessParsed("You need to provide a product");
            }

            final Pointered target = context.targetOrThrow();
            if (target instanceof UserSession user) {
                priceRange.compareAndSet(null, PriceAnalytics.getPriceLimit(user, product));

                final String value = queue.pop().value();
                return switch (value) {
                    case "min_price" -> {
                        final String minPrice = Objects.requireNonNull(priceRange.get()).min().toPlainString();
                        yield Tag.preProcessParsed(minPrice);
                    }
                    case "max_price" -> {
                        final String maxPrice = Objects.requireNonNull(priceRange.get()).max().toPlainString();
                        yield Tag.preProcessParsed(maxPrice);
                    }
                    case "max_quantity" -> {
                        final Integer maxQuantity = product.getData(DataComponentTypes.MAX_STACK_SIZE);
                        yield Tag.preProcessParsed(String.valueOf(maxQuantity));
                    }
                    default -> Tag.preProcessParsed("Unknown product information");
                };
            }

            return Tag.preProcessParsed("Unknown user");
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
