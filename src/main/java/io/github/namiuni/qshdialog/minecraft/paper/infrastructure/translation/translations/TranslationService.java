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

    @Key("qsh_dialog.config.reload.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Configuration reloaded successfully.")
    @Message(locale = Locales.JA_JP, content = "<jis_green>設定の再読み込みに成功しました。")
    Component configurationReloadSuccess(Pointered target);

    @Key("qsh_dialog.config.reload.failure")
    @Message(locale = Locales.ROOT, content = "<jis_red>Failed to reload configuration. See the console for details.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>設定の再読み込みに失敗しました。詳細はコンソールを確認してください。")
    Component configurationReloadFailure(Pointered target);

    @Key("qsh_dialog.translation.reload.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Translations reloaded successfully.")
    @Message(locale = Locales.JA_JP, content = "<jis_green>翻訳の再読み込みに成功しました。")
    Component translationReloadSuccess(Pointered target);

    @Key("qsh_dialog.translation.reload.failure")
    @Message(locale = Locales.ROOT, content = "<jis_red>Failed to reload translations. See the console for details.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>翻訳の再読み込みに失敗しました。詳細はコンソールを確認してください。")
    Component translationReloadFailure(Pointered target);

    @Key("qsh_dialog.command.shop.failure.no_target_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>No block found in your line of sight.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>視線の先にブロックが見つかりませんでした。")
    Component shopCommandNoTargetBlock(Pointered target);

    @Key("qsh_dialog.command.shop.failure.invalid_block")
    @Message(locale = Locales.ROOT, content = "<jis_red>The target block is not a container or wall sign.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>対象のブロックはコンテナまたは壁面看板ではありません。")
    Component shopCommandInvalidBlock(Pointered target);

    @Key("qsh_dialog.command.shop.creation.failure.already_exists")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-already-owned>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-already-owned>")
    Component shopCreationCommandAlreadyExists(Pointered target);

    @Key("qsh_dialog.command.shop.modification.failure.shop_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>No shop was found at that location.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>その場所にショップは見つかりませんでした。")
    Component shopModificationCommandShopNotFound(Pointered target);

    // -------------------------------------------------------------------------
    // Shop creation dialog
    // -------------------------------------------------------------------------

    @Key("qsh_dialog.dialog.shop.creation.title")
    @Message(locale = Locales.ROOT, content = "Shop Creation")
    @Message(locale = Locales.JA_JP, content = "ショップ作成")
    Component shopCreationTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.shop.creation.description")
    @Message(locale = Locales.ROOT, content = "Configure your new shop below, then click <b><jis_green>Create</jis_green></b> to finish. (Cost: <user_cost:shop_create_formatted>)")
    @Message(locale = Locales.JA_JP, content = "ショップを作成します。以下の設定を確認・変更したら、<b><jis_green>作成</jis_green></b>をクリックして完了してください。 (費用: <user_cost:shop_create_formatted>)")
    Component shopCreationDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.button.create")
    @Message(locale = Locales.ROOT, content = "Create")
    @Message(locale = Locales.JA_JP, content = "作成")
    Component shopCreationConfirmButton(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "Cancel")
    @Message(locale = Locales.JA_JP, content = "キャンセル")
    Component shopCreationCancelButton(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.shop.modification.title")
    @Message(locale = Locales.ROOT, content = "Shop Modification")
    @Message(locale = Locales.JA_JP, content = "ショップ編集")
    Component shopModificationTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.shop.modification.description")
    @Message(locale = Locales.ROOT, content = "Adjust your shop settings below, then click <b><jis_green>Apply</jis_green></b> to finish.")
    @Message(locale = Locales.JA_JP, content = "ショップを編集します。以下の設定を確認・変更したら、<b><jis_green>適用</jis_green></b>をクリックして完了してください。")
    Component shopModificationDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.button.apply")
    @Message(locale = Locales.ROOT, content = "Apply")
    @Message(locale = Locales.JA_JP, content = "適用")
    Component shopModificationConfirmButton(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "Cancel")
    @Message(locale = Locales.JA_JP, content = "キャンセル")
    Component shopModificationCancelButton(Pointered target, TagResolver placeholders);

    // -------------------------------------------------------------------------
    // Trade purchase dialog
    // -------------------------------------------------------------------------

    @Key("qsh_dialog.dialog.trade.purchase.title")
    @Message(locale = Locales.ROOT, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    @Message(locale = Locales.JA_JP, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    Component tradePurchaseTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.trade.purchase.description")
    @Message(locale = Locales.ROOT, content = "Enter the quantity, then click <b><jis_green>Buy</jis_green></b> to complete your purchase.")
    @Message(locale = Locales.JA_JP, content = "数量を入力したら、<b><jis_green>購入</jis_green></b>をクリックして注文を確定してください。")
    Component tradePurchaseDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.button.buy")
    @Message(locale = Locales.ROOT, content = "Buy")
    @Message(locale = Locales.JA_JP, content = "購入")
    Component tradePurchaseConfirmButton(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "Cancel")
    @Message(locale = Locales.JA_JP, content = "キャンセル")
    Component tradePurchaseCancelButton(Pointered target, TagResolver placeholders);

    // -------------------------------------------------------------------------
    // Trade sell dialog
    // -------------------------------------------------------------------------

    @Key("qsh_dialog.dialog.trade.sell.title")
    @Message(locale = Locales.ROOT, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    @Message(locale = Locales.JA_JP, content = "<head:'<shop_owner_name>'> <shop_name_or_default>")
    Component tradeSellTitle(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.trade.sell.description")
    @Message(locale = Locales.ROOT, content = "Enter the quantity, then click <b><jis_green>Sell</jis_green></b> to complete your sale.")
    @Message(locale = Locales.JA_JP, content = "数量を入力したら、<b><jis_green>売却</jis_green></b>をクリックして取引を確定してください。")
    Component tradeSellDescription(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.button.sell")
    @Message(locale = Locales.ROOT, content = "Sell")
    @Message(locale = Locales.JA_JP, content = "売却")
    Component tradeSellConfirmButton(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.button.cancel")
    @Message(locale = Locales.ROOT, content = "Cancel")
    @Message(locale = Locales.JA_JP, content = "キャンセル")
    Component tradeSellCancelButton(Pointered target, TagResolver placeholders);

    // -------------------------------------------------------------------------
    // Input labels
    // -------------------------------------------------------------------------

    @Key("qsh_dialog.dialog.input.shop.name")
    @Message(locale = Locales.ROOT, content = "Shop Name")
    @Message(locale = Locales.JA_JP, content = "ショップ名")
    Component inputLabelShopName(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.shop.trade_type")
    @Message(locale = Locales.ROOT, content = "Trade Type")
    @Message(locale = Locales.JA_JP, content = "取引種別")
    Component inputLabelShopTradeType(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.shop.available")
    @Message(locale = Locales.ROOT, content = "Available")
    @Message(locale = Locales.JA_JP, content = "営業中")
    Component inputLabelShopStatus(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.shop.display_visible")
    @Message(locale = Locales.ROOT, content = "Display Item")
    @Message(locale = Locales.JA_JP, content = "アイテム展示")
    Component inputLabelShopDisplay(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.shop.currency")
    @Message(locale = Locales.ROOT, content = "Currency")
    @Message(locale = Locales.JA_JP, content = "通貨")
    Component inputLabelShopCurrency(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.shop.infinite_stock")
    @Message(locale = Locales.ROOT, content = "Infinite Stock")
    @Message(locale = Locales.JA_JP, content = "在庫無制限")
    Component inputLabelShopInfiniteStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.product.price")
    @Message(locale = Locales.ROOT, content = "Price (Range: <price:'<shop_product_key>':min> - <price:'<shop_product_key>':max>)")
    @Message(locale = Locales.JA_JP, content = "価格 (範囲: <price:'<shop_product_key>':min> - <price:'<shop_product_key>':max>)")
    Component inputLabelProductPrice(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.product.quantity")
    @Message(locale = Locales.ROOT, content = "Units per Transaction")
    @Message(locale = Locales.JA_JP, content = "1回の取引量")
    Component inputLabelProductQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.product.quantity.format")
    @Message(locale = Locales.ROOT, content = "%s: %s items")
    @Message(locale = Locales.JA_JP, content = "%s: %s 個")
    String inputFormatProductQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.trade.quantity")
    @Message(locale = Locales.ROOT, content = "Quantity")
    @Message(locale = Locales.JA_JP, content = "数量")
    Component inputLabelTradeQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.dialog.input.trade.quantity.format")
    @Message(locale = Locales.ROOT, content = "%s: %s")
    @Message(locale = Locales.JA_JP, content = "%s: %s")
    String inputFormatTradeQuantity(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade_type.selling")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.selling>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-type.selling>")
    Component tradeTypeSelling(Pointered target);

    @Key("qsh_dialog.trade_type.buying")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-type.buying>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-type.buying>")
    Component tradeTypeBuying(Pointered target);

    // -------------------------------------------------------------------------
    // Shop creation - result
    // -------------------------------------------------------------------------

    @Key("qsh_dialog.shop.creation.success")
    @Message(locale = Locales.ROOT, content = "<quickshop:success-created-shop> <gray>(Cost: <total_cost_formatted>)</gray>\\n<green>You placed <user_shops>/<user_shops_limit> shops.")
    @Message(locale = Locales.JA_JP, content = "<quickshop:success-created-shop> <gray>(Cost: <total_cost_formatted>)</gray>\\n<green><user_shops>/<user_shops_limit>店舗設置しました。")
    Component shopCreationSuccess(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.failure.insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop creation failed. You need <total_cost_formatted> to create this shop.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。作成には <total_cost_formatted> が必要です。")
    Component shopCreationFailedInsufficientFunds(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.failure.price_out_of_range")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop creation failed. The price must be between <price:'<shop_product_key>':min> and <price:'<shop_product_key>':max>.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。価格は <item:min_price> ～ <item:max_price> の範囲で設定してください。")
    Component shopCreationFailedPriceOutOfRange(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.failure.container_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop creation failed. No container was found at the target location.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。対象の場所にコンテナが見つかりませんでした。")
    Component shopCreationFailedContainerNotFound(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.creation.failure.limit_reached")
    @Message(locale = Locales.ROOT, content = "<jis_red>You have reached your shop limit and cannot create any more shops.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成上限に達しているため、これ以上ショップを作成できません。")
    Component shopCreationLimitReached(Pointered target);

    @Key("qsh_dialog.shop.creation.failure.price_invalid")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop creation failed. \"<input>\" is not a valid price.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの作成に失敗しました。\"<input>\" は有効な価格ではありません。")
    Component shopCreationFailedPriceInvalid(Pointered target, String input);

    // -------------------------------------------------------------------------
    // Shop modification - result
    // -------------------------------------------------------------------------

    @Key("qsh_dialog.shop.modification.success")
    @Message(locale = Locales.ROOT, content = "<jis_green>Your shop has been updated successfully. <gray>(Cost: <total_cost_formatted>)")
    @Message(locale = Locales.JA_JP, content = "<jis_green>ショップの設定を更新しました。 <gray>(Cost: <total_cost_formatted>)")
    Component shopModificationSuccess(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.failure.insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop modification failed. You need <total_cost_formatted> to apply this change.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。変更を適用するには <total_cost_formatted> 必要です。")
    Component shopModificationFailedInsufficientFunds(Pointered target, BigDecimal totalCost, String totalCostFormatted, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.failure.price_out_of_range")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop modification failed. The price must be between <price:'<shop_product_key>':min> and <price:'<shop_product_key>':max>.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。価格は <price:'<shop_product_key>':min> ～ <price:'<shop_product_key>':max> の範囲で設定してください。")
    Component shopModificationFailedPriceOutOfRange(Pointered target, BigDecimal input, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.failure.shop_not_found")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop modification failed. The shop could not be found.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。対象のショップが見つかりません。")
    Component shopModificationFailedShopNotFound(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.shop.modification.failure.price_invalid")
    @Message(locale = Locales.ROOT, content = "<jis_red>Shop modification failed. \"<input>\" is not a valid price.")
    @Message(locale = Locales.JA_JP, content = "<jis_red>ショップの編集に失敗しました。\"<input>\" は有効な価格ではありません。")
    Component shopModificationFailedPriceInvalid(Pointered target, String input, TagResolver placeholders);

    // -------------------------------------------------------------------------
    // Trade errors
    // -------------------------------------------------------------------------

    @Key("qsh_dialog.trade.error.shop_unavailable")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-cannot-trade-when-freezing>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-cannot-trade-when-freezing>")
    Component tradeErrorShopUnavailable(Pointered target);

    @Key("qsh_dialog.trade.error.shop_out_of_stock")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-stock-too-low:'<shop_stock>':'<shop_product_display_name>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-stock-too-low:'<shop_stock>':'<shop_product_display_name>'>")
    Component tradeErrorShopOutOfStock(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.error.shop_inventory_full")
    @Message(locale = Locales.ROOT, content = "<quickshop:shop-has-no-space:'<shop_space>':'<shop_product_display_name>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:shop-has-no-space:'<shop_space>':'<shop_product_display_name>'>")
    Component tradeErrorShopInventoryFull(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.error.customer_inventory_full")
    @Message(locale = Locales.ROOT, content = "<quickshop:not-enough-space:'<shop_space>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:not-enough-space:'<shop_space>'>")
    Component tradeErrorCustomerInventoryFull(Pointered target, int actualSpace, TagResolver placeholders);

    @Key("qsh_dialog.trade.error.customer_insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<quickshop:you-cant-afford-to-buy:'<shop_price_formatted>':'<user_balance_formatted>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:you-cant-afford-to-buy:'<shop_price_formatted>':'<user_balance_formatted>'>")
    Component tradeErrorCustomerInsufficientFunds(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.error.shop_insufficient_funds")
    @Message(locale = Locales.ROOT, content = "<quickshop:the-owner-cant-afford-to-buy-from-you:'<shop_price_formatted>':'<shop_owner_balance_formatted>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:the-owner-cant-afford-to-buy-from-you:'<shop_price_formatted>':'<shop_owner_balance_formatted>'>")
    Component tradeErrorShopInsufficientFunds(Pointered target, TagResolver placeholders);

    @Key("qsh_dialog.trade.error.customer_insufficient_items")
    @Message(locale = Locales.ROOT, content = "<quickshop:you-dont-have-that-many-items:0:'<shop_product_display_name>'>")
    @Message(locale = Locales.JA_JP, content = "<quickshop:you-dont-have-that-many-items:0:'<shop_product_display_name>'>")
    Component tradeErrorCustomerInsufficientItems(Pointered target, TagResolver placeholders);
}
