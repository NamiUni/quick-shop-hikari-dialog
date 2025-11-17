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
import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.shop.ShopDisplay;
import io.github.namiuni.qshdialog.shop.ShopMode;
import io.github.namiuni.qshdialog.shop.ShopStatus;
import io.github.namiuni.qshdialog.shop.Shops;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.user.QSHUser;
import io.github.namiuni.qshdialog.utility.QuickShopUtil;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopModificationDialogFactory {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;

    public ShopModificationDialogFactory(final ConfigurationHolder<PrimaryConfiguration> configHolder) {
        this.configHolder = configHolder;
    }

    public Result<Dialog, Component> create(final QSHUser editor, final Shop shop) {
        if (!editor.hasPermission("quickshop.use")) {
            return Result.error(Component.empty());
        }

        final TagResolver shopTags = Shops.tagResolver(shop);

        final PriceLimiterCheckResult priceLimit = QuickShop.getInstance().getShopManager().getPriceLimiter()
                .check(editor.quickShopUser(), shop.getItem(), shop.getCurrency(), shop.getPrice());
        final BigDecimal minPrice = BigDecimal.valueOf(priceLimit.getMin());
        final BigDecimal maxPrice = BigDecimal.valueOf(priceLimit.getMax());

        final Result<List<? extends DialogInput>, Component> inputs = this.inputs(editor, shop, shopTags, minPrice, maxPrice);
        return switch (inputs) {
            case Result.Success<List<? extends DialogInput>, Component>(List<? extends DialogInput> result) -> {
                final DialogBase dialogBase = DialogBase
                        .builder(this.title(editor, shopTags))
                        .body(this.body(editor, shop, shopTags))
                        .inputs(result)
                        .build();

                final Dialog dialog = Dialog.create(builder -> builder
                        .empty()
                        .base(dialogBase)
                        .type(this.dialogType(editor, shop, shopTags))
                );

                yield Result.success(dialog);
            }
            case Result.Error<List<? extends DialogInput>, Component>(Component errorMessage) -> Result.error(errorMessage);
        };
    }

    private Component title(final QSHUser editor, final TagResolver shopTags) {
        return TranslationMessages.shopModificationTitle(editor, shopTags);
    }

    private List<? extends DialogBody> body(final QSHUser editor, final Shop shop, final TagResolver shopTags) {
        final DialogBody body = DialogBody.item(shop.getItem().asOne())
                .description(DialogBody.plainMessage(TranslationMessages.shopModificationDescription(editor, shopTags)))
                .build();

        return List.of(body);
    }

    private Result<List<? extends DialogInput>, Component> inputs(final QSHUser editor, final Shop shop, final TagResolver shopTags, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final List<DialogInput> inputs = new ArrayList<>();

        final boolean isOwner = Objects.equals(editor.uuid(), shop.getOwner().getUniqueId());
        final Result<DialogInput, Component> typeInput = DialogInputs.shopMode(editor, shopTags, isOwner, ShopMode.of(shop.shopType()));
        switch (typeInput) {
            case Result.Success<DialogInput, Component>(DialogInput result) -> inputs.add(result);
            case Result.Error<DialogInput, Component>(Component ignored) -> {
                return Result.error(Component.empty());
            }
        }

        if (isOwner && editor.hasPermission("quickshop.togglefreeze") || editor.hasPermission("quickshop.other.freeze")) {
            final DialogInput input = DialogInputs.shopStatus(editor, shopTags, ShopStatus.of(shop.shopType()));
            inputs.add(input);
        }

        if (isOwner && editor.hasPermission("quickshop.toggledisplay") || editor.hasPermission("quickshop.other.toggledisplay")) {
            inputs.add(DialogInputs.shopDisplay(editor, shopTags, ShopDisplay.of(shop)));
        }

        if (QuickShop.getInstance().getConfig().getBoolean("shop.allow-stacks")) {
            if (isOwner && editor.hasPermission("quickshop.create.stacks") || editor.hasPermission("quickshop.other.amount")) {
                final DialogInput input = DialogInputs.productBundleSize(editor, shopTags, shop.getItem().getAmount(), shop.getItem().getMaxStackSize());
                inputs.add(input);
            }
        }

        if (isOwner || editor.hasPermission("quickshop.other.price")) {
            inputs.add(DialogInputs.productPrice(editor, shopTags, BigDecimal.valueOf(shop.getPrice()), minPrice, maxPrice));
        }

        if (isOwner && editor.hasPermission("quickshop.shopnaming") || editor.hasPermission("quickshop.other.shopnaming")) {
            inputs.add(DialogInputs.shopName(editor, shopTags, shop.getShopName()));
        }

        if (QuickShopUtil.supportsMultiCurrency()) {
            if (isOwner && editor.hasPermission("quickshop.currency") || editor.hasPermission("quickshop.other.currency")) {
                inputs.add(DialogInputs.shopCurrency(editor, shopTags, shop.getCurrency()));
            }
        }

        if (editor.hasPermission("quickshop.unlimited")) {
            inputs.add(DialogInputs.shopUnlimitedStock(editor, shopTags, shop.isUnlimited()));
        }

        if (inputs.isEmpty()) {
            return Result.error(Component.empty());
        } else {
            return Result.success(inputs);
        }
    }

    private DialogType dialogType(final QSHUser editor, final Shop shop, final TagResolver shopTags) {
        final DialogActionCallback callback = new ShopModificationCallback(editor, shop, shopTags);

        final ClickCallback.Options options = ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();

        final ActionButton applyBotton = ActionButton
                .builder(TranslationMessages.shopModificationConfirmationApply(editor, shopTags))
                .action(DialogAction.customClick(callback, options))
                .build();

        final ActionButton cancelButton = ActionButton
                .builder(TranslationMessages.shopModificationConfirmationCancel(editor, shopTags))
                .build();

        return DialogType.confirmation(applyBotton, cancelButton);
    }
}
