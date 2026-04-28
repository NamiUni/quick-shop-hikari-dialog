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
import com.ghostchu.quickshop.obj.QUserImpl;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSConfigurations;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShops;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.intellij.lang.annotations.Pattern;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface UserSession extends Audience, Identified {

    static UserSession of(final QUser qsUser) {
        return new UserSessionImpl(qsUser);
    }

    static UserSession of(final Player player) {
        final QUser qUser = QUserImpl.createFullFilled(player);
        return UserSession.of(qUser);
    }

    QUser qsUser();

    default Optional<Player> bukkit() {
        return this.qsUser().getBukkitPlayer();
    }

    default UUID uuid() {
        return this.get(Identity.UUID).orElseThrow();
    }

    @SuppressWarnings("PatternValidation")
    default @Pattern("^[!-~]{0,16}$") String name() {
        return this.get(Identity.NAME).orElseThrow();
    }

    default ComponentLike displayName() {
        return this.get(Identity.DISPLAY_NAME).orElseThrow();
    }

    default Locale locale() {
        return this.get(Identity.LOCALE).orElse(Locale.US);
    }

    default boolean hasPermission(final String permission) {
        return this.get(PermissionChecker.POINTER)
                .map(checker -> checker.test(permission))
                .orElseThrow();
    }

    default BigDecimal balance(final String world, final @Nullable String currency) {
        return QuickShops.economyProvider().balance(this.qsUser(), world, currency);
    }

    default boolean withdrawMoney(final BigDecimal amount, final String worldName) {
        return QuickShops.economyProvider().withdraw(
                this.qsUser(),
                worldName,
                QSConfigurations.getCurrency(),
                amount
        );
    }

    default boolean depositMoney(final BigDecimal amount, final String worldName) {
        return QuickShops.economyProvider().deposit(
                this.qsUser(),
                worldName,
                QSConfigurations.getCurrency(),
                amount
        );
    }

    default @Nullable Block targetBlock() {
        final Player bukkit = this.bukkit().orElseThrow();
        return bukkit.getTargetBlockExact((int) this.interactionLength());
    }

    default double interactionLength() {
        final Player bukkit = this.bukkit().orElseThrow();
        final AttributeInstance clickDistance = Objects.requireNonNull(bukkit.getAttribute(Attribute.BLOCK_INTERACTION_RANGE));
        return clickDistance.getValue();
    }

    default BlockFace direction() {
        final Player bukkit = this.bukkit().orElseThrow();
        final float yaw = ((bukkit.getYaw() % 360) + 360) % 360;

        return switch ((int) ((yaw + 45) / 90) % 4) {
            case 1 -> BlockFace.WEST;
            case 2 -> BlockFace.NORTH;
            case 3 -> BlockFace.EAST;
            default -> BlockFace.SOUTH;
        };
    }

    default PlayerInventory inventory() {
        final Player bukkit = this.bukkit().orElseThrow();
        return bukkit.getInventory();
    }

    default World world() {
        final Player bukkit = this.bukkit().orElseThrow();
        return bukkit.getWorld();
    }

    @Override
    default Identity identity() {
        return Identity.identity(this.uuid());
    }
}
