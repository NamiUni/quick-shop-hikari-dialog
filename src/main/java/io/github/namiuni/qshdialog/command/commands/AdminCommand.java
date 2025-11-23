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
package io.github.namiuni.qshdialog.command.commands;

import com.github.sviperll.result4j.Result;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.namiuni.qshdialog.QSHPermissions;
import io.github.namiuni.qshdialog.configuration.ConfigurationHolder;
import io.github.namiuni.qshdialog.configuration.configurations.PrimaryConfiguration;
import io.github.namiuni.qshdialog.translation.TranslationMessages;
import io.github.namiuni.qshdialog.translation.TranslatorHolder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translator;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;

@NullMarked
public final class AdminCommand implements QSHCommand {

    private final ConfigurationHolder<PrimaryConfiguration> configHolder;
    private final TranslatorHolder translatorHolder;

    public AdminCommand(
            final ConfigurationHolder<PrimaryConfiguration> configHolder,
            final TranslatorHolder translatorHolder
    ) {
        this.configHolder = configHolder;
        this.translatorHolder = translatorHolder;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("qshdialog")
                .then(Commands.literal("reload")
                        .requires(Commands.restricted(source -> source.getSender().hasPermission(QSHPermissions.RELOAD)))
                        .executes(context -> {
                            final CommandSender commandSender = context.getSource().getSender();

                            switch (this.configHolder.reload()) {
                                case Result.Success<PrimaryConfiguration, ConfigurateException>(PrimaryConfiguration ignored) -> {
                                    final Component message = TranslationMessages.configurationReloadSuccess();
                                    commandSender.sendMessage(message);
                                }
                                case Result.Error<PrimaryConfiguration, ConfigurateException>(ConfigurateException exception) -> {
                                    final Component message = TranslationMessages.configurationReloadError();
                                    commandSender.sendMessage(message);
                                    throw new UncheckedIOException("Failed to reload config!!", exception);
                                }
                            }

                            switch (this.translatorHolder.reload()) {
                                case Result.Success<Translator, IOException>(Translator ignore) -> {
                                    final Component message = TranslationMessages.translationReloadSuccess();
                                    commandSender.sendMessage(message);
                                }
                                case Result.Error<Translator, IOException>(IOException exception) -> {
                                    final Component message = TranslationMessages.translationReloadError();
                                    commandSender.sendMessage(message);
                                    throw new UncheckedIOException("Failed to reload translation!!", exception);
                                }
                            }

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    @Override
    public List<String> aliases() {
        return QSHCommand.super.aliases();
    }

    @Override
    public String description() {
        return QSHCommand.super.description();
    }
}
