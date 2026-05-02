package io.github.namiuni.qshdialog.minecraft.paper.infrastructure.translation.kotonoha;

import io.github.namiuni.kotonoha.translatable.message.context.InvocationArgument;
import io.github.namiuni.kotonoha.translatable.message.context.InvocationContext;
import io.github.namiuni.kotonoha.translatable.message.policy.KotonohaValidationException;
import io.github.namiuni.kotonoha.translatable.message.policy.argument.TranslationArgumentAdaptationPolicy;
import io.github.namiuni.kotonoha.translatable.message.policy.argument.tag.TagNameResolver;
import io.github.namiuni.kotonoha.translatable.message.utility.TranslationArgumentAdapter;
import io.github.namiuni.qshdialog.minecraft.paper.integration.miniplaceholders.MiniPlaceholdersExtension;
import io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.QSPlaceholders;
import jakarta.inject.Inject;
import java.lang.reflect.Method;
import java.util.Arrays;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ArgumentPolicy implements TranslationArgumentAdaptationPolicy {

    private final TranslationArgumentAdaptationPolicy minimessagePolicy;
    private final QSPlaceholders qsPlaceholders;

    @Inject
    ArgumentPolicy(
            final TranslationArgumentAdapter argumentAdapter,
            final QSPlaceholders qsPlaceholders
    ) {
        this.minimessagePolicy = TranslationArgumentAdaptationPolicy.miniMessage(
                argumentAdapter,
                TagNameResolver.annotationOrParameterNameResolver()
        );
        this.qsPlaceholders = qsPlaceholders;
    }

    @Override
    public ComponentLike[] adaptArgumentArray(final InvocationContext context) throws IllegalArgumentException, NullPointerException {
        final ComponentLike[] arguments = this.minimessagePolicy.adaptArgumentArray(context);
        final ComponentLike[] placeholdersIncludedArguments = Arrays.copyOf(arguments, arguments.length + 3);
        placeholdersIncludedArguments[arguments.length] = Argument.tagResolver(MiniPlaceholdersExtension.audienceGlobalPlaceholders());
        placeholdersIncludedArguments[arguments.length + 1] = Argument.tagResolver(this.qsPlaceholders.audiencePlaceholders());
        placeholdersIncludedArguments[arguments.length + 2] = Argument.tagResolver(this.qsPlaceholders.globalPlaceholders());

        return placeholdersIncludedArguments;
    }

    @Override
    public ComponentLike adaptArgument(final InvocationArgument invocationArgument) throws IllegalArgumentException, NullPointerException {
        return this.minimessagePolicy.adaptArgument(invocationArgument);
    }

    @Override
    public void validate(final Method method) throws KotonohaValidationException {
        this.minimessagePolicy.validate(method);
    }
}
