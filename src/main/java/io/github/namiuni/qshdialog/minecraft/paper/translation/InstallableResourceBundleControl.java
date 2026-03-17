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
package io.github.namiuni.qshdialog.minecraft.paper.translation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class InstallableResourceBundleControl extends ResourceBundle.Control {

    private final Path dataDirectory;

    InstallableResourceBundleControl(final Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    public @Nullable ResourceBundle newBundle(
            final String baseName,
            final Locale locale,
            final String format,
            final ClassLoader loader,
            final boolean reload
    ) throws IOException, IllegalAccessException, InstantiationException {
        if (!"java.properties".equals(format)) {
            return super.newBundle(baseName, locale, format, loader, reload);
        }

        final String bundleName = this.toBundleName(baseName, locale);
        final String resourceName = this.toResourceName(bundleName, "properties");
        try (InputStream inputStream = loader.getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                return null;
            }

            final Path destinationPath = this.dataDirectory.resolve(resourceName);
            final byte[] content = inputStream.readAllBytes();
            if (!Files.exists(destinationPath)) {
                InstallableResourceBundleControl.install(content, destinationPath);
            }

            return new PropertyResourceBundle(new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8));
        }
    }

    private static void install(final byte[] content, final Path destinationPath) throws IOException {
        Files.createDirectories(destinationPath.getParent());
        Files.write(destinationPath, content);
    }
}
