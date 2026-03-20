package io.github.namiuni.qshdialog.minecraft.paper.dialog;

import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.ShopInputType;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.ShopInputs;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSConfigurations;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPermissions;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ShopModificationDialog {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;
    private final Translations translations;
    private final ShopService shopService;
    private final ShopInputs shopInputs;
    private final ShopTagMapper shopTagMapper;

    public ShopModificationDialog(
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
        final TagResolver placeholders = TagResolver.builder()
                .resolver(this.shopTagMapper.shopPlaceholders(shop))
                .resolver(this.shopTagMapper.itemPlaceholders(shop.component().product()))
                .resolver(ShopTagMapper.quickshopPlaceholders())
                .build();

        return Dialog.create(db -> db.empty()
                .base(this.createBase(user, shop, placeholders))
                .type(this.createType(user, shop, placeholders)));
    }

    private DialogBase createBase(final UserSession user, final ShopBlock shop, final TagResolver placeholders) {
        final ShopComponent shopComponent = shop.component();
        final boolean isStaff = shopComponent.isStaff(user.uuid());
        final ShopInputs.Builder inputBuilder = this.shopInputs.target(user, placeholders);

        for (final ShopInputType inputType : this.configHolder.getConfig().modificationDialogInputs()) {
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
                    if (QSConfigurations.supportsMultiCurrency()) {
                        if (user.hasPermission(QSPermissions.SHOP_CURRENCY_OTHER) || isStaff && user.hasPermission(QSPermissions.SHOP_CURRENCY)) {
                            inputBuilder.currency(shopComponent.currency());
                        }
                    }
                }
                case PRODUCT_QUANTITY -> {
                    if (QSConfigurations.supportsBulkTransaction()) {
                        if (user.bukkit().orElseThrow().isOp() || isStaff && user.hasPermission(QSPermissions.SHOP_PRODUCT_QUANTITY)) {
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

        final Component title = this.translations.shopModificationTitle(user, placeholders);
        final DialogBody body = DialogBody.item(shopComponent.product().asOne())
                .description(DialogBody.plainMessage(this.translations.shopModificationDescription(user, placeholders)))
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
        final DialogActionCallback callback = ((response, audience) -> {
            final ShopComponent updatedComponent;
            try {
                updatedComponent = DialogResponseParser.parse(response, shop.component());
            } catch (final InvalidPriceException e) {
                final Component message = this.translations.shopModificationFailedPriceInvalid(user, placeholders, e.rawInput());
                user.sendMessage(message);
                return;
            }

            final Result<ShopSuccess, Set<ShopFailure>> result = this.shopService.updateShop(user, shop.withComponent(updatedComponent));
            switch (result) {
                case Result.Success(ShopSuccess success) -> {
                    // TODO: ショップの作成内容と支払いコストをDialogType.notice()を使って通知
                    final Component message = this.translations.shopModificationSuccess(user, placeholders, success);
                    user.sendMessage(message);
                }
                case Result.Error(Set<ShopFailure> errors) -> {
                    for (final ShopFailure failure : errors) {
                        switch (failure) {
                            case ShopFailure.ShopNotFound it -> {
                                final Component message = this.translations.shopModificationFailedShopNotFound(user, placeholders, it);
                                user.sendMessage(message);
                            }
                            case ShopFailure.OperatorInsufficientFunds it -> {
                                final Component message = this.translations.shopModificationFailedInsufficientFunds(user, placeholders, it);
                                user.sendMessage(message);
                            }
                            case ShopFailure.PriceOutOfRange it -> {
                                final Component message = this.translations.shopModificationFailedPriceOutOfRange(user, placeholders, it);
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

        final var applyButton = ActionButton.builder(this.translations.shopModificationConfirmButton(user, placeholders))
                .action(DialogAction.customClick(callback, callbackOptions))
                .build();
        final var cancelButton = ActionButton.builder(this.translations.shopModificationCancelButton(user, placeholders))
                .build();

        return DialogType.confirmation(applyButton, cancelButton);
    }
}
