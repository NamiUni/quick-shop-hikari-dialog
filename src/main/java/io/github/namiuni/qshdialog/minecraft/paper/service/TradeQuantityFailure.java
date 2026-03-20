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
