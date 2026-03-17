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
package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model;

import com.ghostchu.quickshop.api.obj.QUser;
import java.util.Objects;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jspecify.annotations.NullMarked;

@NullMarked
record UserSessionImpl(QUser qsUser) implements UserSession, ForwardingAudience.Single {

    @Override
    public Audience audience() {
        return this.qsUser.getBukkitPlayer()
                .map(Audience.class::cast)
                .orElse(Audience.empty());
    }

    @Override
    public QUser qsUser() {
        return this.qsUser;
    }

    @Override
    @SuppressWarnings("PatternValidation")
    public String name() {
        return Objects.requireNonNullElse(this.qsUser.getUsername(), "Unknown");
    }
}
