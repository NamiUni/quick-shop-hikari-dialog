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
package io.github.namiuni.qshdialog.minecraft.paper;

import com.ghostchu.quickshop.api.shop.interaction.InteractionManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.github.namiuni.qshdialog.minecraft.paper.commands.CommandFactory;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.listener.ShopCreationDialogHandler;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.listener.ShopModificationDialogHandler;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.listener.TradeDialogHandler;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.Set;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class QSHDialogPlugin extends JavaPlugin {

    private final PluginProviderContext context;

    public QSHDialogPlugin(final PluginProviderContext context) {
        this.context = context;
    }

    @Override
    public void onEnable() {
        final QSHDialogModule module = new QSHDialogModule(this.context);
        final Injector injector = Guice.createInjector(module);

        final InteractionManager interactionManager = injector.getInstance(InteractionManager.class);
        interactionManager.behavior(injector.getInstance(ShopCreationDialogHandler.class));
        interactionManager.behavior(injector.getInstance(ShopModificationDialogHandler.class));
        interactionManager.behavior(injector.getInstance(TradeDialogHandler.class));

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Set<CommandFactory> commands = injector.getInstance(Key.get(new TypeLiteral<>() { }));
            commands.forEach(command -> event.registrar().register(command.createCommand(), command.description(), command.aliases()));
        });
    }
}
