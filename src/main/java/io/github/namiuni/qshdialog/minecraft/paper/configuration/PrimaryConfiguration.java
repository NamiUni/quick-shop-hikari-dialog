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
package io.github.namiuni.qshdialog.minecraft.paper.configuration;

import io.github.namiuni.qshdialog.minecraft.paper.configuration.annotations.ConfigHeader;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.annotations.ConfigName;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.ShopInputType;
import java.util.List;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
@ConfigName("config.conf")
@ConfigHeader("""
        QuickShop-Hikari Dialog Addon Configuration
        
        To use the shop operation dialogs provided by this addon,
        set the following behavior identifiers in QuickShop-Hikari's `interaction.yml`:
        
        Available Behaviors:
          ITEM_TRADING_DIALOG         - Shop creation dialog or item trading dialog
          SHOP_MODIFICATION_DIALOG  - Shop modification dialog
        
        Example configuration:
          STANDING_LEFT_CLICK_SIGN: ITEM_TRADING_DIALOG
          STANDING_RIGHT_CLICK_SIGN: SHOP_MODIFICATION_DIALOG
          STANDING_LEFT_CLICK_SHOPBLOCK: TRADE_DIALOG
        """)
public record PrimaryConfiguration(
        Locale defaultLocale,
        TranslationSource translationSource,
        List<ShopInputType> creationDialogInputs,
        List<ShopInputType> modificationDialogInputs
) {

    public static final PrimaryConfiguration DEFAULT = new PrimaryConfiguration(
            Locale.US,
            TranslationSource.PLUGIN,
            List.of(ShopInputType.NAME,
                    ShopInputType.TRADE_TYPE,
                    ShopInputType.CURRENCY,
                    ShopInputType.PRODUCT_QUANTITY,
                    ShopInputType.PRICE,
                    ShopInputType.STATUS,
                    ShopInputType.DISPLAY,
                    ShopInputType.STOCK
            ),
            List.of(ShopInputType.NAME,
                    ShopInputType.TRADE_TYPE,
                    ShopInputType.CURRENCY,
                    ShopInputType.PRODUCT_QUANTITY,
                    ShopInputType.PRICE,
                    ShopInputType.STATUS,
                    ShopInputType.DISPLAY,
                    ShopInputType.STOCK
            )
    );

    public enum TranslationSource {
        RESOURCE_PACK,
        PLUGIN
    }
}
