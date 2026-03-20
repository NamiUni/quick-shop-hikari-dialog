package io.github.namiuni.qshdialog.minecraft.paper.service;

import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSConfigurations;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ShopCreationFilter {

    private ShopCreationFilter() {
    }

    public static boolean isCreationAllowed(final World world, final ItemStack handItem) {
        return isWorldAllowed(world) && isItemAllowed(handItem);
    }

    private static boolean isWorldAllowed(final World world) {
        final List<String> whitelist = QSConfigurations.whitelistWorlds();
        if (!whitelist.isEmpty()) {
            // whitelist が設定されている場合は優先。未掲載ワールドは全拒否
            return whitelist.contains(world.getName());
        }
        // whitelist が空の場合のみ blacklist を参照
        return !QSConfigurations.blacklistWorlds().contains(world.getName());
    }

    private static boolean isItemAllowed(final ItemStack handItem) {
        final Material type = handItem.getType();

        final boolean itemBlacklisted = QSConfigurations.blacklistItems().stream()
                .anyMatch(type::equals);
        if (itemBlacklisted) {
            return false;
        }

        final List<String> blacklistLore = QSConfigurations.blacklistLore();
        if (blacklistLore.isEmpty()) {
            return true;
        }
        final List<Component> lore = handItem.lore();
        if (lore == null || lore.isEmpty()) {
            return true;
        }

        final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        return lore.stream()
                .map(serializer::serialize)
                .noneMatch(line -> blacklistLore.stream().anyMatch(line::contains));
    }
}
