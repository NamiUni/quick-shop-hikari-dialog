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
    @Message(locale = Locales.JA_JP, content = "<jis_green>設定の再読み込みに成功しました。")
    Component configReloadSuccess(Pointered target);

    @Key("qsh_dialog.resource.config.reload.failure")
    @Message(locale = Locales.ROOT, content = "<jis_red>Failed to reload configuration. See the console for details.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>設定の再読み込みに失敗しました。詳細はコンソールを確認してください。")
    Component configReloadFailure(Pointered target);

    @Key("qsh_dialog.resource.translation.reload.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Translations reloaded successfully.")
    @Message(locale = Locales.JA_JP, content = "<jis_green>翻訳の再読み込みに成功しました。")
    Component translationReloadSuccess(Pointered target);

    @Key("qsh_dialog.resource.translation.reload.failure")
    @Message(locale = Locales.ROOT, content = "<jis_red>Failed to reload translations. See the console for details.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>翻訳の再読み込みに失敗しました。詳細はコンソールを確認してください。")
    Component translationReloadFailure(Pointered target);

    // =========================================================================
    // shop.creation.dialog
    // =========================================================================

    @Key("qsh_dialog.shop.creation.dialog.title")
    @Message(locale = Locales.ROOT, content = "Shop Creation")
    @Message(locale = Locales.JA_JP, content = "ショップ作成")
    Component shopCreationDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.description")
    @Message(locale = Locales.ROOT, content = "Configure your new shop below, then click <b><jis_green>Create</jis_green></b> to finish. (Cost: <user_cost:shop_create_formatted>)")
    @Message(locale = Locales.JA_JP, content = "ショップを作成します。以下の設定を確認・変更したら、<b><jis_green>作成</jis_green></b>をクリックして完了してください。 (費用: <user_cost:shop_create_formatted>)")
    Component shopCreationDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.input.name")
    @Message(locale = Locales.ROOT, content = "Shop Name")
    @Message(locale = Locales.JA_JP, content = "ショップ名")
    Component shopCreationInputName(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.input.trade_type")
    @Message(locale = Locales.ROOT, content = "Trade Type")
    @Message(locale = Locales.JA_JP, content = "取引種別")
    Component shopCreationInputTradeType(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.input.trade_type.selling")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.selling>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-type.selling>")
    Component shopCreationInputTradeTypeSelling(Pointered target);

    @Key("qsh_dialog.shop.creation.dialog.input.trade_type.buying")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.buying>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-type.buying>")
    Component shopCreationInputTradeTypeBuying(Pointered target);

    @Key("qsh_dialog.shop.creation.dialog.input.status")
    @Message(locale = Locales.ROOT, content = "Available")
    @Message(locale = Locales.JA_JP, content = "営業中")
    Component shopCreationInputStatus(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.input.display")
    @Message(locale = Locales.ROOT, content = "Display Item")
    @Message(locale = Locales.JA_JP, content = "アイテム展示")
    Component shopCreationInputDisplay(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.input.currency")
    @Message(locale = Locales.ROOT, content = "Currency")
    @Message(locale = Locales.JA_JP, content = "通貨")
    Component shopCreationInputCurrency(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.input.infinite_stock")
    @Message(locale = Locales.ROOT, content = "Infinite Stock")
    @Message(locale = Locales.JA_JP, content = "在庫無制限")
    Component shopCreationInputInfiniteStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.input.price")
    @Message(locale = Locales.ROOT, content = "Price (Range: <price:'<shop_product_key>':min> - <price:'<shop_product_key>':max>)")
    @Message(locale = Locales.JA_JP, content = "価格 (範囲: <price:'<shop_product_key>':min> - <price:'<shop_product_key>':max>)")
    Component shopCreationInputPrice(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.input.quantity")
    @Message(locale = Locales.ROOT, content = "Units per Transaction")
    @Message(locale = Locales.JA_JP, content = "1回の取引量")
    Component shopCreationInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.input.quantity.format")
    @Message(locale = Locales.ROOT, content = "%s: %s items")
    @Message(locale = Locales.JA_JP, content = "%s: %s 個")
    String shopCreationInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.button.confirm")
    @Message(locale = Locales.ROOT, content = "Create")
    @Message(locale = Locales.JA_JP, content = "作成")
    Component shopCreationDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "Cancel")
    @Message(locale = Locales.JA_JP, content = "キャンセル")
    Component shopCreationDialogCancel(Pointered target, TagResolver placeholders);

    // =========================================================================
    // shop.creation.result
    // =========================================================================

    @Key("qsh_dialog.shop.creation.success")
    @Message(locale = Locales.ROOT, content = "<quickshop:success-created-shop> <gray>(Cost: <total_cost_formatted>)</gray>\n<green>You placed <user_shops>/<user_shops_limit> shops.")
    @Message(locale = Locales.JA_JP, content = "<quickshop:success-created-shop> <gray>(Cost: <total_cost_formatted>)</gray>\n<green><user_shops>/<user_shops_limit>店舗設置しました。")
    Component shopCreationSuccess(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.failure.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block found in your line of sight.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。視線の先にブロックが見つかりませんでした。")
    Component shopCreationFailureNoTargetBlock(Pointered target);

    @Key("qsh_dialog.shop.creation.failure.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>The target block is not a container or wall sign.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。対象のブロックはコンテナまたは壁面看板ではありません。")
    Component shopCreationFailureInvalidBlock(Pointered target);

    @Key("qsh_dialog.shop.creation.failure.already_exists")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-already-owned>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-already-owned>")
    Component shopCreationFailureAlreadyExists(Pointered target);

    @Key("qsh_dialog.shop.creation.failure.insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop creation failed. You need <total_cost_formatted> to create this shop.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。作成には <total_cost_formatted> が必要です。")
    Component shopCreationFailureInsufficientFunds(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.failure.price_out_of_range")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop creation failed. The price must be between <price:'<shop_product_key>':min> and <price:'<shop_product_key>':max>.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。価格は <price:'<shop_product_key>':min> ～ <price:'<shop_product_key>':max> の範囲で設定してください。")
    Component shopCreationFailurePriceOutOfRange(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.failure.container_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop creation failed. No container was found at the target location.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。対象の場所にコンテナが見つかりませんでした。")
    Component shopCreationFailureContainerNotFound(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.failure.limit_reached")
    @Message(locale = Locales.ROOT, content = "<jis_red>You have reached your shop limit and cannot create any more shops.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成上限に達しているため、これ以上ショップを作成できません。")
    Component shopCreationFailureLimitReached(Pointered target);

    @Key("qsh_dialog.shop.creation.failure.price_invalid")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop creation failed. \"<input>\" is not a valid price.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。\"<input>\" は有効な価格ではありません。")
    Component shopCreationFailurePriceInvalid(Pointered target, String input);

    @Key("qsh_dialog.shop.creation.failure.world_not_allowed")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shops cannot be created in this world.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>このワールドではショップを作成できません。")
    Component shopCreationFailureWorldNotAllowed(Pointered target);

    @Key("qsh_dialog.shop.creation.failure.product_not_allowed")
    @Message(locale = Locales.ROOT, content = "<jis_red>This item cannot be used as a shop product.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>このアイテムはショップの商品として使用できません。")
    Component shopCreationFailureProductNotAllowed(Pointered target);

    // =========================================================================
    // shop.modification.dialog
    // =========================================================================

    @Key("qsh_dialog.shop.modification.dialog.title")
    @Message(locale = Locales.ROOT, content = "Shop Modification")
    @Message(locale = Locales.JA_JP, content = "ショップ編集")
    Component shopModificationDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.description")
    @Message(locale = Locales.ROOT, content = "Adjust your shop settings below, then click <b><jis_green>Apply</jis_green></b> to finish.")
    @Message(locale = Locales.JA_JP, content = "ショップを編集します。以下の設定を確認・変更したら、<b><jis_green>適用</jis_green></b>をクリックして完了してください。")
    Component shopModificationDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.input.name")
    @Message(locale = Locales.ROOT, content = "Shop Name (Rename Cost: <user_cost:shop_modify_name_formatted>)")
    @Message(locale = Locales.JA_JP, content = "ショップ名 (変更費用: <user_cost:shop_modify_name_formatted>)")
    Component shopModificationInputName(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.input.trade_type")
    @Message(locale = Locales.ROOT, content = "Trade Type")
    @Message(locale = Locales.JA_JP, content = "取引種別")
    Component shopModificationInputTradeType(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.input.trade_type.selling")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.selling>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-type.selling>")
    Component shopModificationInputTradeTypeSelling(Pointered target);

    @Key("qsh_dialog.shop.modification.dialog.input.trade_type.buying")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.buying>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-type.buying>")
    Component shopModificationInputTradeTypeBuying(Pointered target);

    @Key("qsh_dialog.shop.modification.dialog.input.status")
    @Message(locale = Locales.ROOT, content = "Available")
    @Message(locale = Locales.JA_JP, content = "営業中")
    Component shopModificationInputStatus(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.input.display")
    @Message(locale = Locales.ROOT, content = "Display Item")
    @Message(locale = Locales.JA_JP, content = "アイテム展示")
    Component shopModificationInputDisplay(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.input.currency")
    @Message(locale = Locales.ROOT, content = "Currency")
    @Message(locale = Locales.JA_JP, content = "通貨")
    Component shopModificationInputCurrency(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.input.infinite_stock")
    @Message(locale = Locales.ROOT, content = "Infinite Stock")
    @Message(locale = Locales.JA_JP, content = "在庫無制限")
    Component shopModificationInputInfiniteStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.input.price")
    @Message(locale = Locales.ROOT, content = "Price (Range: <price:'<shop_product_key>':min> - <price:'<shop_product_key>':max>, Cost: <user_cost:shop_modify_price_formatted>)")
    @Message(locale = Locales.JA_JP, content = "価格 (範囲: <price:'<shop_product_key>':min> - <price:'<shop_product_key>':max>, 変更費用: <user_cost:shop_modify_price_formatted>)")
    Component shopModificationInputPrice(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.input.quantity")
    @Message(locale = Locales.ROOT, content = "Units per Transaction")
    @Message(locale = Locales.JA_JP, content = "1回の取引量")
    Component shopModificationInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.input.quantity.format")
    @Message(locale = Locales.ROOT, content = "%s: %s items")
    @Message(locale = Locales.JA_JP, content = "%s: %s 個")
    String shopModificationInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.button.confirm")
    @Message(locale = Locales.ROOT, content = "Apply")
    @Message(locale = Locales.JA_JP, content = "適用")
    Component shopModificationDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "Cancel")
    @Message(locale = Locales.JA_JP, content = "キャンセル")
    Component shopModificationDialogCancel(Pointered target, TagResolver placeholders);

    // =========================================================================
    // shop.modification.result
    // =========================================================================

    @Key("qsh_dialog.shop.modification.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Your shop has been updated successfully. <gray>(Cost: <total_cost_formatted>)")
    @Message(locale = Locales.JA_JP, content = "<jis_green>ショップの設定を更新しました。 <gray>(Cost: <total_cost_formatted>)")
    Component shopModificationSuccess(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.failure.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block found in your line of sight.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。視線の先にブロックが見つかりませんでした。")
    Component shopModificationFailureNoTargetBlock(Pointered target);

    @Key("qsh_dialog.shop.modification.failure.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>The target block is not a container or wall sign.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。対象のブロックはコンテナまたは壁面看板ではありません。")
    Component shopModificationFailureInvalidBlock(Pointered target);

    @Key("qsh_dialog.shop.modification.failure.shop_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop modification failed. The shop could not be found.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。対象のショップが見つかりません。")
    Component shopModificationFailureShopNotFound(Pointered target);

    @Key("qsh_dialog.shop.modification.failure.insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop modification failed. You need <total_cost_formatted> to apply this change.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。変更を適用するには <total_cost_formatted> 必要です。")
    Component shopModificationFailureInsufficientFunds(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.failure.price_out_of_range")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop modification failed. The price must be between <price:'<shop_product_key>':min> and <price:'<shop_product_key>':max>.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。価格は <price:'<shop_product_key>':min> ～ <price:'<shop_product_key>':max> の範囲で設定してください。")
    Component shopModificationFailurePriceOutOfRange(Pointered target, BigDecimal input, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.failure.price_invalid")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop modification failed. \"<input>\" is not a valid price.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。\"<input>\" は有効な価格ではありません。")
    Component shopModificationFailurePriceInvalid(Pointered target, String input, TagResolver placeholders);

    // =========================================================================
    // trade.purchase.dialog
    // =========================================================================

    @Key("qsh_dialog.trade.purchase.dialog.title")
    @Message(locale = Locales.ROOT, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    @Message(locale = Locales.JA_JP, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    Component tradePurchaseDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.description")
    @Message(locale = Locales.ROOT, content = "Enter the quantity, then click <b><jis_green>Buy</jis_green></b> to complete your purchase.")
    @Message(locale = Locales.JA_JP, content = "数量を入力したら、<b><jis_green>購入</jis_green></b>をクリックして注文を確定してください。")
    Component tradePurchaseDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.input.quantity")
    @Message(locale = Locales.ROOT, content = "Quantity")
    @Message(locale = Locales.JA_JP, content = "数量")
    Component tradePurchaseInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.input.quantity.format")
    @Message(locale = Locales.ROOT, content = "%s: %s")
    @Message(locale = Locales.JA_JP, content = "%s: %s")
    String tradePurchaseInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.button.confirm")
    @Message(locale = Locales.ROOT, content = "Buy")
    @Message(locale = Locales.JA_JP, content = "購入")
    Component tradePurchaseDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "Cancel")
    @Message(locale = Locales.JA_JP, content = "キャンセル")
    Component tradePurchaseDialogCancel(Pointered target, TagResolver placeholders);

    // =========================================================================
    // trade.purchase.failure
    // =========================================================================

    @Key("qsh_dialog.trade.purchase.failure.shop_out_of_stock")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-stock-too-low:'<shop_stock>':'<shop_product_display_name>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-stock-too-low:'<shop_stock>':'<shop_product_display_name>'>")
    Component tradePurchaseFailureShopOutOfStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.failure.customer_inventory_full")
    @Message(locale = Locales.ROOT, content = "<quickshop:not-enough-space:'<shop_space>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:not-enough-space:'<shop_space>'>")
    Component tradePurchaseFailureCustomerInventoryFull(Pointered target, int actualSpace, TagResolver placeholders);

    @Key("qsh_dialog.trade.purchase.failure.customer_insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<quickshop:you-cant-afford-to-buy:'<shop_price_formatted>':'<user_balance_formatted>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:you-cant-afford-to-buy:'<shop_price_formatted>':'<user_balance_formatted>'>")
    Component tradePurchaseFailureCustomerInsufficientFunds(Pointered target, TagResolver placeholders);

    // =========================================================================
    // trade.sell.dialog
    // =========================================================================

    @Key("qsh_dialog.trade.sell.dialog.title")
    @Message(locale = Locales.ROOT, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    @Message(locale = Locales.JA_JP, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    Component tradeSellDialogTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.description")
    @Message(locale = Locales.ROOT, content = "Enter the quantity, then click <b><jis_green>Sell</jis_green></b> to complete your sale.")
    @Message(locale = Locales.JA_JP, content = "数量を入力したら、<b><jis_green>売却</jis_green></b>をクリックして取引を確定してください。")
    Component tradeSellDialogDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.input.quantity")
    @Message(locale = Locales.ROOT, content = "Quantity")
    @Message(locale = Locales.JA_JP, content = "数量")
    Component tradeSellInputQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.input.quantity.format")
    @Message(locale = Locales.ROOT, content = "%s: %s")
    @Message(locale = Locales.JA_JP, content = "%s: %s")
    String tradeSellInputQuantityFormat(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.button.confirm")
    @Message(locale = Locales.ROOT, content = "Sell")
    @Message(locale = Locales.JA_JP, content = "売却")
    Component tradeSellDialogConfirm(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "Cancel")
    @Message(locale = Locales.JA_JP, content = "キャンセル")
    Component tradeSellDialogCancel(Pointered target, TagResolver placeholders);

    // =========================================================================
    // trade.sell.failure
    // =========================================================================

    @Key("qsh_dialog.trade.sell.failure.shop_inventory_full")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-has-no-space:'<shop_space>':'<shop_product_display_name>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-has-no-space:'<shop_space>':'<shop_product_display_name>'>")
    Component tradeSellFailureShopInventoryFull(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.failure.shop_insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<quickshop:the-owner-cant-afford-to-buy-from-you:'<shop_price_formatted>':'<shop_owner_balance_formatted>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:the-owner-cant-afford-to-buy-from-you:'<shop_price_formatted>':'<shop_owner_balance_formatted>'>")
    Component tradeSellFailureShopInsufficientFunds(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.sell.failure.customer_insufficient_items")
    @Message(locale = Locales.ROOT, content = "<quickshop:you-dont-have-that-many-items:0:'<shop_product_display_name>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:you-dont-have-that-many-items:0:'<shop_product_display_name>'>")
    Component tradeSellFailureCustomerInsufficientItems(Pointered target, TagResolver placeholders);

    // =========================================================================
    // trade.common.failure
    // =========================================================================

    @Key("qsh_dialog.trade.common.failure.shop_unavailable")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-cannot-trade-when-freezing>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-cannot-trade-when-freezing>")
    Component tradeCommonFailureShopUnavailable(Pointered target);

    @Key("qsh_dialog.trade.common.failure.shop_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>Trade failed. The shop could not be found.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>取引に失敗しました。対象のショップが見つかりません。")
    Component tradeCommonFailureShopNotFound(Pointered target);

    @Key("qsh_dialog.trade.common.failure.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block found in your line of sight.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>取引に失敗しました。視線の先にブロックが見つかりませんでした。")
    Component tradeCommonFailureNoTargetBlock(Pointered target);

    @Key("qsh_dialog.trade.common.failure.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>The target block is not a container or wall sign.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>取引に失敗しました。対象のブロックはコンテナまたは壁面看板ではありません。")
    Component tradeCommonFailureInvalidBlock(Pointered target);
}
