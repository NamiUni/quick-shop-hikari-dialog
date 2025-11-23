package io.github.namiuni.qshdialog.configuration.configurations.dialog.input;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record TextDialogInputSettings(
        String key,
        @Range(from = 1, to = 1024) int width,
        Component label,
        boolean labelVisible,
        String initial,
        @Positive int maxLength,
        TextDialogInput.@Nullable MultilineOptions multilineOptions
) implements DialogInputSettings<TextDialogInput> {

    @Override
    public TextDialogInput createDialogInput(final DialogProviderContext context) {
        final Component renderedLabel = GlobalTranslator.render(this.label, context.owner().locale());
        return DialogInput.text(
                this.key,
                this.width,
                renderedLabel,
                this.labelVisible,
                this.initial,
                this.maxLength,
                this.multilineOptions
        );
    }

    public static final class Builder {

        private final String key;
        private @Range(from = 1, to = 1024) int width = 200;
        private final Component label;
        private boolean labelVisible = true;
        private String initial = "";
        private @Positive int maxLength = 32;
        private TextDialogInput.@Nullable MultilineOptions multilineOptions;

        Builder(final String key, final Component label) {
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
