package io.github.namiuni.qshdialog.minecraft.paper.dialog.elements;

import java.util.Locale;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum ShopInputType {
    NAME("name"),
    TRADE_TYPE("trade_type"),
    CURRENCY("currency"),
    PRODUCT_QUANTITY("product_quantity"),
    PRICE("price"),
    STATUS("status"),
    DISPLAY("display"),
    STOCK("stock");

    private final String name;

    ShopInputType(final String name) {
        this.name = name;
    }

    public static ShopInputType of(final String name) {
        return switch (name.toLowerCase(Locale.ROOT)) {
            case "name" -> NAME;
            case "trade_type" -> TRADE_TYPE;
            case "currency" -> CURRENCY;
            case "product_quantity" -> PRODUCT_QUANTITY;
            case "price" -> PRICE;
            case "status" -> STATUS;
            case "display" -> DISPLAY;
            case "stock" -> STOCK;
            default -> throw new IllegalArgumentException();
        };
    }

    @Override
    public String toString() {
        return this.name;
    }
}
