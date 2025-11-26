package io.github.namiuni.qshdialog.configuration.configurations.dialog.input;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record SingleOptionDialogInputSettings(
        String key,
        @Nullable @Range(from = 1, to = 1024) Integer width,
        String label,
        @Nullable Boolean labelVisible,
        List<SingleOptionEntrySettings> options
) implements DialogInputSettings {

    @Override
    public Key type() {
        return DialogInputTypes.SINGLE_OPTION;
    }

    public SingleOptionDialogInput createDialogInput(final DialogProviderContext context) {
        final Component renderedLabel = MiniMessage.miniMessage().deserialize(this.label, context.user());

        return DialogInput.singleOption(
                this.key,
                Optional.ofNullable(this.width).orElse(200),
                this.options.stream().map(entry -> entry.createOptionEntry(context)).toList(),
                renderedLabel,
                Optional.ofNullable(this.labelVisible).orElse(true)
        );
    }

    public static final class Builder {

        private final String key;
        private @Nullable @Range(from = 1, to = 1024) Integer width;
        private final String label;
        private @Nullable Boolean labelVisible;
        private List<SingleOptionEntrySettings> options = List.of();

        Builder(final String key, final String label) {
            this.key = key;
            this.label = label;
        }

        public Builder width(final @Nullable @Range(from = 1, to = 1024) Integer width) {
            this.width = width;
            return this;
        }

        public Builder labelVisible(final @Nullable Boolean labelVisible) {
            this.labelVisible = labelVisible;
            return this;
        }

        public Builder options(final List<SingleOptionEntrySettings> options) {
            this.options = options;
            return this;
        }

        public SingleOptionDialogInputSettings build() {
            return new SingleOptionDialogInputSettings(
                    this.key,
                    this.width,
                    this.label,
                    this.labelVisible,
                    this.options
            );
        }
    }
}
