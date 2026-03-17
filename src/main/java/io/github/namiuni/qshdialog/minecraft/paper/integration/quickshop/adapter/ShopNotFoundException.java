package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ShopNotFoundException extends Exception {
    public ShopNotFoundException(final String message) {
        super(message);
    }
}
