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
import com.ghostchu.quickshop.api.shop.Shop;
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.shop.ContainerShopBuilder;
import io.github.namiuni.qshdialog.shop.ShopType;
import io.github.namiuni.qshdialog.shop.Shops;
import io.github.namiuni.qshdialog.shop.policy.ShopCreationContext;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopCreationDialogFactory {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;

    public ShopCreationDialogFactory(final ConfigurationHolder<PrimaryConfiguration> configHolder) {
        this.configHolder = configHolder;
    }

    public Dialog create(final ShopCreationContext context) {
        final DialogBase dialogBase = DialogBase.builder(this.title(context))
                .body(this.body(context))
                .inputs(this.inputs(context))
                .build();

        return Dialog.create(builder -> builder
                .empty()
                .base(dialogBase)
                .type(this.dialogType(context))
        );
    }

    private Component title(final ShopCreationContext context) {
        return TranslationMessages.shopCreationTitle(context.owner());
    }

    private List<? extends DialogBody> body(final ShopCreationContext context) {
        final DialogBody body = DialogBody.item(context.product().asOne())
                .description(DialogBody.plainMessage(TranslationMessages.shopCreationDescription(context.owner())))
                .build();

        return List.of(body);
    }

    private List<? extends DialogInput> inputs(final ShopCreationContext context) {
        final List<DialogInput> inputs = new ArrayList<>();

        final List<SingleOptionDialogInput.OptionEntry> types = new ArrayList<>();
        if (context.owner().hasPermission("quickshop.create.sell")) {
            types.add(SingleOptionDialogInput.OptionEntry.create(
                    "SELL",
                    TranslationMessages.shopInputTypeSell(context.owner()),
                    true
            ));
        }
        if (context.owner().hasPermission("quickshop.create.buy")) {
            types.add(SingleOptionDialogInput.OptionEntry.create(
                    "BUY",
                    TranslationMessages.shopInputTypeBuy(context.owner()),
                    false
            ));
        }
        final Component typeLabel = TranslationMessages.shopInputTypeLabel(context.owner());
        final DialogInput shopTypeInput = DialogInput.singleOption("type", typeLabel, types).build();
        inputs.add(shopTypeInput);

        if (context.owner().hasPermission("quickshop.create.stacks")) {
            final Component bundleSizeLabel = TranslationMessages.shopInputBundleSize(context.owner());
            final String format = TranslationMessages.shopInputBundleFormat(context.owner());
            final DialogInput bundleSizeInput = DialogInput.numberRange("bundle_size", bundleSizeLabel, 0.0f, 64.0f)
                    .step(1.0f)
                    .initial((float) context.product().getAmount())
                    .labelFormat(format)
                    .build();
            inputs.add(bundleSizeInput);
        }

        final PriceLimiterCheckResult priceLimit = QuickShop.getInstance().getShopManager().getPriceLimiter()
                .check(context.owner().quickShopUser(), context.product(), null, 1.0);
        final double minPrice = priceLimit.getMin();

        final Component priceLabel = TranslationMessages.shopInputPriceLabel(context.owner()); // TODO: 最低最高平均上限下限
        final DialogInput priceInput = DialogInput.text("price", priceLabel)
                .initial(String.valueOf(minPrice))
                .build();
        inputs.add(priceInput);

        if (context.owner().hasPermission("quickshop.shopnaming")) {
            final Component nameLabel = TranslationMessages.shopInputShopName(context.owner());
            final DialogInput nameInput = DialogInput.text("name", nameLabel).build();
            inputs.add(nameInput);
        }

        if (context.owner().hasPermission("quickshop.currency")) {
            final Component currencyLabel = TranslationMessages.shopInputCurrency(context.owner());
            final DialogInput currencyInput = DialogInput.text("currency", currencyLabel).build();
            inputs.add(currencyInput);
        }

        if (context.owner().hasPermission("quickshop.toggledisplay")) {
            final Component displayLabel = TranslationMessages.shopInputDisableDisplay(context.owner());
            final DialogInput displayInput = DialogInput.bool("display", displayLabel)
                    .initial(true)
                    .build();
            inputs.add(displayInput);
        }

        if (context.owner().hasPermission("quickshop.unlimited")) {
            final Component unlimitedLabel = TranslationMessages.shopInputUnlimited(context.owner());
            final DialogInput unlimitedInput = DialogInput.bool("unlimited", unlimitedLabel)
                    .initial(false)
                    .build();
            inputs.add(unlimitedInput);
        }

        return inputs;
    }

    private DialogType dialogType(final ShopCreationContext context) {
        final DialogActionCallback callback = (response, audience) -> {
            final ContainerShopBuilder builder = Shops.container(context.shopContainer())
                    .owner(context.owner().quickShopUser())
                    .product(context.product());
            Optional.ofNullable(response.getFloat("bundle_size"))
                    .ifPresent(bundleSize -> builder.bundleSize(bundleSize.intValue()));

            Optional.ofNullable(response.getText("price"))
                    .map(BigDecimal::new)
                    .map(BigDecimal::doubleValue)
                    .ifPresentOrElse(
                            builder::price,
                            () -> {
                                final Component message = TranslationMessages.shopCreationErrorEmptyInput(context.owner(), TranslationMessages.shopInputPriceLabel(context.owner()));
                                context.owner().sendMessage(message);
                            });

            Optional.ofNullable(response.getText("type"))
                    .map(ShopType::valueOf)
                    .ifPresent(builder::type);

            builder.name(response.getText("name"));
            builder.currency(response.getText("currency"));

            Optional.ofNullable(response.getBoolean("display"))
                    .ifPresent(builder::display);

            Optional.ofNullable(response.getBoolean("unlimited"))
                    .ifPresent(builder::display);

            final Shop shop = builder.build();
            final Block signLocationBlock = context.shopContainer().getBlock().getRelative(context.shopFace());
            QuickShop.getInstance().getShopManager().createShop(shop, signLocationBlock, false);

        };

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
