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

import com.ghostchu.quickshop.api.localization.text.TextManager;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyFormatter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.PriceAnalytics;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopCostCalculator;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopCounter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopInventory;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.utilities.NumberRange;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import jakarta.inject.Inject;
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
public final class QSPlaceholders {

    private final TagResolver globalPlaceholders;
    private final TagResolver audiencePlaceholders;

    private final EconomyService economyService;
    private final EconomyFormatter economyFormatter;
    private final ShopInventory shopInventory;
    private final ShopCounter shopCounter;
    private final PriceAnalytics priceAnalytics;
    private final TextManager textManager;
    private final ShopCostCalculator costCalculator;
    private final QSConfiguration qsConfig;

    @Inject
    QSPlaceholders(
            final EconomyService economyService,
            final EconomyFormatter economyFormatter,
            final ShopInventory shopInventory,
            final ShopCounter shopCounter,
            final PriceAnalytics priceAnalytics,
            final TextManager textManager,
            final ShopCostCalculator costCalculator,
            final QSConfiguration qsConfig
    ) {
        this.economyService = economyService;
        this.economyFormatter = economyFormatter;
        this.shopInventory = shopInventory;
        this.shopCounter = shopCounter;
        this.priceAnalytics = priceAnalytics;
        this.textManager = textManager;
        this.costCalculator = costCalculator;
        this.qsConfig = qsConfig;

        this.globalPlaceholders = TagResolver.resolver(
                this.createPricePlaceholders(),
                this.createQuickshopPlaceholders()
        );
        this.audiencePlaceholders = TagResolver.resolver(
                this.createUserPlaceholders()
        );
    }

    public TagResolver globalPlaceholders() {
        return this.globalPlaceholders;
    }

    public TagResolver audiencePlaceholders() {
        return this.audiencePlaceholders;
    }

    public TagResolver shopPlaceholder(final ShopBlock shop) {
        final ShopComponent shopComponent = shop.component();
        final String worldName = shop.container().getWorld().getName();

        final String nameOrDefault = Optional.ofNullable(shopComponent.name())
                .filter(name -> !name.isEmpty())
                .orElse(shopComponent.owner().name() + "'s Shop");
        final Component tradeTypeComponent = switch (shopComponent.tradeType()) {
            case SELLING -> Component.translatable("qsh_dialog.trade_type.selling");
            case BUYING -> Component.translatable("qsh_dialog.trade_type.buying");
        };

        final BigDecimal ownerBalance = this.economyService.getBalance(
                shop.component().owner(),
                worldName,
                shopComponent.currency()
        );

        return TagResolver.builder()
                .resolver(Placeholder.component("shop_product_name", shopComponent.product().getItemMeta().itemName()))
                .resolver(Placeholder.component("shop_product_display_name", shopComponent.product().effectiveName()))
                .resolver(Placeholder.parsed("shop_product_key", shopComponent.product().getType().key().asString()))
                .resolver(Placeholder.parsed("shop_owner_name", Objects.requireNonNullElse(shopComponent.owner().name(), "")))
                .resolver(Formatter.number("shop_owner_balance", ownerBalance))
                .resolver(Placeholder.parsed("shop_owner_balance_formatted", this.economyFormatter.format(ownerBalance, worldName)))
                .resolver(Formatter.number("shop_price", shopComponent.price()))
                .resolver(Placeholder.parsed("shop_price_formatted", this.economyFormatter.format(shopComponent.price(), worldName)))
                .resolver(Placeholder.component("shop_trade_type", tradeTypeComponent))
                .resolver(Placeholder.parsed("shop_name", Objects.requireNonNullElse(shopComponent.name(), "")))
                .resolver(Placeholder.parsed("shop_name_or_default", nameOrDefault))
                .resolver(Placeholder.parsed("shop_currency", shopComponent.currency() == null ? "" : shopComponent.currency()))
                .resolver(Placeholder.parsed("shop_display_visible", String.valueOf(shopComponent.displayVisible())))
                .resolver(Placeholder.parsed("shop_infinite_stock", String.valueOf(shopComponent.infiniteStock())))
                .resolver(Placeholder.parsed("shop_stock", String.valueOf(this.shopInventory.stockCount(shop))))
                .resolver(Placeholder.parsed("shop_space", String.valueOf(this.shopInventory.spaceCount(shop))))
                .build();
    }

    private TagResolver createUserPlaceholders() {
        final TagResolver userBalance = TagResolver.resolver("user_balance", (_, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            } else {
                final BigDecimal balance = this.economyService.getBalance(user, user.world(), null);
                return Tag.preProcessParsed(balance.toPlainString());
            }
        });

        final TagResolver userBalanceFormatted = TagResolver.resolver("user_balance_formatted", (_, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            } else {
                final BigDecimal balance = this.economyService.getBalance(user, user.world(), null);
                final String formatted = this.economyFormatter.format(balance, user.world());
                return Tag.preProcessParsed(formatted);
            }
        });

        final TagResolver userCost = TagResolver.resolver("user_cost", (queue, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            }

            final List<String> parts = new ArrayList<>();
            while (queue.hasNext()) {
                parts.add(queue.pop().value());
            }

            final String qualifier = parts.getFirst();
            switch (qualifier) {
                case "shop_create" -> {
                    final BigDecimal createCost = this.costCalculator.calculateCreateCost(user);
                    return Tag.preProcessParsed(createCost.toPlainString());
                }
                case "shop_create_formatted" -> {
                    final BigDecimal createCost = this.costCalculator.calculateCreateCost(user);
                    return Tag.preProcessParsed(this.economyFormatter.format(createCost, user.world()));
                }
                case "shop_modify_name" -> {
                    final BigDecimal namingCost = this.costCalculator.calculateNamingCost(user);
                    return Tag.preProcessParsed(namingCost.toPlainString());
                }
                case "shop_modify_name_formatted" -> {
                    final BigDecimal namingCost = this.costCalculator.calculateNamingCost(user);
                    return Tag.preProcessParsed(this.economyFormatter.format(namingCost, user.world()));
                }
                case "shop_modify_price" -> {
                    final BigDecimal pricingCost = this.costCalculator.calculatePricingCost(user);
                    return Tag.preProcessParsed(pricingCost.toPlainString());
                }
                case "shop_modify_price_formatted" -> {
                    final BigDecimal pricingCost = this.costCalculator.calculatePricingCost(user);
                    return Tag.preProcessParsed(this.economyFormatter.format(pricingCost, user.world()));
                }
                default -> {
                    return Tag.preProcessParsed("");
                }
            }
        });

        final TagResolver userShops = TagResolver.resolver("user_shops", (_, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            } else {
                final int shops = this.qsConfig.isShopLimitEnabled()
                        ? this.shopCounter.currentShops(user)
                        : -1;
                return Tag.preProcessParsed(Integer.toString(shops));
            }
        });

        final TagResolver userShopsLimit = TagResolver.resolver("user_shops_limit", (_, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            } else {
                final int shopsLimit = this.qsConfig.isShopLimitEnabled()
                        ? this.shopCounter.maximumShops(user)
                        : -1;
                return Tag.preProcessParsed(Integer.toString(shopsLimit));
            }
        });

        return TagResolver.resolver(userBalance, userBalanceFormatted, userCost, userShops, userShopsLimit);
    }

    private TagResolver createPricePlaceholders() {
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
            final NumberRange<BigDecimal> range = this.priceAnalytics.priceRange(user, item);
            final BigDecimal value = "min".equals(parts.getLast()) ? range.min() : range.max();
            return Tag.preProcessParsed(value.toPlainString());
        });
    }

    private TagResolver createQuickshopPlaceholders() {
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

            final Component message = this.textManager
                    .of(user.qsUser(), key, args.toArray())
                    .forLocale(user.locale().toString());
            return Tag.selfClosingInserting(message);
        });
    }
}
