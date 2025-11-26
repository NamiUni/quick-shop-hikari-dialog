package io.github.namiuni.qshdialog.configuration.configurations.dialog.input;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record TextDialogInputSettings(
        String key,
        @Nullable @Range(from = 1, to = 1024) Integer width,
        String label,
        @Nullable Boolean labelVisible,
        @Nullable String initial,
        @Nullable @Positive Integer maxLength,
        TextDialogInput.@Nullable MultilineOptions multilineOptions
) implements DialogInputSettings {

    @Override
    public Key type() {
        return DialogInputTypes.TEXT;
    }

    @Override
    public TextDialogInput createDialogInput(final DialogProviderContext context) {
        final Component renderedLabel = MiniMessage.miniMessage().deserialize(this.label, context.user());
        final TextDialogInput.Builder builder = DialogInput.text(this.key, renderedLabel);

        Optional.ofNullable(this.width)
                .ifPresent(builder::width);

        Optional.ofNullable(this.labelVisible)
                .ifPresent(builder::labelVisible);

        Optional.ofNullable(this.initial)
                .ifPresent(builder::initial);

        Optional.ofNullable(this.maxLength)
                .ifPresent(builder::maxLength);

        builder.multiline(this.multilineOptions);

        return builder.build();
    }

    public static final class Builder {

        private final String key;
        private @Nullable @Range(from = 1, to = 1024) Integer width = null;
        private final String label;
        private @Nullable Boolean labelVisible = null;
        private @Nullable String initial = null;
        private @Nullable @Positive Integer maxLength = null;
        private TextDialogInput.@Nullable MultilineOptions multilineOptions;

        Builder(final String key, final String label) {
            this.key = key;
            this.label = label;
        }

        public Builder width(final @Range(from = 1, to = 1024) int width) {
            this.width = width;
            return this;
        }

        public Builder labelVisible(final boolean labelVisible) {
            this.labelVisible = labelVisible;
            return this;
        }

        public Builder initial(final String initial) {
            this.initial = initial;
            return this;
        }

        public Builder maxLength(final @Positive int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public Builder multilineOptions(final TextDialogInput.@Nullable MultilineOptions multilineOptions) {
            this.multilineOptions = multilineOptions;
            return this;
        }

        public TextDialogInputSettings build() {
            return new TextDialogInputSettings(
                    this.key,
                    this.width,
                    this.label,
                    this.labelVisible,
                    this.initial,
                    this.maxLength,
                    this.multilineOptions
            );
        }
    }
}
