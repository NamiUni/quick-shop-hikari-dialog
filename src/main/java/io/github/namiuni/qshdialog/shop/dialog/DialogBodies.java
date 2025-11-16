package io.github.namiuni.qshdialog.shop.dialog;

import com.ghostchu.quickshop.api.shop.Shop;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.utility.PlayerHead;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import java.util.Objects;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DialogBodies {

    private DialogBodies() {
    }

    public static DialogBody buyDescription(final QSHUser customer, final Shop shop) {
        final Component buyDescription = TranslationMessages.tradeBodyBuyDescription(customer, shop);

        return DialogBody.plainMessage(buyDescription);
    }

    public static DialogBody shopName(final QSHUser customer, final Shop shop) {
        final Optional<Player> bukkitOwner = shop.getOwner().getBukkitPlayer();
        final ItemStack ownerHead = bukkitOwner
                .map(PlayerHead::of)
                .orElse(PlayerHead.steve(Component.text(Objects.requireNonNull(shop.getOwner().getUsername(), "Unknown"))));

        final Component shopName = Optional.ofNullable(shop.getShopName())
                .filter(name -> !name.isEmpty())
                .map(Component::text)
                .map(Component.class::cast)
                .orElseGet(() -> TranslationMessages.tradeBodyDefaultShopName(customer, shop));

        return DialogBody.item(ownerHead)
                .description(DialogBody.plainMessage(shopName))
                .build();
    }

    public static DialogBody bundleSize(final QSHUser customer, final Shop shop) {
        final ItemStack icon = shop.getItem();
        final Component text = TranslationMessages.tradeBodyProductName(customer, shop);
        return DialogBody.item(icon)
                .description(DialogBody.plainMessage(text))
                .build();
    }

    public static DialogBody price(final QSHUser customer, final Shop shop) {
        final ItemStack priceIcon = ItemStack.of(Material.EMERALD);
        final Component text = TranslationMessages.tradeBodyPrice(customer, shop);

        return DialogBody.item(priceIcon)
                .description(DialogBody.plainMessage(text))
                .showTooltip(false)
                .build();
    }
}
