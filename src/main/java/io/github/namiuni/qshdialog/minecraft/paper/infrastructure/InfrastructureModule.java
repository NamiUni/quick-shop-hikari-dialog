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
package io.github.namiuni.qshdialog.minecraft.paper.infrastructure;

import com.google.inject.AbstractModule;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.ConfigurationModule;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.TranslationModule;
import java.nio.file.Path;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class InfrastructureModule extends AbstractModule {

    private final ComponentLogger logger;
    private final Path dataDirectory;
    private final Path pluginResource;

    public InfrastructureModule(
            final ComponentLogger logger,
            final Path dataDirectory,
            final Path pluginResource
    ) {
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.pluginResource = pluginResource;
    }

    @Override
    protected void configure() {
        this.bind(Path.class).annotatedWith(DataDirectory.class).toInstance(this.dataDirectory);
        this.bind(Path.class).annotatedWith(PluginSource.class).toInstance(this.pluginResource);
        this.bind(ComponentLogger.class).toInstance(this.logger);

        this.install(new ConfigurationModule());
        this.install(new TranslationModule());
    }
}
