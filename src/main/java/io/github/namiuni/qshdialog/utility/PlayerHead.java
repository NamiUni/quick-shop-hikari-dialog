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
