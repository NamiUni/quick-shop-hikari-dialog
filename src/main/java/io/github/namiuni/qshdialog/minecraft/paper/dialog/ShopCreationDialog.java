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
public final class ShopCreationDialog {

    private final Provider<PrimaryConfiguration> primaryConfig;
    private final TranslationService translations;
    private final ShopService shopService;
    private final ShopInputs shopInputs;
    private final QSPlaceholders qsPlaceholders;
    private final EconomyService economyService;
    private final EconomyFormatter economyFormatter;
    private final QSConfiguration qsConfig;

    @Inject
    ShopCreationDialog(
            final Provider<PrimaryConfiguration> primaryConfig,
            final TranslationService translations,
            final ShopService shopService,
            final ShopInputs shopInputs,
            final QSPlaceholders qsPlaceholders,
            final EconomyService economyService,
            final EconomyFormatter economyFormatter,
            final QSConfiguration qsConfig
    ) {
        this.primaryConfig = primaryConfig;
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

        return Dialog.create(builder -> builder.empty()
                .base(this.createBase(user, shop, placeholders))
                .type(this.createType(user, shop, placeholders)));
    }

    private DialogBase createBase(final UserSession user, final ShopBlock shop, final TagResolver placeholders) {
        final ShopComponent shopComponent = shop.component();
        final ShopInputs.Builder inputBuilder = this.shopInputs.target(user, placeholders);

        for (final ShopInputType inputType : this.primaryConfig.get().creationDialogInputs()) {
            switch (inputType) {
                case NAME -> {
                    if (user.hasPermission(QSPermissions.SHOP_NAMING)) {
                        inputBuilder.name(shopComponent.name());
                    }
                }
                case TRADE_TYPE -> {
                    final List<TradeType> modes = new ArrayList<>();
                    if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_SELLING))
                        modes.add(TradeType.SELLING);
                    if (user.hasPermission(QSPermissions.SHOP_TRADE_TYPE_BUYING))
                        modes.add(TradeType.BUYING);
                    if (!modes.isEmpty()) {
                        inputBuilder.tradeType(modes, shopComponent.tradeType());
                    }
                }
                case CURRENCY -> {
                    final String currency = shop.component().currency();
                    if (currency != null && this.economyService.supportsMultiCurrency()) {
                        if (user.hasPermission(QSPermissions.SHOP_CURRENCY)) {
                            inputBuilder.currency(shopComponent.currency());
                        }
                    }
                }
                case PRODUCT_QUANTITY -> {
                    if (this.qsConfig.supportsUnitTransaction()) {
                        if (user.hasPermission(QSPermissions.SHOP_PRODUCT_QUANTITY)) {
                            final int maxStackSize = Objects.requireNonNullElse(
                                    shopComponent.product().getData(DataComponentTypes.MAX_STACK_SIZE),
                                    shopComponent.product().getMaxStackSize()
                            );
                            inputBuilder.quantity(maxStackSize, shopComponent.product().getAmount());
                        }
                    }
                }
                case PRICE -> {
                    if (user.hasPermission(QSPermissions.SHOP_PRICE)) {
                        inputBuilder.price(shopComponent.price());
                    }
                }
                case STATUS -> {
                    if (user.hasPermission(QSPermissions.SHOP_TOGGLE_STATUS)) {
                        inputBuilder.status(shopComponent.available());
                    }
                }
                case DISPLAY -> {
                    if (user.hasPermission(QSPermissions.SHOP_TOGGLE_DISPLAY)) {
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

        final Component title = this.translations.shopCreationTitle(user, placeholders);
        final DialogBody body = DialogBody.item(shopComponent.product().asOne())
                .description(DialogBody.plainMessage(this.translations.shopCreationDescription(user, placeholders)))
                .build();
        return DialogBase.builder(title)
                .body(List.of(body))
                .inputs(inputBuilder.buildInputs())
                .build();
    }

    private DialogType createType(
            final UserSession user,
            final ShopBlock preparingShop,
            final TagResolver placeholders
    ) {
        final var callbackOptions = ClickCallback.Options.builder()
                .uses(1)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build();

        // TODO: ラベルにエラー理由を追記したダイアログの再生成
        final DialogActionCallback callback = ((response, _) -> {
            final ShopComponent inputComponent;
            try {
                inputComponent = DialogResponseParser.parse(response, preparingShop.component());
            } catch (final InvalidPriceException e) {
                final Component message = this.translations.shopCreationFailedPriceInvalid(user, e.rawInput());
                user.sendMessage(message);
                return;
            }

            final ShopBlock shop = preparingShop.withComponent(inputComponent);
            final TagResolver newPlaceholders = TagResolver.builder()
                    .resolver(this.qsPlaceholders.shopPlaceholder(shop))
                    .build();

            final Result<ShopSuccess, Set<ShopFailure>> result = this.shopService.createShop(user, preparingShop.withComponent(inputComponent));
            final String world = shop.container().getWorld().getName();
            switch (result) {
                case Result.Success(ShopSuccess success) -> {
                    // TODO: ショップの作成内容と支払いコストをDialogType.notice()を使って通知
                    final Component message = this.translations.shopCreationSuccess(
                            user,
                            success.paid(),
                            this.economyFormatter.format(success.paid(), world),
                            newPlaceholders
                    );
                    user.sendMessage(message);
                }
                case Result.Error(Set<ShopFailure> errors) -> {
                    for (final ShopFailure failure : errors) {
                        switch (failure) {
                            case ShopFailure.ContainerNotFound _ -> {
                                final Component message = this.translations.shopCreationFailedContainerNotFound(user, newPlaceholders);
                                user.sendMessage(message);
                            }
                            case ShopFailure.OperatorInsufficientFunds it -> {
                                final BigDecimal cost = it.totalCost();
                                final String formatted = this.economyFormatter.format(cost, world);
                                final Component message = this.translations.shopCreationFailedInsufficientFunds(user, cost, formatted, newPlaceholders);
                                user.sendMessage(message);
                            }
                            case ShopFailure.PriceOutOfRange _ -> {
                                final Component message = this.translations.shopCreationFailedPriceOutOfRange(user, newPlaceholders);
                                user.sendMessage(message);
                            }
                            default -> {
                                // ignored
                            }
                        }
                    }
                }
            }
        });

        final var applyButton = ActionButton.builder(this.translations.shopCreationConfirmButton(user, placeholders))
                .action(DialogAction.customClick(callback, callbackOptions))
                .build();
        final var cancelButton = ActionButton.builder(this.translations.shopCreationCancelButton(user, placeholders))
                .build();

        return DialogType.confirmation(applyButton, cancelButton);
    }
}
