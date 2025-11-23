package io.github.namiuni.qshdialog.shop.dialog;

import io.github.namiuni.qshdialog.user.QSHUser;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record DialogProviderContext(QSHUser owner, QSHUser customer, ItemStack product) {
}
