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
package io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation;

import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.DataDirectory;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.PluginSource;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.configurations.PrimaryConfiguration;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class TranslatorLoader {

    private static final String BASE_NAME = "messages";
    private static final String FILE_SUFFIX = ".properties";

    private final ComponentLogger logger;
    private final MiniMessage miniMessage;
    private final Path translationDir;
    private final Path pluginResource;
    private final Key translationKey;
    private final Provider<PrimaryConfiguration> primaryConfig;

    @Inject
    TranslatorLoader(
            final ComponentLogger logger,
            final MiniMessage miniMessage,
            final @DataDirectory Path dataDirectory,
            final @PluginSource Path pluginResource,
            final Provider<PrimaryConfiguration> primaryConfig
    ) {
        this.logger = logger;
        this.miniMessage = miniMessage;
        this.translationDir = dataDirectory.resolve("translations");
        this.pluginResource = pluginResource;
        this.primaryConfig = primaryConfig;

        try {
            Files.createDirectories(this.translationDir);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }

        this.translationKey = Key.key("qsh_dialog", BASE_NAME);
    }

    Translator loadTranslator() {
        final MiniMessageTranslationStore store = MiniMessageTranslationStore.create(this.translationKey, this.miniMessage);
        store.defaultLocale(this.primaryConfig.get().defaultLocale());

        final Set<Locale> diskLocales = this.registerDiskTranslations(store);
        final Set<Locale> jarLocales = this.registerJarTranslations(store, diskLocales);

        Stream.concat(diskLocales.stream(), jarLocales.stream())
                .distinct()
                .filter(locale -> Objects.equals(this.primaryConfig.get().defaultLocale(), locale))
                .findFirst()
                .ifPresentOrElse(
                        store::defaultLocale,
                        () -> {
                            store.defaultLocale(Locale.ROOT);
                            final Locale configuredDefault = this.primaryConfig.get().defaultLocale();
                            this.logger.warn("Configured default locale '{}' was not found. Falling back to ROOT.", configuredDefault);
                        }
                );

        return store;
    }

    private Set<Locale> registerDiskTranslations(final MiniMessageTranslationStore store) {
        final Set<Locale> registered = new HashSet<>();
        try (Stream<Path> files = Files.list(this.translationDir)) {
            files
                    .filter(Files::isRegularFile)
                    .filter(path -> isTranslationFile(path.getFileName().toString()))
                    .forEach(path -> {
                        final String fileName = path.getFileName().toString();
                        final Locale locale = parseLocale(fileName);

                        // ROOT locale is reserved for the JAR bundle. A disk file that
                        // resolves to ROOT (e.g. a hand-placed messages.properties) would
                        // corrupt the fallback chain, so we skip it with a clear warning.
                        if (locale == Locale.ROOT) {
                            this.logger.warn(
                                    "Skipped '{}': ROOT locale must not be overridden from disk. " +
                                    "Place your English customisations in 'messages_en_US.properties' instead.",
                                    fileName
                            );
                            return;
                        }

                        store.registerAll(locale, path, false);
                        registered.add(locale);
                        this.logger.debug("[{}] Registered from disk: {} ({})", TranslatorLoader.class.getSimpleName(), fileName, locale);
                    });
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
        if (!registered.isEmpty()) {
            this.logger.info("Loaded {} translation file(s) from disk: {}.", registered.size(), registered);
        }
        return registered;
    }

    private Set<Locale> registerJarTranslations(final MiniMessageTranslationStore store, final Set<Locale> diskLocales) {
        final Set<Locale> registered = new HashSet<>();
        try (FileSystem jar = FileSystems.newFileSystem(this.pluginResource, TranslatorLoader.class.getClassLoader())) {
            final Path root = jar.getRootDirectories().iterator().next();
            try (Stream<Path> paths = Files.walk(root)) {
                paths
                        .filter(Files::isRegularFile)
                        .filter(path -> isTranslationFile(path.getFileName().toString()))
                        .forEach(path -> {
                            final Locale locale = this.processJarEntry(store, diskLocales, path);
                            registered.add(locale);
                        });
            }
        } catch (final IOException exception) {
            this.logger.warn("Could not scan JAR for translations. Skipping resource export.", exception);
        }
        return registered;
    }

    private Locale processJarEntry(
            final MiniMessageTranslationStore store,
            final Set<Locale> diskLocales,
            final Path jarPath
    ) {
        final String fileName = jarPath.getFileName().toString();
        final Locale locale = parseLocale(fileName);

        if (locale == Locale.ROOT || !diskLocales.contains(locale)) {
            store.registerAll(locale, jarPath, false);
            this.logger.debug("[{}] Registered from JAR: {} ({})", TranslatorLoader.class.getSimpleName(), fileName, locale);
        }

        final Path diskTarget = this.translationDir.resolve(fileName);
        if (locale != Locale.ROOT && Files.notExists(diskTarget)) {
            this.exportDefault(jarPath, diskTarget, fileName);
        }

        return locale;
    }

    private void exportDefault(final Path source, final Path target, final String fileName) {
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            this.logger.debug("[{}] Exported default: {}", TranslatorLoader.class.getSimpleName(), fileName);
        } catch (final IOException exception) {
            this.logger.error("Failed to export default: {}", fileName, exception);
        }
    }

    private static boolean isTranslationFile(final String name) {
        return name.startsWith(BASE_NAME) && name.endsWith(FILE_SUFFIX);
    }

    private static Locale parseLocale(final String fileName) {
        if (Objects.equals(BASE_NAME + FILE_SUFFIX, fileName)) {
            return Locale.ROOT;
        }

        final String tag = fileName.substring(BASE_NAME.length() + 1, fileName.length() - FILE_SUFFIX.length());
        final Locale locale = Translator.parseLocale(tag);
        if (locale == null) {
            throw new IllegalArgumentException("Cannot parse locale from translation file shopName: " + fileName);
        }

        return locale;
    }
}
