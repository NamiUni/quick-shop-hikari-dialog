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
package io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.configurations;

import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopInputType;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.annotations.ConfigHeader;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.annotations.ConfigName;
import java.util.List;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@NullMarked
@ConfigSerializable
@ConfigName("config.conf")
@ConfigHeader("""
        QuickShop-Hikari Dialog Addon Configuration
        
        To use the dialogs provided by this addon, set the behavior identifiers below
        in QuickShop-Hikari's "interaction.yml".
        
        Available Behaviors:
          TRADE_DIALOG        - Dialog for buying/selling items at a shop
          SHOP_CREATE_DIALOG  - Dialog for creating a new shop
          SHOP_EDIT_DIALOG    - Dialog for modifying an existing shop
        
        Example configuration:
          STANDING_LEFT_CLICK_SIGN: TRADE_DIALOG
          STANDING_RIGHT_CLICK_SIGN: SHOP_EDIT_DIALOG
          STANDING_LEFT_CLICK_SHOPBLOCK: TRADE_DIALOG
          STANDING_RIGHT_CLICK_SHOPBLOCK: NONE # Reserved for opening chest
          STANDING_LEFT_CLICK_CONTAINER: SHOP_CREATE_DIALOG
          STANDING_RIGHT_CLICK_CONTAINER:  NONE
        """)
public record PrimaryConfiguration(
        @Comment("""
                Fallback locale used for translation rendering when the player's locale cannot be determined.
                Example: en_US, ja_JP""")
        Locale defaultLocale,
        DialogConfig dialog
) {

    @ConfigSerializable
    public record DialogConfig(

            @Comment("""
                    Input fields displayed in the shop create dialog (SHOP_CREATE_DIALOG).
                    Available values: shop_name, trade_type, currency, unit, price, status, display, unlimited_stock""")
            List<ShopInputType> shopCreateInputs,

            @Comment("""
                    Input fields displayed in the shop edit dialog (SHOP_EDIT_DIALOG).
                    Available values: name, trade_type, currency, unit, price, status, display, unlimited_stock""")
            List<ShopInputType> shopEditInputs
    ) {
    }

    public static final PrimaryConfiguration DEFAULT = new PrimaryConfiguration(
            Locale.ROOT,
            new DialogConfig(
                    List.of(
                            ShopInputType.SHOP_NAME,
                            ShopInputType.TRADE_TYPE,
                            ShopInputType.CURRENCY,
                            ShopInputType.UNIT,
                            ShopInputType.PRICE,
                            ShopInputType.STATUS,
                            ShopInputType.DISPLAY,
                            ShopInputType.UNLIMITED_STOCK
                    ),
                    List.of(
                            ShopInputType.SHOP_NAME,
                            ShopInputType.TRADE_TYPE,
                            ShopInputType.CURRENCY,
                            ShopInputType.UNIT,
                            ShopInputType.PRICE,
                            ShopInputType.STATUS,
                            ShopInputType.DISPLAY,
                            ShopInputType.UNLIMITED_STOCK
                    )
            )
    );
}
