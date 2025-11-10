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
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.ConfigurationLoader;
import io.github.namiuni.qshdialog.configuration.PrimaryConfiguration;
import io.github.namiuni.qshdialog.translation.TranslatorHolder;
import io.github.namiuni.qshdialog.translation.TranslatorLoader;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.nio.file.Path;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class QSHDialogBootstrap implements PluginBootstrap {

    private @MonotonicNonNull ConfigurationHolder<PrimaryConfiguration> configHolder;
    private @MonotonicNonNull TranslatorHolder translatorHolder;

    @Override
    public void bootstrap(final BootstrapContext context) {
        final Path dataDirectory = context.getDataDirectory();
        final ComponentLogger logger = context.getLogger();

        final ConfigurationLoader<PrimaryConfiguration> configLoader = new ConfigurationLoader<>(
                PrimaryConfiguration.class,
                new PrimaryConfiguration(),
                dataDirectory,
                logger
        );
        this.configHolder = new ConfigurationHolder<>(configLoader);

        final TranslatorLoader translatorLoader = new TranslatorLoader(dataDirectory, logger);
        this.translatorHolder = new TranslatorHolder(translatorLoader);

        this.registerCommands(context);
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return new QSHDialogPlugin(this.configHolder, this.translatorHolder);
    }

    private void registerCommands(final BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final AdminCommand command = new AdminCommand(this.configHolder, this.translatorHolder);
            event.registrar().register(command.node(), command.description(), command.aliases());
        });
    }
}
