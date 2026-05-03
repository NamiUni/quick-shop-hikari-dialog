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

import com.ghostchu.quickshop.QuickShop;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import io.github.namiuni.qshdialog.minecraft.paper.commands.AdminCommand;
import io.github.namiuni.qshdialog.minecraft.paper.commands.CommandFactory;
import io.github.namiuni.qshdialog.minecraft.paper.commands.ShopCommand;
import io.github.namiuni.qshdialog.minecraft.paper.commands.TradeCommand;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.InfrastructureModule;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QuickShopModule;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class QSHDialogModule extends AbstractModule {

    private final PluginProviderContext context;

    public QSHDialogModule(final PluginProviderContext context) {
        this.context = context;
    }

    @Override
    protected void configure() {
        final var infrastructure = new InfrastructureModule(
                this.context.getLogger(),
                this.context.getDataDirectory(),
                this.context.getPluginSource()
        );
        this.install(infrastructure);

        final var quickShop = new QuickShopModule(QuickShop.getInstance());
        this.install(quickShop);

        this.bindCommands();
    }

    private void bindCommands() {
        final Multibinder<CommandFactory> commands = Multibinder.newSetBinder(this.binder(), CommandFactory.class);
        commands.addBinding().to(AdminCommand.class).in(Scopes.SINGLETON);
        commands.addBinding().to(ShopCommand.class).in(Scopes.SINGLETON);
        commands.addBinding().to(TradeCommand.class).in(Scopes.SINGLETON);
    }
}
