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
import io.github.namiuni.qshdialog.shop.TradeType;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopModificationDialogFactory {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;

    public ShopModificationDialogFactory(final ConfigurationHolder<PrimaryConfiguration> configHolder) {
        this.configHolder = configHolder;
    }

    public Dialog create(final Shop shop, final QSHUser qshUser) {
        final PriceLimiterCheckResult priceLimit = QuickShop.getInstance().getShopManager().getPriceLimiter()
                .check(qshUser.quickShopUser(), shop.getItem(), shop.getCurrency(), shop.getPrice());
        final BigDecimal minPrice = BigDecimal.valueOf(priceLimit.getMin());
        final BigDecimal maxPrice = BigDecimal.valueOf(priceLimit.getMax());

        final DialogBase dialogBase = DialogBase.builder(this.title(shop, qshUser))
                .body(this.body(shop, qshUser))
                .inputs(this.inputs(shop, qshUser, minPrice, maxPrice))
                .build();

        return Dialog.create(builder -> builder
                .empty()
                .base(dialogBase)
                .type(this.dialogType(shop, qshUser))
        );
    }

    private Component title(final Shop shop, final QSHUser qshUser) {
        return TranslationMessages.shopModificationTitle(qshUser);
    }

    private List<? extends DialogBody> body(final Shop shop, final QSHUser qshUser) {
        final DialogBody body = DialogBody.item(shop.getItem())
                .description(DialogBody.plainMessage(TranslationMessages.shopModificationDescription(qshUser)))
                .build();

        return List.of(body);
    }

    private List<? extends DialogInput> inputs(final Shop shop, final QSHUser owner, final BigDecimal minPrice, final BigDecimal maxPrice) {
        final List<DialogInput> inputs = new ArrayList<>();

        inputs.add(DialogInputs.tradeType(owner, TradeType.of(shop.shopType())));

        if (owner.hasPermission("quickshop.create.stacks") && QuickShop.getInstance().getConfig().getBoolean("shop.allow-stacks")) {
            final DialogInput input = DialogInputs.productBundleSize(owner, shop.getItem().getAmount(), shop.getItem().getMaxStackSize());
            inputs.add(input);
        }

        inputs.add(DialogInputs.productPrice(owner, BigDecimal.valueOf(shop.getPrice()), minPrice, maxPrice));

        if (owner.hasPermission("quickshop.shopnaming")) {
            inputs.add(DialogInputs.shopName(owner, shop.getShopName()));
        }

        if (owner.hasPermission("quickshop.currency") && QuickShopUtil.supportsMultiCurrency()) {
            inputs.add(DialogInputs.shopCurrency(owner, shop.getCurrency()));
        }

        if (owner.hasPermission("quickshop.toggledisplay")) {
            inputs.add(DialogInputs.shopShowDisplay(owner, !shop.isDisableDisplay()));
        }

        if (owner.hasPermission("quickshop.unlimited")) {
            inputs.add(DialogInputs.shopUnlimitedStock(owner, shop.isUnlimited()));
        }

        return inputs;
    }

    private DialogType dialogType(final Shop shop, final QSHUser qshUser) {
        final DialogActionCallback callback = new ShopModificationCallback(shop, qshUser);

        final ClickCallback.Options options = ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();

        final ActionButton applyBotton = ActionButton
                .builder(TranslationMessages.shopModificationConfirmationApply(qshUser))
                .action(DialogAction.customClick(callback, options))
                .build();

        final ActionButton cancelButton = ActionButton
                .builder(TranslationMessages.shopModificationConfirmationCancel(qshUser))
                .build();

        return DialogType.confirmation(applyBotton, cancelButton);
    }
}
