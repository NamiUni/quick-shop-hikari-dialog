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
import io.github.namiuni.qshdialog.minecraft.paper.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.ShopInputType;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.ShopInputs;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSConfigurations;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPermissions;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.adapter.EconomyFormatter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopBlock;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.ShopComponent;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.TradeType;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model.UserSession;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopFailure;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopService;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopSuccess;
import io.github.namiuni.qshdialog.minecraft.paper.translation.Translations;
import io.github.namiuni.qshdialog.minecraft.paper.utilities.ShopTagMapper;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Entity;
import org.bukkit.generator.WorldInfo;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopCreationDialog {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;
    private final Translations translations;
    private final ShopService shopService;
    private final ShopInputs shopInputs;
    private final ShopTagMapper shopTagMapper;

    public ShopCreationDialog(
            final ConfigurationHolder<PrimaryConfiguration> configHolder,
            final Translations translations,
            final ShopService shopService,
            final ShopInputs shopInputs,
            final ShopTagMapper shopTagMapper
    ) {
        this.configHolder = configHolder;
        this.translations = translations;
        this.shopService = shopService;
        this.shopInputs = shopInputs;
        this.shopTagMapper = shopTagMapper;
    }

    public DialogLike createDialog(final UserSession user, final ShopBlock shop) {
        final BigDecimal userBalance = user.balance(shop.container().getWorld().getName(), shop.component().currency());
        final BigDecimal createCost = QSConfigurations.shopCreateCost();
        final BigDecimal namingCost = QSConfigurations.shopNamingCost();
        final String world = user.bukkit()
                .map(Entity::getWorld)
                .map(WorldInfo::getName)
                .orElse("world");
        final TagResolver placeholders = TagResolver.builder()
                .resolver(this.shopTagMapper.shopPlaceholders(user, shop))
                .resolver(ShopTagMapper.pricePlaceholders())
                .resolver(ShopTagMapper.quickshopPlaceholders())
                .resolver(Formatter.number("user_balance", userBalance))
                .resolver(Placeholder.parsed("user_balance_formatted", EconomyFormatter.format(userBalance, shop.container().getWorld().getName())))
                .resolver(Formatter.number("create_cost", createCost))
                .resolver(Placeholder.parsed("create_cost_formatted", EconomyFormatter.format(createCost, world)))
                .resolver(Formatter.number("naming_cost", namingCost))
                .resolver(Placeholder.parsed("naming_cost_formatted", EconomyFormatter.format(namingCost, world)))
                .build();

        return Dialog.create(db -> db.empty()
                .base(this.createBase(user, shop, placeholders))
                .type(this.createType(user, shop, placeholders)));
    }

    private DialogBase createBase(final UserSession user, final ShopBlock shop, final TagResolver placeholders) {
        final ShopComponent shopComponent = shop.component();
        final ShopInputs.Builder inputBuilder = this.shopInputs.target(user, placeholders);

        for (final ShopInputType inputType : this.configHolder.getConfig().creationDialogInputs()) {
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
                    if (QSConfigurations.supportsMultiCurrency()) {
                        if (user.hasPermission(QSPermissions.SHOP_CURRENCY)) {
                            inputBuilder.currency(shopComponent.currency());
                        }
                    }
                }
                case PRODUCT_QUANTITY -> {
                    if (QSConfigurations.supportsBulkTransaction()) {
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
            final ShopBlock shop,
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
                inputComponent = DialogResponseParser.parse(response, shop.component());
            } catch (final InvalidPriceException e) {
                final Component message = this.translations.shopCreationFailedPriceInvalid(user, placeholders, e.rawInput());
                user.sendMessage(message);
                return;
            }

            final Result<ShopSuccess, Set<ShopFailure>> result = this.shopService.createShop(user, shop.withComponent(inputComponent));
            switch (result) {
                case Result.Success(ShopSuccess success) -> {
                    // TODO: ショップの作成内容と支払いコストをDialogType.notice()を使って通知
                    final Component message = this.translations.shopCreationSuccess(user, placeholders, success);
                    user.sendMessage(message);
                }
                case Result.Error(Set<ShopFailure> errors) -> {
                    for (final ShopFailure failure : errors) {
                        switch (failure) {
                            case ShopFailure.ContainerNotFound it -> {
                                final Component message = this.translations.shopCreationFailedContainerNotFound(user, placeholders, it);
                                user.sendMessage(message);
                            }
                            case ShopFailure.OperatorInsufficientFunds it -> {
                                final Component message = this.translations.shopCreationFailedInsufficientFunds(user, placeholders, it);
                                user.sendMessage(message);
                            }
                            case ShopFailure.PriceOutOfRange it -> {
                                final Component message = this.translations.shopCreationFailedPriceOutOfRange(user, placeholders, it);
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
