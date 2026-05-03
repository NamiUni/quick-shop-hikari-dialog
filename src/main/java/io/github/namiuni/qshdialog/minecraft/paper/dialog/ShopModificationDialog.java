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
package io.github.namiuni.qshdialog.minecraft.paper.dialog;

import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.configurations.PrimaryConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPlaceholders;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyFormatter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.economy.EconomyService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.permission.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopFailure;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.shop.ShopSuccess;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.trade.TradeType;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopModificationDialog {

    private final Provider<PrimaryConfiguration> configHolder;
    private final TranslationService translations;
    private final ShopService shopService;
    private final ShopInputs shopInputs;
    private final QSPlaceholders qsPlaceholders;
    private final EconomyService economyService;
    private final EconomyFormatter economyFormatter;
    private final QSConfiguration qsConfig;

    @Inject
    ShopModificationDialog(
            final Provider<PrimaryConfiguration> configHolder,
            final TranslationService translations,
            final ShopService shopService,
            final ShopInputs shopInputs,
            final QSPlaceholders qsPlaceholders,
            final EconomyService economyService,
            final EconomyFormatter economyFormatter,
            final QSConfiguration qsConfig
    ) {
        this.configHolder = configHolder;
        this.translations = translations;
        this.shopService = shopService;
        this.shopInputs = shopInputs;
        this.qsPlaceholders = qsPlaceholders;
        this.economyService = economyService;
        this.economyFormatter = economyFormatter;
        this.qsConfig = qsConfig;
    }

    public DialogLike createDialog(final UserSession user, final ShopBlock shop) {
        final TagResolver placeholders = TagResolver.builder()
                .resolver(this.qsPlaceholders.shopPlaceholder(shop))
                .build();

        return Dialog.create(db -> db.empty()
                .base(this.createBase(user, shop, placeholders))
                .type(this.createType(user, shop, placeholders)));
    }

    private DialogBase createBase(final UserSession user, final ShopBlock shop, final TagResolver placeholders) {
        final ShopComponent shopComponent = shop.component();
        final boolean isStaff = shopComponent.isStaff(user.uuid());
        final ShopInputs.Builder inputBuilder = this.shopInputs.forModification(user, placeholders);

        for (final ShopInputType inputType : this.configHolder.get().modificationDialogInputs()) {
            switch (inputType) {
                case NAME -> {
                    if (user.hasPermission(QSPermissions.SHOP_NAMING_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_NAMING)) {
                        inputBuilder.name(shopComponent.name());
                    }
                }
                case TRADE_TYPE -> {
                    final List<TradeType> modes = new ArrayList<>();
                    if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING))
                        modes.add(TradeType.SELLING);
                    if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING))
                        modes.add(TradeType.BUYING);
                    if (!modes.isEmpty()) {
                        inputBuilder.tradeType(modes, shopComponent.tradeType());
                    }
                }
                case CURRENCY -> {
                    final String currency = shop.component().currency();
                    if (currency != null && this.economyService.supportsMultiCurrency()) {
                        if (user.hasPermission(QSPermissions.SHOP_CURRENCY_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_CURRENCY)) {
                            inputBuilder.currency(shopComponent.currency());
                        }
                    }
                }
                case PRODUCT_QUANTITY -> {
                    if (this.qsConfig.supportsUnitTransaction()) {
                        if (isStaff && user.hasPermission(QSPermissions.SHOP_PRODUCT_QUANTITY)) {
                            final int maxStackSize = Objects.requireNonNullElse(
                                    shopComponent.product().getData(DataComponentTypes.MAX_STACK_SIZE),
                                    shopComponent.product().getMaxStackSize()
                            );
                            inputBuilder.quantity(maxStackSize, shopComponent.product().getAmount());
                        }
                    }
                }
                case PRICE -> {
                    if (user.hasPermission(QSPermissions.SHOP_PRICE_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_PRICE)) {
                        inputBuilder.price(shopComponent.price());
                    }
                }
                case STATUS -> {
                    if (user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS)) {
                        inputBuilder.status(shopComponent.available());
                    }
                }
                case DISPLAY -> {
                    if (user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY)) {
                        inputBuilder.display(shopComponent.displayVisible());
                    }
                }
                case STOCK -> {
                    if (user.hasPermission(QSPermissions.SHOP_INFINITE_STOCK)) {
                        inputBuilder.stock(shopComponent.infiniteStock());
                    }
                }
            }
        }

        final Component title = this.translations.dialogModificationTitle(user, placeholders);
        final DialogBody body = DialogBody.item(shopComponent.product().asOne())
                .description(DialogBody.plainMessage(this.translations.dialogModificationDescription(user, placeholders)))
                .build();
        return DialogBase.builder(title)
                .body(List.of(body))
                .inputs(inputBuilder.buildInputs())
                .build();
    }

    private DialogType createType(
            final UserSession user,
            final ShopBlock shop,
            final TagResolver placeholders
    ) {
        final var callbackOptions = ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();

        final DialogActionCallback callback = ((response, _) -> {
            final ShopComponent updatedComponent;
            try {
                updatedComponent = DialogResponseParser.parse(response, shop.component());
            } catch (final InvalidPriceException e) {
                user.sendMessage(this.translations.shopModificationFailedPriceInvalid(user, e.rawInput(), placeholders));
                return;
            }

            final ShopBlock updatedShop = shop.withComponent(updatedComponent);
            final TagResolver newPlaceholders = TagResolver.builder()
                    .resolver(this.qsPlaceholders.shopPlaceholder(updatedShop))
                    .build();
            final Result<ShopSuccess, Set<ShopFailure>> result = this.shopService.updateShop(user, updatedShop);
            final String world = shop.container().getWorld().getName();
            switch (result) {
                case Result.Success(ShopSuccess success) -> user.sendMessage(this.translations.shopModificationSuccess(
                        user,
                        success.paid(),
                        this.economyFormatter.format(success.paid(), world, updatedComponent.currency()),
                        newPlaceholders
                ));
                case Result.Error(Set<ShopFailure> errors) -> {
                    for (final ShopFailure failure : errors) {
                        switch (failure) {
                            case ShopFailure.ShopNotFound _ ->
                                    user.sendMessage(this.translations.shopModificationFailedShopNotFound(user));
                            case ShopFailure.OperatorInsufficientFunds it -> {
                                final BigDecimal cost = it.totalCost();
                                user.sendMessage(this.translations.shopModificationFailedInsufficientFunds(
                                        user, cost, this.economyFormatter.format(cost, world), newPlaceholders));
                            }
                            case ShopFailure.PriceOutOfRange _ ->
                                    user.sendMessage(this.translations.shopModificationFailedPriceOutOfRange(
                                            user, updatedComponent.price(), newPlaceholders));
                            default -> {
                                // ignored
                            }
                        }
                    }
                }
            }
        });

        final var applyButton = ActionButton.builder(this.translations.dialogModificationConfirmButton(user, placeholders))
                .action(DialogAction.customClick(callback, callbackOptions))
                .build();
        final var cancelButton = ActionButton.builder(this.translations.dialogModificationCancelButton(user, placeholders))
                .build();

        return DialogType.confirmation(applyButton, cancelButton);
    }
}
