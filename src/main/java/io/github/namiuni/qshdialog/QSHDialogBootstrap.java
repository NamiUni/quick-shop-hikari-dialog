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
package io.github.namiuni.qshdialog;

import com.google.common.base.Suppliers;
import io.github.namiuni.qshdialog.command.commands.AdminCommand;
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.ConfigurationLoader;
import io.github.namiuni.qshdialog.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.shop.dialog.ItemPurchaseDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ItemSaleDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopCreationDialogFactory;
import io.github.namiuni.qshdialog.shop.dialog.ShopModificationDialogFactory;
import io.github.namiuni.qshdialog.translation.TranslatorHolder;
import io.github.namiuni.qshdialog.translation.TranslatorLoader;
import io.github.namiuni.qshdialog.user.QSHUserService;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.nio.file.Path;
import java.util.function.Supplier;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class QSHDialogBootstrap implements PluginBootstrap {

    private @MonotonicNonNull ConfigurationHolder<PrimaryConfiguration> configHolder;
    private @MonotonicNonNull TranslatorHolder translatorHolder;
    private @MonotonicNonNull Supplier<QSHUserService> userService;

    @Override
    public void bootstrap(final BootstrapContext context) {
        this.configHolder = this.createConfigHolder(context);
        this.translatorHolder = this.createTranslatorHolder(context);
        this.userService = Suppliers.memoize(this::createUserService);

        this.registerCommands(context);
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return new QSHDialogPlugin(this.userService);
    }

    private ConfigurationHolder<PrimaryConfiguration> createConfigHolder(final BootstrapContext context) {
        final Path dataDirectory = context.getDataDirectory();
        final ComponentLogger logger = context.getLogger();
        final ConfigurationLoader<PrimaryConfiguration> configLoader = new ConfigurationLoader<>(
                PrimaryConfiguration.class,
                new PrimaryConfiguration(),
                dataDirectory,
                logger
        );
        return new ConfigurationHolder<>(configLoader);
    }

    private TranslatorHolder createTranslatorHolder(final BootstrapContext context) {
        final Path dataDirectory = context.getDataDirectory();
        final ComponentLogger logger = context.getLogger();
        final TranslatorLoader translatorLoader = new TranslatorLoader(dataDirectory, logger);
        return new TranslatorHolder(translatorLoader);
    }

    private QSHUserService createUserService() {
        final var itemPurchaseDialogFactory = new ItemPurchaseDialogFactory(this.configHolder);
        final var itemSaleDialogFactory = new ItemSaleDialogFactory(this.configHolder);
        final var shopCreationDialogFactory = new ShopCreationDialogFactory(this.configHolder);
        final var shopModificationDialogFactory = new ShopModificationDialogFactory(this.configHolder);
        return new QSHUserService(
                shopCreationDialogFactory,
                shopModificationDialogFactory,
                itemPurchaseDialogFactory,
                itemSaleDialogFactory
        );
    }

    private void registerCommands(final BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final AdminCommand command = new AdminCommand(this.configHolder, this.translatorHolder);
            event.registrar().register(command.node(), command.description(), command.aliases());
        });
    }
}
