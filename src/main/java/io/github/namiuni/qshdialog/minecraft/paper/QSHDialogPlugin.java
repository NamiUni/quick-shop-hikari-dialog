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
package io.github.namiuni.qshdialog.minecraft.paper;

import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopCreationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopModificationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradePurchaseDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradeSellDialog;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import io.github.namiuni.qshdialog.minecraft.paper.interaction.ShopCreationDialogHandler;
import io.github.namiuni.qshdialog.minecraft.paper.interaction.ShopModificationDialogHandler;
import io.github.namiuni.qshdialog.minecraft.paper.interaction.TradeDialogHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QSHDialogPlugin extends JavaPlugin {

    private final ShopCreationDialog shopCreationDialog;
    private final ShopModificationDialog shopModificationDialog;
    private final TradePurchaseDialog tradePurchaseDialog;
    private final TradeSellDialog tradeSellDialog;

    public QSHDialogPlugin(
            final ShopCreationDialog shopCreationDialog,
            final ShopModificationDialog shopModificationDialog,
            final TradePurchaseDialog tradePurchaseDialog,
            final TradeSellDialog tradeSellDialog
    ) {
        this.shopCreationDialog = shopCreationDialog;
        this.shopModificationDialog = shopModificationDialog;
        this.tradePurchaseDialog = tradePurchaseDialog;
        this.tradeSellDialog = tradeSellDialog;
    }

    @Override
    public void onEnable() {
        final var shopModificationDialogHandler = new ShopModificationDialogHandler(this.shopModificationDialog);
        final var shopCreationDialogHandler = new ShopCreationDialogHandler(this.shopCreationDialog);
        final var tradeDialogHandler = new TradeDialogHandler(this.tradePurchaseDialog, this.tradeSellDialog);

        QuickShops.interactionManager().behavior(shopModificationDialogHandler);
        QuickShops.interactionManager().behavior(shopCreationDialogHandler);
        QuickShops.interactionManager().behavior(tradeDialogHandler);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
