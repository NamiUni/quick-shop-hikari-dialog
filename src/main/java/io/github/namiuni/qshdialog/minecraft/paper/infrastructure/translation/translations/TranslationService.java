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
package io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations;

import io.github.namiuni.kotonoha.annotations.Key;
import io.github.namiuni.kotonoha.annotations.Locales;
import io.github.namiuni.kotonoha.annotations.Message;
import io.github.namiuni.kotonoha.annotations.ResourceBundle;
import java.math.BigDecimal;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ResourceBundle(baseName = "translations/messages")
public interface TranslationService {

    // =========================================================================
    // config reload
    // =========================================================================

    @Key("qsh_dialog.resource.config.reload.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Configuration reloaded successfully.")
    Component configReloadSuccess(Pointered target);

    @Key("qsh_dialog.resource.config.reload.fail")
    @Message(locale = Locales.ROOT, content = "<jis_red>Failed to reload configuration. See the console for details.")
    Component configReloadFailure(Pointered target);

    @Key("qsh_dialog.resource.translation.reload.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Translations reloaded successfully.")
    Component translationReloadSuccess(Pointered target);

    @Key("qsh_dialog.resource.translation.reload.fail")
    @Message(locale = Locales.ROOT, content = "<jis_red>Failed to reload translations. See the console for details.")
    Component translationReloadFailure(Pointered target);

    // =========================================================================
    // shop.create.dialog
    // =========================================================================

    @Key("qsh_dialog.shop.create.dialog.title")
    @Message(locale = Locales.ROOT, content = "Create Shop")
    Component shopCreationDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.description")
    @Message(locale = Locales.ROOT, content = "Set your new shop up, then click <b><jis_green>Create</jis_green></b>. (Cost: <shop_fee:create_formatted:'<player_name>'>)")
    Component shopCreationDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.input.shop_name")
    @Message(locale = Locales.ROOT, content = "Shop Name")
    Component shopCreationInputName(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.input.trade_type")
    @Message(locale = Locales.ROOT, content = "Trade Type")
    Component shopCreationInputTradeType(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.input.trade_type.selling")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.selling>")
    Component shopCreationInputTradeTypeSelling(Pointered target);

    @Key("qsh_dialog.shop.create.dialog.input.trade_type.buying")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.buying>")
    Component shopCreationInputTradeTypeBuying(Pointered target);

    @Key("qsh_dialog.shop.create.dialog.input.status")
    @Message(locale = Locales.ROOT, content = "Available")
    Component shopCreationInputStatus(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.input.display")
    @Message(locale = Locales.ROOT, content = "Display Item")
    Component shopCreationInputDisplay(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.input.currency")
    @Message(locale = Locales.ROOT, content = "Currency")
    Component shopCreationInputCurrency(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.input.unlimited_stock")
    @Message(locale = Locales.ROOT, content = "Unlimited Stock")
    Component shopCreationInputUnlimitedStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.input.price")
    @Message(locale = Locales.ROOT, content = "Price (Range: <price:'<shop:product_id>':min> - <price:'<shop:product_id>':max>)")
    Component shopCreationInputPrice(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.input.unit")
    @Message(locale = Locales.ROOT, content = "Unit per Trade")
    Component shopCreationInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.input.unit.format")
    @Message(locale = Locales.ROOT, content = "%s: %s")
    String shopCreationInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.button.confirm")
    @Message(locale = Locales.ROOT, content = "Create")
    Component shopCreationDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "<lang:gui.cancel>")
    Component shopCreationDialogCancel(Pointered target, TagResolver placeholders);

    // =========================================================================
    // shop.create.result
    // =========================================================================

    @Key("qsh_dialog.shop.create.success")
    @Message(locale = Locales.ROOT, content = "<quickshop:success-created-shop> <gray>(Cost: <total_cost_formatted>)</gray>\n<green>You placed <shop_count:'<player_name>'>/<shop_count_max:'<player_name>'> shops.")
    Component shopCreationSuccess(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.fail.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block was found.")
    Component shopCreationFailureNoTargetBlock(Pointered target);

    @Key("qsh_dialog.shop.create.fail.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>Not a container or wall sign.")
    Component shopCreationFailureInvalidBlock(Pointered target);

    @Key("qsh_dialog.shop.create.fail.already_exists")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-already-owned>")
    Component shopCreationFailureAlreadyExists(Pointered target);

    @Key("qsh_dialog.shop.create.fail.insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<jis_red>You need <total_cost_formatted> to create this shop.")
    Component shopCreationFailureInsufficientFunds(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.fail.price_out_of_range")
    @Message(locale = Locales.ROOT, content = "<jis_red>The price must be between <price:'<shop:product_id>':min> and <price:'<shop:product_id>':max>.")
    Component shopCreationFailurePriceOutOfRange(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.fail.container_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>No container was found.")
    Component shopCreationFailureContainerNotFound(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.fail.limit_reached")
    @Message(locale = Locales.ROOT, content = "<jis_red>You have reached your shop limit and cannot create any more shops.")
    Component shopCreationFailureLimitReached(Pointered target);

    @Key("qsh_dialog.shop.create.fail.price_invalid")
    @Message(locale = Locales.ROOT, content = "<jis_red>\"<input>\" is not a valid price.")
    Component shopCreationFailurePriceInvalid(Pointered target, String input);

    @Key("qsh_dialog.shop.create.fail.world_not_allowed")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shops cannot be created in this world.")
    Component shopCreationFailureWorldNotAllowed(Pointered target);

    @Key("qsh_dialog.shop.create.fail.product_not_allowed")
    @Message(locale = Locales.ROOT, content = "<jis_red>This item cannot be used as a shop product.")
    Component shopCreationFailureProductNotAllowed(Pointered target);

    // =========================================================================
    // shop.edit.dialog
    // =========================================================================

    @Key("qsh_dialog.shop.edit.dialog.title")
    @Message(locale = Locales.ROOT, content = "Edit Shop")
    Component shopModificationDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.description")
    @Message(locale = Locales.ROOT, content = "Edit your shop settings, then click <b><jis_green><lang:gui.done></jis_green></b>.")
    Component shopModificationDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.input.shop_name")
    @Message(locale = Locales.ROOT, content = "Shop Name (Cost: <shop_fee:change_name_formatted:'<player_name>'>)")
    Component shopModificationInputName(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.input.trade_type")
    @Message(locale = Locales.ROOT, content = "Trade Type")
    Component shopModificationInputTradeType(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.input.trade_type.selling")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.selling>")
    Component shopModificationInputTradeTypeSelling(Pointered target);

    @Key("qsh_dialog.shop.edit.dialog.input.trade_type.buying")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.buying>")
    Component shopModificationInputTradeTypeBuying(Pointered target);

    @Key("qsh_dialog.shop.edit.dialog.input.status")
    @Message(locale = Locales.ROOT, content = "Available")
    Component shopModificationInputStatus(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.input.display")
    @Message(locale = Locales.ROOT, content = "Display Item")
    Component shopModificationInputDisplay(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.input.currency")
    @Message(locale = Locales.ROOT, content = "Currency")
    Component shopModificationInputCurrency(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.input.unlimited_stock")
    @Message(locale = Locales.ROOT, content = "Unlimited Stock")
    Component shopModificationInputUnlimitedStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.input.price")
    @Message(locale = Locales.ROOT, content = "Price (Range: <price:'<shop:product_id>':min> - <price:'<shop:product_id>':max>, Cost: <shop_fee:change_price_formatted:'<player_name>'>)")
    Component shopModificationInputPrice(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.input.unit")
    @Message(locale = Locales.ROOT, content = "Unit per Trade")
    Component shopModificationInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.input.unit.format")
    @Message(locale = Locales.ROOT, content = "%s: %s")
    String shopModificationInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.button.confirm")
    @Message(locale = Locales.ROOT, content = "<lang:gui.done>")
    Component shopModificationDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "<lang:gui.cancel>")
    Component shopModificationDialogCancel(Pointered target, TagResolver placeholders);

    // =========================================================================
    // shop.edit.result
    // =========================================================================

    @Key("qsh_dialog.shop.edit.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Shop updated. <gray>(Cost: <total_cost_formatted>)")
    Component shopModificationSuccess(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.fail.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block was found.")
    Component shopModificationFailureNoTargetBlock(Pointered target);

    @Key("qsh_dialog.shop.edit.fail.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>Not a container or wall sign.")
    Component shopModificationFailureInvalidBlock(Pointered target);

    @Key("qsh_dialog.shop.edit.fail.shop_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>No shop was found.")
    Component shopModificationFailureShopNotFound(Pointered target);

    @Key("qsh_dialog.shop.edit.fail.insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<jis_red>You need <total_cost_formatted> to apply this change.")
    Component shopModificationFailureInsufficientFunds(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.fail.price_out_of_range")
    @Message(locale = Locales.ROOT, content = "<jis_red>The price must be between <price:'<shop:product_id>':min> and <price:'<shop:product_id>':max>.")
    Component shopModificationFailurePriceOutOfRange(Pointered target, BigDecimal input, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.fail.price_invalid")
    @Message(locale = Locales.ROOT, content = "<jis_red>\"<input>\" is not a valid price.")
    Component shopModificationFailurePriceInvalid(Pointered target, String input, TagResolver placeholders);

    // =========================================================================
    // trade.purchase.dialog
    // =========================================================================

    @Key("qsh_dialog.trade.purchase.dialog.title")
    @Message(locale = Locales.ROOT, content = "<head:'<shop:owner_name>'> <shop:name_or:'<shop:owner_name>'s Shop'>")
    Component tradePurchaseDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.description")
    @Message(locale = Locales.ROOT, content = "Enter the amount, then click <b><jis_green>Buy</jis_green></b>.")
    Component tradePurchaseDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.input.amount")
    @Message(locale = Locales.ROOT, content = "Amount")
    Component tradePurchaseInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.input.amount.format")
    @Message(locale = Locales.ROOT, content = "%s: %s")
    String tradePurchaseInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.button.confirm")
    @Message(locale = Locales.ROOT, content = "Buy")
    Component tradePurchaseDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "<lang:gui.cancel>")
    Component tradePurchaseDialogCancel(Pointered target, TagResolver placeholders);

    // =========================================================================
    // trade.purchase.fail
    // =========================================================================

    @Key("qsh_dialog.trade.purchase.fail.shop_out_of_stock")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-stock-too-low:'<shop:stock>':'<shop:product_display_name>'>")
    Component tradePurchaseFailureShopOutOfStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.fail.customer_inventory_full")
    @Message(locale = Locales.ROOT, content = "<quickshop:not-enough-space:'<shop:space>'>")
    Component tradePurchaseFailureCustomerInventoryFull(Pointered target, int actualSpace, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.fail.customer_insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<quickshop:you-cant-afford-to-buy:'<shop:price_formatted>':'<player_balance_formatted>'>")
    Component tradePurchaseFailureCustomerInsufficientFunds(Pointered target, TagResolver placeholders);

    // =========================================================================
    // trade.sell.dialog
    // =========================================================================

    @Key("qsh_dialog.trade.sell.dialog.title")
    @Message(locale = Locales.ROOT, content = "<head:'<shop:owner_name>'> <shop:name_or:'<shop:owner_name>'s Shop'>")
    Component tradeSellDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.description")
    @Message(locale = Locales.ROOT, content = "Enter the amount, then click <b><jis_green>Sell</jis_green></b>.")
    Component tradeSellDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.input.amount")
    @Message(locale = Locales.ROOT, content = "Amount")
    Component tradeSellInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.input.quantity.format")
    @Message(locale = Locales.ROOT, content = "%s: %s")
    String tradeSellInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.button.confirm")
    @Message(locale = Locales.ROOT, content = "Sell")
    Component tradeSellDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "<lang:gui.cancel>")
    Component tradeSellDialogCancel(Pointered target, TagResolver placeholders);

    // =========================================================================
    // trade.sell.fail
    // =========================================================================

    @Key("qsh_dialog.trade.sell.fail.shop_inventory_full")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-has-no-space:'<shop:space>':'<shop:product_display_name>'>")
    Component tradeSellFailureShopInventoryFull(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.fail.shop_insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<quickshop:the-owner-cant-afford-to-buy-from-you:'<shop:price_formatted>':'<shop:owner_balance_formatted>'>")
    Component tradeSellFailureShopInsufficientFunds(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.fail.customer_insufficient_items")
    @Message(locale = Locales.ROOT, content = "<quickshop:you-dont-have-that-many-items:0:'<shop:product_display_name>'>")
    Component tradeSellFailureCustomerInsufficientItems(Pointered target, TagResolver placeholders);

    // =========================================================================
    // trade.common.fail
    // =========================================================================

    @Key("qsh_dialog.trade.fail.shop_unavailable")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-cannot-trade-when-freezing>")
    Component tradeCommonFailureShopUnavailable(Pointered target);

    @Key("qsh_dialog.trade.fail.shop_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>No shop was found.")
    Component tradeCommonFailureShopNotFound(Pointered target);

    @Key("qsh_dialog.trade.fail.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block was found.")
    Component tradeCommonFailureNoTargetBlock(Pointered target);

    @Key("qsh_dialog.trade.fail.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>Not a container or wall sign.")
    Component tradeCommonFailureInvalidBlock(Pointered target);
}
