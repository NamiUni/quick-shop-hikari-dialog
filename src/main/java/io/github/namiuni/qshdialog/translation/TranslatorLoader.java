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
package io.github.namiuni.qshdialog.translation;

import com.github.sviperll.result4j.Result;
import io.github.namiuni.qshdialog.utility.JisSafetyColors;
import io.github.namiuni.qshdialog.utility.MoreFiles;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslatorLoader {

    private static final Set<Locale> INCLUDED_BUNDLE_LOCALES = Set.of(Locale.US, Locale.JAPAN);
    private static final Key STORE_NAME = Key.key("qsh_dialog", "translations");
    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(TagResolver.standard())
                    .resolver(JisSafetyColors.jisSafetyColors())
                    .build())
            .build();

    private final Path dataDirectory;
    private final ComponentLogger logger;

    public TranslatorLoader(final Path dataDirectory, final ComponentLogger logger) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    public Result<Translator, IOException> load() {

        final Path translationDirectory = MoreFiles.createDirectories(this.dataDirectory.resolve("translations"));
        final var store = MiniMessageTranslationStore.create(STORE_NAME, MINI_MESSAGE);
        final Set<Locale> installedLocales = new HashSet<>();

        try (Stream<Path> pathStream = Files.list(translationDirectory)) {
            pathStream.filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".properties"))
                    .map(LocaleBundlePair::of)
                    .forEach(localeBundlePair -> {
                        store.registerAll(localeBundlePair.locale, localeBundlePair.bundlePath, false);
                        installedLocales.add(localeBundlePair.locale);
                    });
        } catch (final IOException exception) {
            return Result.error(exception);
        }

        INCLUDED_BUNDLE_LOCALES.stream()
                .map(locale -> ResourceBundle.getBundle(
                        "translations/messages",
                        locale,
                        TranslatorHolder.class.getClassLoader(),
                        new InstallableResourceBundleControl(this.dataDirectory))
                )
                .forEach(bundle -> {
                    store.registerAll(bundle.getLocale(), bundle, false);
                    installedLocales.add(bundle.getLocale());
                });

        final String localeTags = installedLocales.stream().map(Locale::toString).collect(Collectors.joining(", "));
        this.logger.info("Loaded {} translations: [{}]", installedLocales.size(), localeTags);

        return Result.success(store);
    }

    private record LocaleBundlePair(Locale locale, Path bundlePath) {
        static LocaleBundlePair of(final Path translationFile) {
            final String localeString = translationFile.getFileName().toString()
                    .substring("messages_".length())
                    .replace(".properties", "");
            final Locale locale = Locale.of(localeString);
            return new LocaleBundlePair(locale, translationFile);
        }
    }
}
