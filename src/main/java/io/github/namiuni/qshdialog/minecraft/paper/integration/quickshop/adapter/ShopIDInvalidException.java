package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ShopIDInvalidException extends RuntimeException {
    public ShopIDInvalidException(final String message) {
        super(message);
    }
}
