package io.github.namiuni.qshdialog.minecraft.paper.service;

import org.jspecify.annotations.NullMarked;

/**
 * 取引可能数の計算が失敗した理由。
 */
@NullMarked
public enum TradeQuantityFailure {

    /** 購入者のインベントリが満杯 */
    CUSTOMER_INVENTORY_FULL,

    /** ショップの在庫が不足している */
    SHOP_OUT_OF_STOCK,

    /** 購入者の残高が不足している */
    CUSTOMER_INSUFFICIENT_FUNDS,

    /** ショップのインベントリが満杯 */
    SHOP_INVENTORY_FULL,

    /** ショップオーナーの残高が不足している */
    SHOP_INSUFFICIENT_FUNDS,

    /** 売却者が対象アイテムを持っていない */
    CUSTOMER_INSUFFICIENT_ITEMS,
}
