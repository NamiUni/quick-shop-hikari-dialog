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
import java.nio.file.Path;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@NullMarked
public final class ConfigurationLoader<T> {

    private final Class<T> configClass;
    private final T defaultConfig;
    private final Path configDirectory;
    private final ComponentLogger logger;

    public ConfigurationLoader(
            final Class<T> configClass,
            final T defaultConfig,
            final Path configDirectory,
            final ComponentLogger logger
    ) {
        this.configClass = configClass;
        this.defaultConfig = defaultConfig;
        this.configDirectory = configDirectory;
        this.logger = logger;
    }

    public Result<T, ConfigurateException> load() {
        // Config path
        final String configName = this.configClass.getAnnotation(ConfigName.class).value();
        final Path configPath = this.configDirectory.resolve(configName);

        // Config header
        final String configHeader = this.configClass.getAnnotation(ConfigHeader.class).value();

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(options -> options
                        .shouldCopyDefaults(true)
                        .header(configHeader)
                        .serializers(ConfigurateComponentSerializer.configurate().serializers()))
                .path(configPath)
                .build();

        try {
            final ConfigurationNode node = loader.load();

            final T config = node.get(this.configClass, this.defaultConfig);
            loader.save(node);

            this.logger.info("Loaded configuration: {}", configName);

            return Result.success(config);
        } catch (final ConfigurateException exception) {
            return Result.error(exception);
        }
    }
}
