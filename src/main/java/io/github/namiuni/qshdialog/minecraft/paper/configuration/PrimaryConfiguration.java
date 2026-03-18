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
          CREATION_DIALOG     - Dialog for creating a new shop
          MODIFICATION_DIALOG - Dialog for modifying an existing shop
        
        Example configuration:
          STANDING_LEFT_CLICK_SIGN: TRADE_DIALOG
          STANDING_RIGHT_CLICK_SIGN: MODIFICATION_DIALOG
          STANDING_LEFT_CLICK_SHOPBLOCK: TRADE_DIALOG
          STANDING_RIGHT_CLICK_SHOPBLOCK: NONE # Reserved for opening chest
          STANDING_LEFT_CLICK_CONTAINER: CREATION_DIALOG
          STANDING_RIGHT_CLICK_CONTAINER:  NONE
        """)
public record PrimaryConfiguration(
        @Comment("""
                Fallback locale used for translation rendering when the player's locale cannot be determined.
                Only effective when translation-source is PLUGIN.
                Example: en_US, ja_JP""")
        Locale defaultLocale,

        @Comment("""
                Controls how translations are delivered to players.
                  PLUGIN       - Rendered server-side using the plugin's translation files.
                                 The player's locale is resolved server-side, falling back to default-locale.
                  RESOURCE_PACK - Sent as translatable components and resolved client-side via resource pack.""")
        TranslationSource translationSource,

        @Comment("""
                Input fields displayed in the shop creation dialog (CREATION_DIALOG).
                Available values: name, trade_type, currency, product_quantity, price, status, display, stock""")
        List<ShopInputType> creationDialogInputs,

        @Comment("""
                Input fields displayed in the shop modification dialog (MODIFICATION_DIALOG).
                Available values: name, trade_type, currency, product_quantity, price, status, display, stock""")
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
