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
package io.github.namiuni.qshdialog.minecraft.paper;

import io.github.namiuni.qshdialog.minecraft.paper.commands.AdminCommand;
import io.github.namiuni.qshdialog.minecraft.paper.commands.ShopCommand;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopCreationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.ShopModificationDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradePurchaseDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.TradeSellDialog;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.ShopInputs;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.TradeInputs;
import io.github.namiuni.qshdialog.minecraft.paper.service.ShopService;
import io.github.namiuni.qshdialog.minecraft.paper.translation.Translations;
import io.github.namiuni.qshdialog.minecraft.paper.translation.TranslatorHolder;
import io.github.namiuni.qshdialog.minecraft.paper.translation.TranslatorLoader;
import io.github.namiuni.qshdialog.minecraft.paper.utilities.ShopTagMapper;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.nio.file.Path;
import java.util.stream.Stream;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class QSHDialogBootstrap implements PluginBootstrap {

    private @MonotonicNonNull ConfigurationHolder<PrimaryConfiguration> primaryConfig;
    private @MonotonicNonNull TranslatorHolder translatorHolder;
    private @MonotonicNonNull Translations translations;

    private @MonotonicNonNull ShopService shopService;
    private @MonotonicNonNull ShopCreationDialog shopCreationDialog;
    private @MonotonicNonNull ShopModificationDialog shopModificationDialog;
    private @MonotonicNonNull TradePurchaseDialog tradePurchaseDialog;
    private @MonotonicNonNull TradeSellDialog tradeSellDialog;

    @Override
    public void bootstrap(final BootstrapContext context) {
        this.primaryConfig = ConfigurationHolder.createPrimaryConfigHolder(context.getDataDirectory());
        this.translatorHolder = this.createTranslatorHolder(context);
        this.translations = new Translations(this.primaryConfig);
        this.shopService = new ShopService();
        this.initializeDialogs();
        this.registerCommands(context);
    }

    private void initializeDialogs() {
        final ShopInputs shopInputs = new ShopInputs(this.translations);
        final TradeInputs tradeInputs = new TradeInputs(this.translations);
        final ShopTagMapper shopTagMapper = new ShopTagMapper(this.translations);
        this.shopCreationDialog = new ShopCreationDialog(
                this.primaryConfig,
                this.translations,
                this.shopService,
                shopInputs,
                shopTagMapper
        );
        this.shopModificationDialog = new ShopModificationDialog(
                this.primaryConfig,
                this.translations,
                this.shopService,
                shopInputs,
                shopTagMapper
        );
        this.tradePurchaseDialog = new TradePurchaseDialog(
                this.primaryConfig,
                this.translations,
                tradeInputs,
                shopTagMapper
        );
        this.tradeSellDialog = new TradeSellDialog(
                this.primaryConfig,
                this.translations,
                tradeInputs,
                shopTagMapper
        );
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return new QSHDialogPlugin(
                this.translations,
                this.shopCreationDialog,
                this.shopModificationDialog,
                this.tradePurchaseDialog,
                this.tradeSellDialog
        );
    }

    private TranslatorHolder createTranslatorHolder(final BootstrapContext context) {
        final Path dataDirectory = context.getDataDirectory();
        final ComponentLogger logger = context.getLogger();
        final TranslatorLoader translatorLoader = new TranslatorLoader(dataDirectory, logger);
        return new TranslatorHolder(translatorLoader);
    }

    private void registerCommands(final BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final AdminCommand adminCommand = new AdminCommand(this.primaryConfig, this.translatorHolder, this.translations);
            final ShopCommand shopCommand = new ShopCommand(this.translations, this.shopService, this.shopCreationDialog, this.shopModificationDialog, this.tradePurchaseDialog, this.tradeSellDialog);

            Stream.of(adminCommand, shopCommand)
                    .forEach(command -> event.registrar().register(command.node(), command.description(), command.aliases()));
        });
    }
}
