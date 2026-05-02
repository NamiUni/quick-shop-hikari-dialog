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

import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.Reloadable;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.util.concurrent.atomic.AtomicReference;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class TranslatorHolder implements Provider<Translator>, Reloadable<Translator> {

    private final TranslatorLoader translatorLoader;
    private final AtomicReference<Translator> translator;
    private final ComponentLogger logger;

    @Inject
    TranslatorHolder(
            final TranslatorLoader translatorLoader,
            final ComponentLogger logger
    ) {
        this.translatorLoader = translatorLoader;
        this.logger = logger;

        this.logger.info("Loading translations...");
        final Translator initial = translatorLoader.loadTranslator();

        this.translator = new AtomicReference<>(initial);
        this.logger.info("Translations loaded.");
    }

    @Override
    public Translator reload() {
        this.logger.info("Reloading translations...");
        return this.translator.updateAndGet(_ -> {
            final Translator fresh = this.translatorLoader.loadTranslator();
            this.logger.info("Translation reload complete.");
            return fresh;
        });
    }

    @Override
    public Translator get() {
        return this.translator.get();
    }
}
