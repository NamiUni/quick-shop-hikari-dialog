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

import io.github.namiuni.qshdialog.command.commands.AdminCommand;
import io.github.namiuni.qshdialog.command.commands.ShopCommand;
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.configurations.PrimaryConfiguration;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.DialogConfiguration;
import io.github.namiuni.qshdialog.translation.TranslatorHolder;
import io.github.namiuni.qshdialog.translation.TranslatorLoader;
import io.github.namiuni.qshdialog.user.QSHUserService;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.event.RegistryEvents;
import java.nio.file.Path;
import java.util.List;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class QSHDialogBootstrap implements PluginBootstrap {

    private @MonotonicNonNull ConfigurationHolder<PrimaryConfiguration> primaryConfig;
    private @MonotonicNonNull ConfigurationHolder<DialogConfiguration> shopCreationConfig;
    private @MonotonicNonNull TranslatorHolder translatorHolder;
    private @MonotonicNonNull QSHUserService userService;

    @Override
    public void bootstrap(final BootstrapContext context) {
        this.primaryConfig = ConfigurationHolder.primary(context.getDataDirectory());
        this.translatorHolder = this.createTranslatorHolder(context);
        this.userService = new QSHUserService();

        context.getLifecycleManager().registerEventHandler(RegistryEvents.DIALOG.compose(), event ->
                this.shopCreationConfig = ConfigurationHolder.shopCreationDialog(context.getDataDirectory()));
        this.registerCommands(context);
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return new QSHDialogPlugin(this.userService);
    }

    private TranslatorHolder createTranslatorHolder(final BootstrapContext context) {
        final Path dataDirectory = context.getDataDirectory();
        final ComponentLogger logger = context.getLogger();
        final TranslatorLoader translatorLoader = new TranslatorLoader(dataDirectory, logger);
        return new TranslatorHolder(translatorLoader);
    }

    private void registerCommands(final BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final AdminCommand adminCommand = new AdminCommand(this.primaryConfig, this.translatorHolder);
            final ShopCommand shopCommand = new ShopCommand(this.shopCreationConfig, null, this.userService);

            List.of(adminCommand, shopCommand).forEach(command -> {
                event.registrar().register(command.node(), command.description(), command.aliases());
            });
        });
    }
}
