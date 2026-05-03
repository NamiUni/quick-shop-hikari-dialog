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
package io.github.namiuni.qshdialog.minecraft.paper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.Reloadable;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.UncheckedConfigurateException;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.configuration.configurations.PrimaryConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.configuration.QSConfiguration;
import io.github.namiuni.qshdialog.minecraft.paper.permission.QSHDialogPermissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import jakarta.inject.Inject;
import java.io.UncheckedIOException;
import java.util.List;
import net.kyori.adventure.translation.Translator;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AdminCommand implements CommandFactory {

    private final Reloadable<PrimaryConfiguration> primaryConfig;
    private final Reloadable<Translator> translatorHolder;
    private final TranslationService translations;
    private final QSConfiguration qsConfig;

    @Inject
    AdminCommand(
            final Reloadable<PrimaryConfiguration> primaryConfig,
            final Reloadable<Translator> translator,
            final TranslationService translations,
            final QSConfiguration qsConfig
    ) {
        this.primaryConfig = primaryConfig;
        this.translatorHolder = translator;
        this.translations = translations;
        this.qsConfig = qsConfig;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("qshdialog")
                .then(Commands.literal("reload")
                        .requires(Commands.restricted(source -> source.getSender().hasPermission(QSHDialogPermissions.COMMAND_RELOAD)))
                        .executes(context -> {
                            final CommandSender commandSender = context.getSource().getSender();

                            this.qsConfig.reload();

                            try {
                                this.primaryConfig.reload();
                                this.translations.configReloadSuccess(commandSender);
                            } catch (final UncheckedConfigurateException exception) {
                                this.translations.configReloadFailure(commandSender);
                                return SINGLE_FAILED;
                            }

                            try {
                                this.translatorHolder.reload();
                                this.translations.translationReloadSuccess(commandSender);
                            } catch (final UncheckedIOException exception) {
                                this.translations.translationReloadFailure(commandSender);
                                return SINGLE_FAILED;
                            }

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    @Override
    public List<String> aliases() {
        return CommandFactory.super.aliases();
    }

    @Override
    public String description() {
        return CommandFactory.super.description();
    }
}
