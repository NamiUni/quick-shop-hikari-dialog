package io.github.namiuni.qshdialog.minecraft.paper.service;

import io.github.namiuni.qshdialog.common.utilities.NumberRange;
import java.math.BigDecimal;
import org.jspecify.annotations.NullMarked;

@NullMarked
public sealed interface ShopFailure permits ShopFailure.ContainerNotFound, ShopFailure.OperatorInsufficientFunds, ShopFailure.PriceOutOfRange, ShopFailure.ShopNotFound {

    record ContainerNotFound() implements ShopFailure {
    }

    record OperatorInsufficientFunds(BigDecimal totalCost) implements ShopFailure {
    }

    record PriceOutOfRange(NumberRange<BigDecimal> priceLimit) implements ShopFailure {
    }

    record ShopNotFound() implements ShopFailure {
    }
}
