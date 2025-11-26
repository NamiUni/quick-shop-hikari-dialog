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
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.QSHDialogItemType;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.type.DialogTypeSettings;
import io.github.namiuni.qshdialog.configuration.serializer.DataComponentMapSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.DataComponentTypeSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.DialogActionSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.DialogAfterActionSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.DialogBodySettingsSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.DialogInputSettingsSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.DialogTypeSettingsSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.ItemSettingsSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.ItemTypeSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.QSHDialogItemTypeSerializer;
import io.github.namiuni.qshdialog.configuration.serializer.TextDialogInputMultilineOptionsSerializer;
import io.github.namiuni.qshdialog.utility.QSHDialogLogger;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import java.nio.file.Path;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemType;
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

    @SuppressWarnings("UnstableApiUsage")
    public Result<T, ConfigurateException> load() {
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .defaultOptions(options -> options
                        .shouldCopyDefaults(true)
                        .header(this.configHeader)
                        .serializers(builder -> builder
                                .registerAll(ConfigurateComponentSerializer.builder().scalarSerializer(MiniMessage.miniMessage()).build().serializers())
                                .register(ItemType.class, ItemTypeSerializer.INSTANCE)
                                .register(QSHDialogItemType.class, QSHDialogItemTypeSerializer.INSTANCE)
                                .register(DataComponentTypeSerializer.INSTANCE)
                                .register(new TypeToken<>() { }, DataComponentMapSerializer.INSTANCE)
                                .register(new TypeToken<>() { }, ItemSettingsSerializer.INSTANCE)
                                .register(TextDialogInput.MultilineOptions.class, TextDialogInputMultilineOptionsSerializer.INSTANCE)
                                .register(DialogAction.class, DialogActionSerializer.INSTANCE)
                                .register(DialogBase.DialogAfterAction.class, DialogAfterActionSerializer.INSTANCE)
                                .register(DialogBodySettings.class, DialogBodySettingsSerializer.INSTANCE)
                                .register(DialogInputSettings.class, DialogInputSettingsSerializer.INSTANCE)
                                .register(DialogTypeSettings.class, DialogTypeSettingsSerializer.INSTANCE)
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
