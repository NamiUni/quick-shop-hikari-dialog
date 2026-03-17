package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QSPermissions {

    // General
    public static final String USE = "quickshop.use";

    // Trade type
    public static final String SHOP_TRADE_TYPE_SELLING = "quickshop.create.sell";
    public static final String SHOP_TRADE_TYPE_BUYING = "quickshop.create.buy";
    public static final String SHOP_TRADE_TYPE_SELLING_OTHER = "quickshop.other.sell";
    public static final String SHOP_TRADE_TYPE_BUYING_OTHER = "quickshop.other.buy";

    // Availability
    public static final String SHOP_TOGGLE_STATUS = "quickshop.togglefreeze";
    public static final String SHOP_TOGGLE_STATUS_OTHER = "quickshop.other.freeze";

    // Display
    public static final String SHOP_TOGGLE_DISPLAY = "quickshop.toggledisplay";
    public static final String SHOP_TOGGLE_DISPLAY_OTHER = "quickshop.other.toggledisplay";

    // Product quantity
    public static final String SHOP_PRODUCT_QUANTITY = "quickshop.create.stacks";
    // public static final String SHOP_PRODUCT_QUANTITY_OTHER = "quickshop.other.stacks";

    // Naming
    public static final String SHOP_NAMING = "quickshop.shopnaming";
    public static final String SHOP_NAMING_OTHER = "quickshop.other.shopnaming";
    public static final String SHOP_NAMING_BYPASS_COST = "quickshop.bypass.namefee";

    // Currency
    public static final String SHOP_CURRENCY = "quickshop.currency";
    public static final String SHOP_CURRENCY_OTHER = "quickshop.other.currency";

    // Infinite stock
    public static final String SHOP_INFINITE_STOCK = "quickshop.unlimited";

    // Price
    public static final String SHOP_PRICE = "quickshop.create.changeprice";
    public static final String SHOP_PRICE_OTHER = "quickshop.other.price";

    private QSPermissions() {
    }
}
