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
package io.github.namiuni.qshdialog.configuration;

import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.configuration.annotations.ConfigHeader;
import io.github.namiuni.qshdialog.configuration.annotations.ConfigName;
import io.github.namiuni.qshdialog.configuration.configurations.PrimaryConfiguration;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.DialogConfiguration;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.DialogConfigurations;
import io.github.namiuni.qshdialog.utility.Reloadable;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;

@NullMarked
public final class ConfigurationHolder<T> implements Reloadable<T, ConfigurateException> {

    private final ConfigurationLoader<T> configLoader;
    private final AtomicReference<T> config;

    ConfigurationHolder(final ConfigurationLoader<T> configLoader) {
        this.configLoader = configLoader;

        final T loadedConfig = this.configLoader.load().orOnErrorThrow(UncheckedIOException::new);
        this.config = new AtomicReference<>(loadedConfig);
    }

    public static ConfigurationHolder<PrimaryConfiguration> primary(final Path dataDirectory) {
        final String configName = PrimaryConfiguration.class.getAnnotation(ConfigName.class).value();
        final Path configPath = dataDirectory.resolve(configName);
        final String configHeader = PrimaryConfiguration.class.getAnnotation(ConfigHeader.class).value();

        final ConfigurationLoader<PrimaryConfiguration> loader = new ConfigurationLoader<>(
                PrimaryConfiguration.class,
                PrimaryConfiguration.DEFAULT,
                configPath,
                configHeader
        );
        return new ConfigurationHolder<>(loader);
    }

    public static ConfigurationHolder<DialogConfiguration> shopCreationDialog(final Path dataDirectory) {
        final String configName = "shop-creation.conf";
        final Path configPath = dataDirectory.resolve(configName);
        final String configHeader = ""; // TODO

        final ConfigurationLoader<DialogConfiguration> loader = new ConfigurationLoader<>(
                DialogConfiguration.class,
                DialogConfigurations.CREATION_DEFAULT,
                configPath,
                configHeader
        );
        return new ConfigurationHolder<>(loader);
    }

    public T getConfig() {
        return this.config.get();
    }

    public Result<T, ConfigurateException> reload() {
        final Result<T, ConfigurateException> result = this.configLoader.load();
        if (result instanceof Result.Success<T, ConfigurateException>(T value)) {
            this.config.set(value);
        }

        return result;
    }
}
