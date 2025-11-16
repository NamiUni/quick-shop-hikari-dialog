package io.github.namiuni.qshdialog.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PlayerHead {

    private PlayerHead() {
    }

    public static ItemStack of(final Player player) {
        final ItemStack playerHead = ItemStack.of(Material.PLAYER_HEAD);
        playerHead.editMeta(itemMeta -> {
            if (itemMeta instanceof SkullMeta skullMeta) {
                skullMeta.setOwningPlayer(player);
            }

            itemMeta.displayName(player.name().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        });

        return playerHead;
    }

    public static ItemStack steve(final Component name) {
        final ItemStack playerHead = ItemStack.of(Material.PLAYER_HEAD);
        playerHead.editMeta(itemMeta -> itemMeta.displayName(name.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));

        return playerHead;
    }
}
