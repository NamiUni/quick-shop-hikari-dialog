package io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.kotonoha;

import io.github.namiuni.kotonoha.translatable.message.context.InvocationContext;
import io.github.namiuni.kotonoha.translatable.message.policy.KotonohaValidationException;
import io.github.namiuni.kotonoha.translatable.message.policy.result.CustomResultComponentTransformationPolicy;
import io.github.namiuni.kotonoha.translatable.message.utility.ComponentTransformer;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ResultPolicy implements CustomResultComponentTransformationPolicy {

    private final ComponentTransformer componentTransformer;
    private final Provider<Translator> translator;

    @Inject
    ResultPolicy(
            final ComponentTransformer componentTransformer,
            final Provider<Translator> translator
    ) {
        this.componentTransformer = componentTransformer;
        this.translator = translator;
    }

    @Override
    public @UnknownNullability Object transformComponent(final TranslatableComponent component, final InvocationContext context) {
        final Method method = context.method();
        final Type genericReturnType = method.getGenericReturnType();

        if (context.invocationArguments()[0].value() instanceof Pointered pointered) {
            final Locale locale = pointered.getOrDefault(Identity.LOCALE, Locale.ROOT);
            final Component translated = this.translator.get().translate(component, locale);
            return this.componentTransformer.transform(genericReturnType, Objects.requireNonNull(translated));
        }

        throw new IllegalArgumentException();
    }

    @Override
    public void validate(final Method method) throws KotonohaValidationException {
    }
}
