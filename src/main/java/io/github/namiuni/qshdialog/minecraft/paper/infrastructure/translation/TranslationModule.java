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
package io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import io.github.namiuni.kotonoha.translatable.message.KotonohaMessage;
import io.github.namiuni.kotonoha.translatable.message.configuration.InvocationConfiguration;
import io.github.namiuni.kotonoha.translatable.message.policy.argument.TranslationArgumentAdaptationPolicy;
import io.github.namiuni.kotonoha.translatable.message.policy.key.TranslationKeyResolutionPolicy;
import io.github.namiuni.kotonoha.translatable.message.policy.result.ResultComponentTransformationPolicy;
import io.github.namiuni.kotonoha.translatable.message.utility.ComponentTransformer;
import io.github.namiuni.kotonoha.translatable.message.utility.TranslationArgumentAdapter;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.Reloadable;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.kotonoha.ArgumentPolicy;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.kotonoha.ResultPolicy;
import io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.translations.TranslationService;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslationModule extends AbstractModule {

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    TranslationArgumentAdapter argumentAdapter() {
        return TranslationArgumentAdapter.standard()
                .toBuilder()
                .argument(BigDecimal.class, TranslationArgument::numeric)
                .build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    ComponentTransformer componentTransformer() {
        return ComponentTransformer.builder()
                .register(String.class, PlainTextComponentSerializer.plainText()::serialize)
                .register(Component.class, Function.identity())
                .build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    TranslationService translationService(
            final TranslationArgumentAdaptationPolicy argumentPolicy,
            final ResultComponentTransformationPolicy resultPolicy
    ) {
        final var config = InvocationConfiguration.of(
                TranslationKeyResolutionPolicy.annotationKeyResolutionPolicy(),
                argumentPolicy,
                resultPolicy
        );
        return KotonohaMessage.createProxy(TranslationService.class, config);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    MiniMessage miniMessage() {
        final TagResolver jisSafetyColors = TagResolver.builder()
                .tag("jis_red", Tag.styling(TextColor.color(0xFF4B00)))
                .tag("jis_orange", Tag.styling(TextColor.color(0xF6AA00)))
                .tag("jis_yellow", Tag.styling(TextColor.color(0xF2E700)))
                .tag("jis_green", Tag.styling(TextColor.color(0x00B06B)))
                .tag("jis_blue", Tag.styling(TextColor.color(0x1971FF)))
                .tag("jis_purple", Tag.styling(TextColor.color(0x990099)))
                .build();
        return MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(TagResolver.standard())
                        .resolver(jisSafetyColors)
                        .build()
                )
                .build();
    }

    @Override
    protected void configure() {
        this.bind(TranslationArgumentAdaptationPolicy.class).to(ArgumentPolicy.class).in(Scopes.SINGLETON);
        this.bind(ResultComponentTransformationPolicy.class).to(ResultPolicy.class).in(Scopes.SINGLETON);

        this.bind(TranslatorHolder.class).asEagerSingleton();
        this.bind(Translator.class).toProvider(new TypeLiteral<TranslatorHolder>() { });
        this.bind(new TypeLiteral<Reloadable<Translator>>() { })
                .to(new TypeLiteral<TranslatorHolder>() { });
    }
}
