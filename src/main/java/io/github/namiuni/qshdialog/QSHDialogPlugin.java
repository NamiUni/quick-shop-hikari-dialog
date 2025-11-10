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
package io.github.namiuni.qshdialog;

import com.ghostchu.quickshop.QuickShop;
import io.github.namiuni.qshdialog.shop.behavior.QSContainerClickHandler;
import io.github.namiuni.qshdialog.shop.behavior.QSShopClickHandler;
import io.github.namiuni.qshdialog.user.QSHUserService;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QSHDialogPlugin extends JavaPlugin {


    private final QSHUserService userService;

    public QSHDialogPlugin(final QSHUserService userService) {
        this.userService = userService;
    }

    @Override
    public void onEnable() {
        final var shopHandler = new QSShopClickHandler(this.userService);
        final var containerHandler = new QSContainerClickHandler(this.userService);

        QuickShop.getInstance().getInteractionManager().behavior(shopHandler);
        QuickShop.getInstance().getInteractionManager().behavior(containerHandler);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
