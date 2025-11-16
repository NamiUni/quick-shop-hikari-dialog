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
package io.github.namiuni.qshdialog.shop.dialog;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.shop.PriceLimiterCheckResult;
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.shop.TradeType;
import io.github.namiuni.qshdialog.shop.policy.ShopCreationContext;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopCreationDialogFactory {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;

    public ShopCreationDialogFactory(final ConfigurationHolder<PrimaryConfiguration> configHolder) {
        this.configHolder = configHolder;
    }

    public Dialog create(final ShopCreationContext context) {
        final PriceLimiterCheckResult priceLimit = QuickShop.getInstance().getShopManager().getPriceLimiter()
                .check(context.owner().quickShopUser(), context.product(), null, 1.0);
        final BigDecimal minPrice = BigDecimal.valueOf(priceLimit.getMin());
        final BigDecimal maxPrice = BigDecimal.valueOf(priceLimit.getMax());

        final DialogBase dialogBase = DialogBase
                .builder(this.title(context))
                .body(this.body(context))
                .inputs(this.inputs(context, minPrice, maxPrice))
                .build();

        return Dialog.create(builder -> builder
                .empty()
                .base(dialogBase)
                .type(this.dialogType(context, minPrice, maxPrice))
        );
    }

    private Component title(final ShopCreationContext context) {
        return TranslationMessages.shopCreationTitle(context.owner());
    }

    private List<? extends DialogBody> body(final ShopCreationContext context) {
        final PlainMessageDialogBody description = DialogBody.plainMessage(TranslationMessages.shopCreationDescription(context.owner()));
        final DialogBody body = DialogBody.item(context.product().asOne())
                .description(description)
                .build();

        return List.of(body);
    }

    private List<? extends DialogInput> inputs(final ShopCreationContext context, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final List<DialogInput> inputs = new ArrayList<>();

        inputs.add(DialogInputs.tradeType(context.owner(), TradeType.SELL));

        if (context.owner().hasPermission("quickshop.create.stacks") && QuickShop.getInstance().getConfig().getBoolean("shop.allow-stacks")) {
            final DialogInput input = DialogInputs.productBundleSize(
                    context.owner(),
                    context.product().getAmount(),
                    context.product().getMaxStackSize()
            );
            inputs.add(input);
        }

        inputs.add(DialogInputs.productPrice(context.owner(), minPrice, minPrice, maxPrice));

        if (context.owner().hasPermission("quickshop.shopnaming")) {
            inputs.add(DialogInputs.shopName(context.owner(), ""));
        }

        if (context.owner().hasPermission("quickshop.currency") && QuickShopUtil.supportsMultiCurrency()) {
            inputs.add(DialogInputs.shopCurrency(context.owner(), ""));
        }

        if (context.owner().hasPermission("quickshop.toggledisplay")) {
            inputs.add(DialogInputs.shopShowDisplay(context.owner(), true));
        }

        if (context.owner().hasPermission("quickshop.unlimited")) {
            inputs.add(DialogInputs.shopUnlimitedStock(context.owner(), false));
        }

        return inputs;
    }

    private DialogType dialogType(final ShopCreationContext context, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final DialogActionCallback callback = new ShopCreationCallback(context, minPrice, maxPrice);

        final ClickCallback.Options options = ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();

        final ActionButton createButton = ActionButton
                .builder(TranslationMessages.shopCreationConfirmationCreate(context.owner()))
                .action(DialogAction.customClick(callback, options))
                .build();

        final ActionButton cancelButton = ActionButton
                .builder(TranslationMessages.shopCreationConfirmationCancel(context.owner()))
                .build();

        return DialogType.confirmation(createButton, cancelButton);
    }
}
