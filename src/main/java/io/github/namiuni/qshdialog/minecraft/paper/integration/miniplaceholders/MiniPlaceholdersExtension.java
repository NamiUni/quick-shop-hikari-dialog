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
package io.github.namiuni.qshdialog.minecraft.paper.integration.miniplaceholders;

import io.github.miniplaceholders.api.MiniPlaceholders;
import java.util.concurrent.atomic.AtomicReference;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.util.TriState;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MiniPlaceholdersExtension {

    private static final AtomicReference<TriState> MINI_PLACEHOLDERS_LOADED = new AtomicReference<>(TriState.NOT_SET);

    private MiniPlaceholdersExtension() {
    }

    static boolean miniPlaceholdersLoaded() {
        if (MINI_PLACEHOLDERS_LOADED.get() == TriState.NOT_SET) {
            try {
                Class.forName("io.github.miniplaceholders.api.MiniPlaceholders");
                MINI_PLACEHOLDERS_LOADED.compareAndSet(TriState.NOT_SET, TriState.TRUE);
            } catch (final ClassNotFoundException ignored) {
                MINI_PLACEHOLDERS_LOADED.compareAndSet(TriState.NOT_SET, TriState.FALSE);
            }
        }
        return MINI_PLACEHOLDERS_LOADED.get() == TriState.TRUE;
    }

    public static TagResolver audienceGlobalPlaceholders() {
        if (!miniPlaceholdersLoaded()) {
            return TagResolver.empty();
        }
        return MiniPlaceholders.audienceGlobalPlaceholders();
    }
}
