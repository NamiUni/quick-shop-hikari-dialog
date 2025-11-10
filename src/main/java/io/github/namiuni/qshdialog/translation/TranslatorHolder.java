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
import io.github.namiuni.qshdialog.utility.Reloadable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicReference;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslatorHolder implements Reloadable<Translator, IOException> {

    private final TranslatorLoader translatorLoader;

    private final AtomicReference<Translator> translator;

    public TranslatorHolder(final TranslatorLoader translatorLoader) {
        this.translatorLoader = translatorLoader;

        final Translator loaded = this.translatorLoader.load().orOnErrorThrow(UncheckedIOException::new);
        this.translator = new AtomicReference<>(loaded);
        GlobalTranslator.translator().addSource(this.translator.get());
    }

    public Translator getTranslator() {
        return this.translator.get();
    }

    public Result<Translator, IOException> reload() {
        final Result<Translator, IOException> result = this.translatorLoader.load();
        if (result instanceof Result.Success<Translator, IOException>(Translator newTranslator)) {
            GlobalTranslator.translator().removeSource(this.translator.get());
            this.translator.set(newTranslator);
            GlobalTranslator.translator().addSource(newTranslator);
        }

        return result;
    }
}
