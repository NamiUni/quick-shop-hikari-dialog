package io.github.namiuni.qshdialog.minecraft.paper.dialog;

import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.TradeType;
import io.papermc.paper.dialog.DialogResponseView;
import java.math.BigDecimal;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DialogResponseParser {

    private DialogResponseParser() {
    }

    public static ShopComponent parse(final DialogResponseView response, final ShopComponent current) {
        final ShopComponent.Builder builder = current.toBuilder();
        final String rawPrice = response.getText(DialogInputKeys.SHOP_PRICE);
        if (rawPrice != null) {
            try {
                builder.price(new BigDecimal(rawPrice));
            } catch (final NumberFormatException e) {
                // TODO: throw 検査例外
            }
        }

        Optional.ofNullable(response.getFloat(DialogInputKeys.SHOP_QUANTITY))
                .map(Float::intValue)
                .ifPresent(i -> builder.product(current.product().asQuantity(i)));

        Optional.ofNullable(response.getText(DialogInputKeys.SHOP_TRADE_TYPE))
                .map(TradeType::valueOf)
                .ifPresent(builder::tradeType);

        Optional.ofNullable(response.getBoolean(DialogInputKeys.SHOP_AVAILABLE))
                .ifPresent(builder::available);

        Optional.ofNullable(response.getBoolean(DialogInputKeys.SHOP_DISPLAY_VISIBLE))
                .ifPresent(builder::displayVisible);

        Optional.ofNullable(response.getBoolean(DialogInputKeys.SHOP_INFINITE_STOCK))
                .ifPresent(builder::infiniteStock);

        Optional.ofNullable(response.getText(DialogInputKeys.SHOP_NAME))
                .ifPresent(builder::name);

        Optional.ofNullable(response.getText(DialogInputKeys.SHOP_CURRENCY))
                .ifPresent(builder::currency);

        return builder.build();
    }
}
