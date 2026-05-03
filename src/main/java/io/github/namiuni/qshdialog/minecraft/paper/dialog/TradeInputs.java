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
package io.github.namiuni.qshdialog.minecraft.paper.dialog;

import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.user.UserSession;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class TradeInputs {

    private final TranslationService translations;

    @Inject
    TradeInputs(final TranslationService translations) {
        this.translations = translations;
    }

    public DialogInput tradeQuantity(final int max, final int initial, final UserSession target, final TagResolver placeholders) {
        final Component label = this.translations.dialogTradeInputQuantity(target, placeholders);
        final String format = this.translations.dialogTradeInputQuantityFormat(target, placeholders);
        return DialogInput.numberRange(DialogInputKeys.TRADE_QUANTITY, label, 1.0f, max)
                .step(1.0f)
                .initial((float) initial)
                .labelFormat(format)
                .build();
    }
}
