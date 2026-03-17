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
package io.github.namiuni.qshdialog.minecraft.paper.configuration;

import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.common.utilities.QSHDialogLogger;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.serializer.LocaleSerializer;
import io.github.namiuni.qshdialog.minecraft.paper.configuration.serializer.ShopInputTypeSerializer;
import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.ShopInputType;
import java.nio.file.Path;
import java.util.Locale;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

@NullMarked
final class ConfigurationLoader<T> {

    private final Class<T> configClass;
    private final T defaultConfig;
    private final Path configPath;
    private final String configHeader;

    ConfigurationLoader(
            final Class<T> configClass,
            final T defaultConfig,
            final Path configPath,
            final String configHeader
    ) {
        this.configClass = configClass;
        this.defaultConfig = defaultConfig;
        this.configPath = configPath;
        this.configHeader = configHeader;
    }

    public Result<T, ConfigurateException> load() {
        final var kyoriSerializer = ConfigurateComponentSerializer.builder()
                .scalarSerializer(MiniMessage.miniMessage())
                .build()
                .serializers();

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .defaultOptions(options -> options
                        .shouldCopyDefaults(true)
                        .header(this.configHeader)
                        .serializers(builder -> builder
                                .registerAll(kyoriSerializer)
                                .register(Locale.class, LocaleSerializer.INSTANCE)
                                .register(ShopInputType.class, ShopInputTypeSerializer.INSTANCE)
                        )
                )
                .path(this.configPath)
                .build();

        try {
            final ConfigurationNode node = loader.load();

            final T config = node.get(this.configClass, this.defaultConfig);
            loader.save(node);

            QSHDialogLogger.logger().info("Loaded configuration: {}", this.configPath.getFileName());

            return Result.success(config);
        } catch (final ConfigurateException exception) {
            return Result.error(exception);
        }
    }
}
