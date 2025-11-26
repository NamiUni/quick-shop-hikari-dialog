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
import com.ghostchu.quickshop.api.shop.Shop;
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.configurations.PrimaryConfiguration;
import io.github.namiuni.qshdialog.shop.ShopMode;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.utility.QSHDialogPlaceholders;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ProductPurchaseDialogFactory {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;

    public ProductPurchaseDialogFactory(final ConfigurationHolder<PrimaryConfiguration> configHolder) {
        this.configHolder = configHolder;
    }

    public Result<Dialog, Component> create(final QSHUser customer, final Shop shop) {
        if (ShopMode.valueOf(shop.shopType().identifier()) == ShopMode.BUYING) {
            throw new IllegalArgumentException("Invalid shop componentType: %s".formatted(shop.shopType().identifier()));
        }

        final TagResolver shopPlaceholders = QSHDialogPlaceholders.shopPlaceholders(shop);
        final Result<List<? extends DialogInput>, Component> inputsResult = this.inputs(customer, shop, shopPlaceholders);
        return switch (inputsResult) {
            case Result.Success<List<? extends DialogInput>, Component>(List<? extends DialogInput> inputs) -> {
                final Component title = this.title(customer, shopPlaceholders);
                final DialogBase dialogBase = DialogBase.builder(title)
                        .body(this.body(customer, shop, shopPlaceholders))
                        .inputs(inputs)
                        .build();

                final Dialog dialog = Dialog.create(builder -> builder
                        .empty()
                        .base(dialogBase)
                        .type(this.dialogType(customer, shop, shopPlaceholders))
                );

                yield Result.success(dialog);
            }
            case Result.Error<List<? extends DialogInput>, Component>(Component errorMessage) -> Result.error(errorMessage);
        };
    }

    private Component title(final QSHUser customer, final TagResolver shopTags) {
        return TranslationMessages.tradeBuyTitle(customer, shopTags);
    }

    private List<? extends DialogBody> body(final QSHUser customer, final Shop shop, final TagResolver shopTags) {
        final List<DialogBody> body = new ArrayList<>();

        body.add(DialogBodies.shopName(customer, shop, shopTags));

        if (QuickShop.getInstance().getConfig().getBoolean("shop.allow-stacks")) {
            final DialogBody bundleSize = DialogBodies.productSize(customer, shop, shopTags);
            body.add(bundleSize);
        }

        body.add(DialogBodies.price(customer, shopTags));

        final Component description = TranslationMessages.tradeBodyBuyDescription(customer, shopTags);
        body.add(DialogBody.plainMessage(description));

        return body;
    }

    private Result<List<? extends DialogInput>, Component> inputs(final QSHUser customer, final Shop shop, final TagResolver shopTags) {
        final Result<DialogInput, Component> result = DialogInputs.purchaseQuantity(customer, shop, shopTags);
        return switch (result) {
            case Result.Success<DialogInput, Component>(DialogInput quantity) -> Result.success(List.of(quantity));
            case Result.Error<DialogInput, Component>(Component errorMessage) -> Result.error(errorMessage);
        };
    }

    private DialogType dialogType(final QSHUser customer, final Shop shop, final TagResolver shopTags) {
        final ActionButton buyButton = ActionButton
                .builder(TranslationMessages.tradeBuyConfirmationBuy(customer, shopTags))
                .action(this.buyCallback(customer, shop))
                .build();

        final ActionButton cancelButton = ActionButton
                .builder(TranslationMessages.tradeBuyConfirmationCancel(customer, shopTags))
                .build();

        return DialogType.confirmation(buyButton, cancelButton);
    }

    private DialogAction.CustomClickAction buyCallback(final QSHUser customer, final Shop shop) {

        final ClickCallback.Options options = ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();

        return DialogAction.customClick((response, audience) -> {
            final int quantity = Objects.requireNonNull(response.getFloat("trade_quantity")).intValue();
            QuickShopUtil.buyItem(customer, shop, quantity);
        }, options);
    }
}
