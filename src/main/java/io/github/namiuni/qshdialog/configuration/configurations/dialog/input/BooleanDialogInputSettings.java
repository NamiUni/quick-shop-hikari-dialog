package io.github.namiuni.qshdialog.configuration.configurations.dialog.input;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.input.BooleanDialogInput;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record BooleanDialogInputSettings(
        String key,
        String label,
        @Nullable Boolean initial,
        @Nullable String onTrue,
        @Nullable String onFalse
) implements DialogInputSettings {

    @Override
    public Key type() {
        return DialogInputTypes.BOOLEAN;
    }

    @Override
    public BooleanDialogInput createDialogInput(final DialogProviderContext context) {
        final Component renderedLabel = MiniMessage.miniMessage().deserialize(this.label, context.user());
        final BooleanDialogInput.Builder builder = DialogInput.bool(this.key, renderedLabel);

        Optional.ofNullable(this.initial)
                .ifPresent(builder::initial);

        Optional.ofNullable(this.onTrue)
                .ifPresent(builder::onTrue);

        Optional.ofNullable(this.onFalse)
                .ifPresent(builder::onFalse);

        return builder.build();
    }

    public static class Builder {

        private final String key;
        private final String label;
        private @Nullable Boolean initial = null;
        private @Nullable String onTrue = null;
        private @Nullable String onFalse = null;

        Builder(final String key, final String label) {
            this.key = key;
            this.label = label;
        }

        public Builder initial(final @Nullable Boolean initial) {
            this.initial = initial;
            return this;
        }

        public Builder onTrue(final @Nullable String onTrue) {
            this.onTrue = onTrue;
            return this;
        }

        public Builder onFalse(final @Nullable String onFalse) {
            this.onFalse = onFalse;
            return this;
        }

        public BooleanDialogInputSettings build() {
            return new BooleanDialogInputSettings(
                    this.key,
                    this.label,
                    this.initial,
                    this.onTrue,
                    this.onFalse
            );
        }
    }
}
