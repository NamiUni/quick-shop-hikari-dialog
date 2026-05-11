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
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
                this.createPriceResolver(),
                this.createQuickShopResolver(),
                this.createShopCountResolver(),
                this.createShopFeeResolver()
        );

        this.audiencePlaceholders = this.createPlayerResolver();
    }

    public TagResolver globalPlaceholders() {
        return this.globalPlaceholders;
    }

    public TagResolver audiencePlaceholders() {
        return this.audiencePlaceholders;
    }

    public TagResolver shopTagResolver(final ShopBlock shop) {
        final ShopComponent component = shop.component();
        final String worldName = shop.container().getWorld().getName();

        return TagResolver.resolver("shop", (queue, context) -> {
            if (!queue.hasNext()) {
                return Tag.preProcessParsed("");
            }
            return switch (queue.pop().value()) {
                case "name" -> Tag.preProcessParsed(
                        Objects.requireNonNullElse(component.shopName(), ""));

                case "name_or" -> {
                    final String name = component.shopName();
                    if (name != null && !name.isEmpty()) {
                        yield Tag.preProcessParsed(name);
                    }
                    yield queue.hasNext()
                            ? Tag.selfClosingInserting(context.deserialize(queue.pop().value()))
                            : Tag.preProcessParsed("");
                }

                case "owner_name" -> Tag.preProcessParsed(component.owner().name());

                case "owner_balance" -> {
                    final BigDecimal balance = this.economyService.getBalance(
                            component.owner(), worldName, component.currency());
                    yield Tag.preProcessParsed(balance.toPlainString());
                }

                case "owner_balance_formatted" -> {
                    final BigDecimal balance = this.economyService.getBalance(
                            component.owner(), worldName, component.currency());
                    yield Tag.preProcessParsed(this.economyFormatter.format(balance, worldName));
                }

                case "price" -> Tag.preProcessParsed(component.price().toPlainString());

                case "price_formatted" -> Tag.preProcessParsed(
                        this.economyFormatter.format(component.price(), worldName));

                case "trade_type" -> Tag.selfClosingInserting(switch (component.tradeType()) {
                    case SELLING -> Component.translatable("qsh_dialog.trade_type.selling");
                    case BUYING -> Component.translatable("qsh_dialog.trade_type.buying");
                });

                case "currency" -> Tag.preProcessParsed(
                        Objects.requireNonNullElse(component.currency(), ""));

                case "stock" -> Tag.preProcessParsed(
                        String.valueOf(this.shopInventory.stockCount(shop)));

                case "space" -> Tag.preProcessParsed(
                        String.valueOf(this.shopInventory.spaceCount(shop)));

                case "display" -> Tag.preProcessParsed(
                        String.valueOf(component.displayVisible()));

                case "unlimited_stock" -> Tag.preProcessParsed(
                        String.valueOf(component.infiniteStock()));

                case "product_id" -> Tag.preProcessParsed(
                        component.product().getType().key().asString());

                case "product_name" -> Tag.selfClosingInserting(
                        component.product().getItemMeta().itemName());

                case "product_display_name" -> Tag.selfClosingInserting(
                        component.product().effectiveName());

                default -> Tag.preProcessParsed("");
            };
        });
    }

    // =========================================================================
    // Private resolver builders
    // =========================================================================

    private TagResolver createPlayerResolver() {
        final TagResolver playerName = TagResolver.resolver("player_name", (_, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            }
            return Tag.preProcessParsed(user.name());
        });

        final TagResolver playerDisplayName = TagResolver.resolver("player_display_name", (_, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            }
            return Tag.selfClosingInserting(user.displayName().asComponent());
        });

        final TagResolver playerBalance = TagResolver.resolver("player_balance", (_, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            }
            final BigDecimal balance = this.economyService.getBalance(user, user.world(), null);
            return Tag.preProcessParsed(balance.toPlainString());
        });

        final TagResolver playerBalanceFormatted = TagResolver.resolver("player_balance_formatted", (_, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            }
            final BigDecimal balance = this.economyService.getBalance(user, user.world(), null);
            return Tag.preProcessParsed(this.economyFormatter.format(balance, user.world()));
        });

        return TagResolver.resolver(playerName, playerDisplayName, playerBalance, playerBalanceFormatted);
    }

    private TagResolver createShopCountResolver() {
        final TagResolver shopCount = TagResolver.resolver("shop_count", (queue, context) -> {
            if (!queue.hasNext()) {
                return Tag.preProcessParsed("-1");
            }
            final UserSession user = this.resolvePlayerFromArg(queue.pop().value(), context);
            if (user == null) {
                return Tag.preProcessParsed("-1");
            }
            final int count = this.shopCounter.currentShops(user);
            return Tag.preProcessParsed(Integer.toString(count));
        });

        final TagResolver shopCountMax = TagResolver.resolver("shop_count_max", (queue, context) -> {
            if (!queue.hasNext()) {
                return Tag.preProcessParsed("-1");
            }
            final UserSession user = this.resolvePlayerFromArg(queue.pop().value(), context);
            if (user == null) {
                return Tag.preProcessParsed("-1");
            }
            final int max = this.qsConfig.isShopLimitEnabled()
                    ? this.shopCounter.maximumShops(user)
                    : -1;
            return Tag.preProcessParsed(Integer.toString(max));
        });

        return TagResolver.resolver(shopCount, shopCountMax);
    }

    private TagResolver createShopFeeResolver() {
        return TagResolver.resolver("shop_fee", (queue, context) -> {
            if (!queue.hasNext()) {
                return Tag.preProcessParsed("");
            }
            final String qualifier = queue.pop().value();

            final UserSession user = queue.hasNext()
                    ? this.resolvePlayerFromArg(queue.pop().value(), context)
                    : null;
            final String world = user != null
                    ? user.world()
                    : Bukkit.getWorlds().getFirst().getName();

            return switch (qualifier) {
                case "create" -> {
                    final BigDecimal cost = user != null
                            ? this.costCalculator.calculateCreateCost(user)
                            : this.qsConfig.shopCreateCost();
                    yield Tag.preProcessParsed(cost.toPlainString());
                }
                case "create_formatted" -> {
                    final BigDecimal cost = user != null
                            ? this.costCalculator.calculateCreateCost(user)
                            : this.qsConfig.shopCreateCost();
                    yield Tag.preProcessParsed(this.economyFormatter.format(cost, world));
                }
                case "edit_name" -> {
                    final BigDecimal cost = user != null
                            ? this.costCalculator.calculateNamingCost(user)
                            : this.qsConfig.shopModifyNameCost();
                    yield Tag.preProcessParsed(cost.toPlainString());
                }
                case "edit_name_formatted" -> {
                    final BigDecimal cost = user != null
                            ? this.costCalculator.calculateNamingCost(user)
                            : this.qsConfig.shopModifyNameCost();
                    yield Tag.preProcessParsed(this.economyFormatter.format(cost, world));
                }
                case "edit_price" -> {
                    final BigDecimal cost = user != null
                            ? this.costCalculator.calculatePricingCost(user)
                            : this.qsConfig.shopModifyPriceCost();
                    yield Tag.preProcessParsed(cost.toPlainString());
                }
                case "edit_price_formatted" -> {
                    final BigDecimal cost = user != null
                            ? this.costCalculator.calculatePricingCost(user)
                            : this.qsConfig.shopModifyPriceCost();
                    yield Tag.preProcessParsed(this.economyFormatter.format(cost, world));
                }
                default -> Tag.preProcessParsed("");
            };
        });
    }

    private TagResolver createPriceResolver() {
        return TagResolver.resolver("price", (queue, context) -> {
            if (!(context.target() instanceof UserSession user)) {
                return Tag.preProcessParsed("");
            }

            final List<String> parts = new ArrayList<>();
            while (queue.hasNext()) {
                parts.add(queue.pop().value());
            }
            if (parts.size() < 2) {
                return Tag.preProcessParsed("");
            }

            final @Subst("namespace") String itemId = parts.getFirst();
            final String qualifier = parts.getLast();
            if (!"min".equals(qualifier) && !"max".equals(qualifier)) {
                return Tag.preProcessParsed("");
            }

            final ItemStack item = RegistryAccess.registryAccess()
                    .getRegistry(RegistryKey.ITEM)
                    .getOrThrow(Key.key(itemId))
                    .createItemStack();
            final NumberRange<BigDecimal> range = this.priceAnalytics.priceRange(user, item);
            final BigDecimal value = "min".equals(qualifier) ? range.min() : range.max();
            return Tag.preProcessParsed(value.toPlainString());
        });
    }

    private TagResolver createQuickShopResolver() {
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
                args.add(plain.serialize(context.deserialize(queue.pop().value())));
            }

            final Component message = this.textManager
                    .of(user.qsUser(), key, args.toArray())
                    .forLocale(user.locale().toString());
            return Tag.selfClosingInserting(message);
        });
    }

    private @Nullable UserSession resolvePlayerFromArg(final String rawArg, final Context context) {
        final String name = PlainTextComponentSerializer.plainText().serialize(context.deserialize(rawArg));
        final Player player = Bukkit.getPlayerExact(name);
        return player != null ? UserSession.of(player) : null;
    }
}
