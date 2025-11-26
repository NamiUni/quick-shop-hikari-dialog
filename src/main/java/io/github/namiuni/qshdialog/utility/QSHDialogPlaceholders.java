package io.github.namiuni.qshdialog.utility;

import com.ghostchu.quickshop.api.obj.QUser;
import com.ghostchu.quickshop.api.shop.PriceLimiterCheckResult;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.obj.QUserImpl;
import io.github.namiuni.qshdialog.shop.ShopMode;
import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public final class QSHDialogPlaceholders {

    private QSHDialogPlaceholders() {
    }

    public static TagResolver productPlaceholders(final ItemStack item) {
        final AtomicReference<@Nullable PriceLimiterCheckResult> priceInfo = new AtomicReference<>();
        return TagResolver.resolver("product", (queue, context) -> {
            if (!queue.hasNext()) {
                return Tag.preProcessParsed("You need to provide a item");
            }

            final Pointered target = context.targetOrThrow();
            if (target instanceof Identified) {
                final QUser user = QUserImpl.createFullFilled(
                        target.get(Identity.UUID).orElseThrow(),
                        target.get(Identity.NAME).orElseThrow(),
                        true
                );
                priceInfo.compareAndSet(null, QuickShopUtil.getPriceInfo(user, item));

                final String value = queue.pop().value();
                return switch (value) {
                    case "min_price" -> {
                        final double minPrice = Objects.requireNonNull(priceInfo.get()).getMin();
                        yield Tag.preProcessParsed(String.valueOf(minPrice));
                    }
                    case "max_price" -> {
                        final double maxPrice = Objects.requireNonNull(priceInfo.get()).getMax();
                        yield Tag.preProcessParsed(String.valueOf(maxPrice));
                    }
                    case "max_stack_size" -> {
                        final Integer max = item.getData(DataComponentTypes.MAX_STACK_SIZE);
                        yield Tag.preProcessParsed(String.valueOf(max));
                    }
                    default -> Tag.preProcessParsed("Unknown product information");
                };
            }

            return Tag.preProcessParsed("Unknown user");
        });
    }

    public static TagResolver shopPlaceholders(final Shop shop) {
        return TagResolver.resolver("shop", (queue, context) -> {
            if (!queue.hasNext()) {
                return Tag.preProcessParsed("You need to provide a shop information");
            }

            final String value = queue.pop().value();
            return switch (value) {
                case "owner_name" -> Tag.preProcessParsed(Objects.requireNonNullElse(shop.getOwner().getUsername(), ""));
                case "mode" -> Tag.selfClosingInserting(ShopMode.valueOf(shop.shopType().identifier()).displayName());
                case "price" -> Tag.preProcessParsed(String.valueOf(shop.getPrice()));
                case "shopname" -> Tag.preProcessParsed(Objects.requireNonNullElse(shop.getShopName(), ""));
                case "shopname_or_default" -> {
                    final String shopName = Optional.ofNullable(shop.getShopName())
                            .filter(it -> !it.isEmpty())
                            .orElse(shop.getOwner().getUsername() + "'s Shop");
                    yield Tag.preProcessParsed(shopName);
                }
                case "currency" -> Tag.preProcessParsed(shop.getCurrency() == null ? "" : shop.getCurrency());
                case "show_display" -> Tag.preProcessParsed(String.valueOf(!shop.isDisableDisplay()));
                case "is_unlimited" -> Tag.preProcessParsed(String.valueOf(shop.isUnlimited()));
                default -> Tag.preProcessParsed("Unknown shop information");
            };
        });
    }
}
