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
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.shop.behavior.QSContainerClickHandler;
import io.github.namiuni.qshdialog.shop.behavior.QSShopClickHandler;
import io.github.namiuni.qshdialog.shop.dialog.ItemPurchaseDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ItemSaleDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopCreationDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopModificationDialogFactory;
import io.github.namiuni.qshdialog.translation.TranslatorHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QSHDialogPlugin extends JavaPlugin {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;
    private final TranslatorHolder translatorHolder;

    public QSHDialogPlugin(
            final ConfigurationHolder<PrimaryConfiguration> configHolder,
            final TranslatorHolder translatorHolder
    ) {
        this.configHolder = configHolder;
        this.translatorHolder = translatorHolder;
    }

    @Override
    public void onEnable() {
        final var itemPurchaseDialogFactory = new ItemPurchaseDialogFactory(this.configHolder, this.translatorHolder);
        final var itemSaleDialogFactory = new ItemSaleDialogFactory(this.configHolder, this.translatorHolder);
        final var shopCreationDialogFactory = new ShopCreationDialogFactory(this.configHolder, this.translatorHolder);
        final var shopModificationDialogFactory = new ShopModificationDialogFactory(this.configHolder, this.translatorHolder);

        final var shopHandler = new QSShopClickHandler(shopModificationDialogFactory);
        final var containerHandler = new QSContainerClickHandler(
                shopCreationDialogFactory,
                itemPurchaseDialogFactory,
                itemSaleDialogFactory
        );

        QuickShop.getInstance().getInteractionManager().behavior(shopHandler);
        QuickShop.getInstance().getInteractionManager().behavior(containerHandler);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
