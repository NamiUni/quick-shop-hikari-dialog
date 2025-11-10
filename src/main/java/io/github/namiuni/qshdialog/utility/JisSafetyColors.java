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
package io.github.namiuni.qshdialog.utility;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class JisSafetyColors {

    // JIS Z 9103 https://ja.wikipedia.org/wiki/JIS%E5%AE%89%E5%85%A8%E8%89%B2
    private static final TagResolver JIS_SAFETY_COLORS = TagResolver.builder()
            .tag("jis_red", Tag.styling(TextColor.color(0xFF4B00)))
            .tag("jis_orange", Tag.styling(TextColor.color(0xF6AA00)))
            .tag("jis_yellow", Tag.styling(TextColor.color(0xF2E700)))
            .tag("jis_green", Tag.styling(TextColor.color(0x00B06B)))
            .tag("jis_blue", Tag.styling(TextColor.color(0x1971FF)))
            .tag("jis_purple", Tag.styling(TextColor.color(0x990099)))
            .build();

    private JisSafetyColors() {
    }

    public static TagResolver jisSafetyColors() {
        return JIS_SAFETY_COLORS;
    }
}
