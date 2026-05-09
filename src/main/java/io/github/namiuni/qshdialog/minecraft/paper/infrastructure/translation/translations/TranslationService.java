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

    @Key("qsh_dialog.config.reload.message.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Configuration reloaded.")
    @Message(locale = Locales.JA_JP, content = "<jis_green>設定を再読み込みしました。")
    Component configReloadSuccess(Pointered target);

    @Key("qsh_dialog.config.reload.message.fail")
    @Message(locale = Locales.ROOT, content = "<jis_red>Failed to reload configuration. Check the console for details.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>設定の再読み込みに失敗しました。詳細はコンソールを確認してください。")
    Component configReloadFail(Pointered target);

    @Key("qsh_dialog.translation.message.reload.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Translations reloaded.")
    @Message(locale = Locales.JA_JP, content = "<jis_green>翻訳を再読み込みしました。")
    Component translationReloadSuccess(Pointered target);

    @Key("qsh_dialog.translation.reload.message.fail")
    @Message(locale = Locales.ROOT, content = "<jis_red>Failed to reload translations. Check the console for details.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>翻訳の再読み込みに失敗しました。詳細はコンソールを確認してください。")
    Component translationReloadFail(Pointered target);

    @Key("qsh_dialog.shop.create.title")
    @Message(locale = Locales.ROOT, content = "Create Shop")
    @Message(locale = Locales.JA_JP, content = "ショップ作成")
    Component shopCreateDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.description")
    @Message(locale = Locales.ROOT, content = "Configure your new shop, then click <b><jis_green>Create</jis_green></b>. (Cost: <user_cost:shop_create_formatted>)")
    @Message(locale = Locales.JA_JP, content = "ショップを設定したら <b><jis_green>作成</jis_green></b> をクリックしてください。 (費用: <user_cost:shop_create_formatted>)")
    Component shopCreateDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.input.shop_name")
    @Message(locale = Locales.ROOT, content = "Shop Name")
    @Message(locale = Locales.JA_JP, content = "ショップ名")
    Component shopCreateInputName(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.input.trade_type")
    @Message(locale = Locales.ROOT, content = "Trade Type")
    @Message(locale = Locales.JA_JP, content = "取引種別")
    Component shopCreateInputTradeType(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.input.trade_type.selling")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.selling>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-type.selling>")
    Component shopCreateInputTradeTypeSelling(Pointered target);

    @Key("qsh_dialog.shop.create.input.trade_type.buying")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.buying>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-type.buying>")
    Component shopCreateInputTradeTypeBuying(Pointered target);

    @Key("qsh_dialog.shop.create.input.status")
    @Message(locale = Locales.ROOT, content = "Open for Business")
    @Message(locale = Locales.JA_JP, content = "営業中")
    Component shopCreateInputStatus(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.input.display")
    @Message(locale = Locales.ROOT, content = "Display Item")
    @Message(locale = Locales.JA_JP, content = "アイテム展示")
    Component shopCreateInputDisplay(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.input.currency")
    @Message(locale = Locales.ROOT, content = "Currency")
    @Message(locale = Locales.JA_JP, content = "通貨")
    Component shopCreateInputCurrency(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.input.unlimited_stock")
    @Message(locale = Locales.ROOT, content = "Unlimited Stock")
    @Message(locale = Locales.JA_JP, content = "在庫無制限")
    Component shopCreateInputUnlimitedStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.input.price")
    @Message(locale = Locales.ROOT, content = "Price (Range: <price:'<shop_product_key>':min> - <price:'<shop_product_key>':max>)")
    @Message(locale = Locales.JA_JP, content = "価格 (範囲: <price:'<shop_product_key>':min> - <price:'<shop_product_key>':max>)")
    Component shopCreateInputPrice(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.input.quantity")
    @Message(locale = Locales.ROOT, content = "Units per Trade")
    @Message(locale = Locales.JA_JP, content = "1回の取引量")
    Component shopCreateInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.input.quantity.format")
    @Message(locale = Locales.ROOT, content = "%s: %s items")
    @Message(locale = Locales.JA_JP, content = "%s: %s 個")
    String shopCreateInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.button.confirm")
    @Message(locale = Locales.ROOT, content = "Create")
    @Message(locale = Locales.JA_JP, content = "作成")
    Component shopCreateDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.button.cancel")
    @Message(locale = Locales.ROOT, content = "<lang:gui.cancel>")
    @Message(locale = Locales.JA_JP, content = "<lang:gui.cancel>")
    Component shopCreateDialogCancel(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.message.success")
    @Message(locale = Locales.ROOT, content = "<quickshop:success-created-shop> <gray>(Cost: <total_cost_formatted>)</gray>\n<green>You placed <user_shops>/<user_shops_limit> shops.")
    @Message(locale = Locales.JA_JP, content = "<quickshop:success-created-shop> <gray>(Cost: <total_cost_formatted>)</gray>\n<green><user_shops>/<user_shops_limit>店舗設置しました。")
    Component shopCreateSuccess(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.message.fail.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block was found.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ブロックが見つかりません。")
    Component shopCreateFailNoTargetBlock(Pointered target);

    @Key("qsh_dialog.shop.create.message.fail.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>Not a container or wall sign.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>コンテナまたは壁面看板ではありません。")
    Component shopCreateFailInvalidBlock(Pointered target);

    @Key("qsh_dialog.shop.create.message.fail.already_exists")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-already-owned>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-already-owned>")
    Component shopCreateFailAlreadyExists(Pointered target);

    @Key("qsh_dialog.shop.create.message.fail.insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<jis_red>You need <total_cost_formatted> to create this shop.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>作成には <total_cost_formatted> が必要です。")
    Component shopCreateFailInsufficientFunds(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.message.fail.price_out_of_range")
    @Message(locale = Locales.ROOT, content = "<jis_red>The price must be between <price:'<shop_product_key>':min> and <price:'<shop_product_key>':max>.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>価格は <price:'<shop_product_key>':min> ～ <price:'<shop_product_key>':max> の範囲で設定してください。")
    Component shopCreateFailPriceOutOfRange(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.message.fail.container_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>No container was found.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>コンテナが見つかりません。")
    Component shopCreateFailContainerNotFound(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.create.message.fail.limit_reached")
    @Message(locale = Locales.ROOT, content = "<jis_red>You have reached your shop limit and cannot create any more shops.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成上限に達しているため、これ以上ショップを作成できません。")
    Component shopCreateFailLimitReached(Pointered target);

    @Key("qsh_dialog.shop.create.message.fail.price_invalid")
    @Message(locale = Locales.ROOT, content = "<jis_red>\"<input>\" is not a valid price.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>\"<input>\" は有効な価格ではありません。")
    Component shopCreateFailPriceInvalid(Pointered target, String input);

    @Key("qsh_dialog.shop.create.message.fail.world_not_allowed")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shops cannot be created in this world.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>このワールドではショップを作成できません。")
    Component shopCreateFailWorldNotAllowed(Pointered target);

    @Key("qsh_dialog.shop.create.message.fail.product_not_allowed")
    @Message(locale = Locales.ROOT, content = "<jis_red>This item cannot be used as a shop product.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>このアイテムはショップの商品として使用できません。")
    Component shopCreateFailProductNotAllowed(Pointered target);

    @Key("qsh_dialog.shop.edit.title")
    @Message(locale = Locales.ROOT, content = "Edit Shop")
    @Message(locale = Locales.JA_JP, content = "ショップ編集")
    Component shopEditDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.description")
    @Message(locale = Locales.ROOT, content = "Edit your shop settings, then click <b><jis_green><lang:gui.done></jis_green></b>.")
    @Message(locale = Locales.JA_JP, content = "設定を編集したら <b><jis_green><lang:gui.done></jis_green></b> をクリックしてください。")
    Component shopEditDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.button.confirm")
    @Message(locale = Locales.ROOT, content = "<lang:gui.done>")
    @Message(locale = Locales.JA_JP, content = "<lang:gui.done>")
    Component shopEditDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.button.cancel")
    @Message(locale = Locales.ROOT, content = "<lang:gui.cancel>")
    @Message(locale = Locales.JA_JP, content = "<lang:gui.cancel>")
    Component shopEditDialogCancel(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.message.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Shop updated. <gray>(Cost: <total_cost_formatted>)")
    @Message(locale = Locales.JA_JP, content = "<jis_green>ショップを更新しました。 <gray>(費用: <total_cost_formatted>)")
    Component shopEditSuccess(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.message.fail.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block in sight.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>視線の先にブロックが見つかりませんでした。")
    Component shopEditFailNoTargetBlock(Pointered target);

    @Key("qsh_dialog.shop.edit.message.fail.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>That block is not a container or wall sign.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>対象のブロックはコンテナまたは壁面看板ではありません。")
    Component shopEditFailInvalidBlock(Pointered target);

    @Key("qsh_dialog.shop.edit.message.fail.shop_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop not found.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップが見つかりません。")
    Component shopEditFailShopNotFound(Pointered target);

    @Key("qsh_dialog.shop.edit.message.fail.insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<jis_red>You need <total_cost_formatted> to apply this change.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>変更を適用するには <total_cost_formatted> 必要です。")
    Component shopEditFailInsufficientFunds(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.message.fail.price_out_of_range")
    @Message(locale = Locales.ROOT, content = "<jis_red>Price must be between <price:'<shop_product_key>':min> and <price:'<shop_product_key>':max>.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>価格は <price:'<shop_product_key>':min> ～ <price:'<shop_product_key>':max> の範囲で設定してください。")
    Component shopEditFailPriceOutOfRange(Pointered target, BigDecimal input, TagResolver placeholders);

    @Key("qsh_dialog.shop.edit.message.fail.price_invalid")
    @Message(locale = Locales.ROOT, content = "<jis_red>\"<input>\" is not a valid price.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>\"<input>\" は有効な価格ではありません。")
    Component shopEditFailPriceInvalid(Pointered target, String input, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.title")
    @Message(locale = Locales.ROOT, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    @Message(locale = Locales.JA_JP, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    Component tradePurchaseDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.description")
    @Message(locale = Locales.ROOT, content = "Enter the quantity, then click <b><jis_green>Buy</jis_green></b>.")
    @Message(locale = Locales.JA_JP, content = "数量を入力したら <b><jis_green>購入</jis_green></b> をクリックしてください。")
    Component tradePurchaseDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.input.quantity")
    @Message(locale = Locales.ROOT, content = "Quantity")
    @Message(locale = Locales.JA_JP, content = "数量")
    Component tradeInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.input.quantity.format")
    @Message(locale = Locales.ROOT, content = "%s: %s")
    @Message(locale = Locales.JA_JP, content = "%s: %s")
    String tradeInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.button.confirm")
    @Message(locale = Locales.ROOT, content = "Buy")
    @Message(locale = Locales.JA_JP, content = "購入")
    Component tradePurchaseDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.button.cancel")
    @Message(locale = Locales.ROOT, content = "<lang:gui.cancel>")
    @Message(locale = Locales.JA_JP, content = "<lang:gui.cancel>")
    Component tradePurchaseDialogCancel(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.message.fail.shop_out_of_stock")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-stock-too-low:'<shop_stock>':'<shop_product_display_name>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-stock-too-low:'<shop_stock>':'<shop_product_display_name>'>")
    Component tradePurchaseFailShopOutOfStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.message.fail.customer_inventory_full")
    @Message(locale = Locales.ROOT, content = "<quickshop:not-enough-space:'<shop_space>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:not-enough-space:'<shop_space>'>")
    Component tradePurchaseFailCustomerInventoryFull(Pointered target, int actualSpace, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.message.fail.customer_insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<quickshop:you-cant-afford-to-buy:'<shop_price_formatted>':'<user_balance_formatted>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:you-cant-afford-to-buy:'<shop_price_formatted>':'<user_balance_formatted>'>")
    Component tradePurchaseFailCustomerInsufficientFunds(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.title")
    @Message(locale = Locales.ROOT, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    @Message(locale = Locales.JA_JP, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    Component tradeSellDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.description")
    @Message(locale = Locales.ROOT, content = "Enter the quantity, then click <b><jis_green>Sell</jis_green></b>.")
    @Message(locale = Locales.JA_JP, content = "数量を入力したら <b><jis_green>売却</jis_green></b> をクリックしてください。")
    Component tradeSellDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.button.confirm")
    @Message(locale = Locales.ROOT, content = "Sell")
    @Message(locale = Locales.JA_JP, content = "売却")
    Component tradeSellDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.button.cancel")
    @Message(locale = Locales.ROOT, content = "<lang:gui.cancel>")
    @Message(locale = Locales.JA_JP, content = "<lang:gui.cancel>")
    Component tradeSellDialogCancel(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.message.fail.shop_inventory_full")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-has-no-space:'<shop_space>':'<shop_product_display_name>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-has-no-space:'<shop_space>':'<shop_product_display_name>'>")
    Component tradeSellFailShopInventoryFull(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.message.fail.shop_insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<quickshop:the-owner-cant-afford-to-buy-from-you:'<shop_price_formatted>':'<shop_owner_balance_formatted>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:the-owner-cant-afford-to-buy-from-you:'<shop_price_formatted>':'<shop_owner_balance_formatted>'>")
    Component tradeSellFailShopInsufficientFunds(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.message.fail.customer_insufficient_items")
    @Message(locale = Locales.ROOT, content = "<quickshop:you-dont-have-that-many-items:0:'<shop_product_display_name>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:you-dont-have-that-many-items:0:'<shop_product_display_name>'>")
    Component tradeSellFailCustomerInsufficientItems(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.common.message.fail.shop_unavailable")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-cannot-trade-when-freezing>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-cannot-trade-when-freezing>")
    Component tradeCommonFailShopUnavailable(Pointered target);

    @Key("qsh_dialog.trade.common.message.fail.shop_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>That shop no longer exists.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>対象のショップが見つかりません。")
    Component tradeCommonFailShopNotFound(Pointered target);

    @Key("qsh_dialog.trade.common.message.fail.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block in sight.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>視線の先にブロックが見つかりませんでした。")
    Component tradeCommonFailNoTargetBlock(Pointered target);

    @Key("qsh_dialog.trade.common.message.fail.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>That block is not a container or wall sign.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>対象のブロックはコンテナまたは壁面看板ではありません。")
    Component tradeCommonFailInvalidBlock(Pointered target);
}
