package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model;

import org.jspecify.annotations.NullMarked;

/**
 * ショップに対するユーザーのアクセスレベル。
 *
 * <p>QS の {@code BuiltInShopPermissionGroup} と1対1で対応する。
 * QS との変換は {@link io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.ShopConverter} が担う。
 */
@NullMarked
public enum AccessLevel {
    ADMINISTRATOR,
    STAFF,
    EVERYONE,
    BLOCKED
}
