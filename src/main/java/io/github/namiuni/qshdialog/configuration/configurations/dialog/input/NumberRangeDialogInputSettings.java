package io.github.namiuni.qshdialog.configuration.configurations.dialog.input;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.NumberRangeDialogInput;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record NumberRangeDialogInputSettings(
        String key,
        @Nullable @Range(from = 1, to = 1024) Integer width,
        String label,
        @Nullable String labelFormat,
        String start,
        String end,
        @Nullable String step,
        @Nullable String initial
) implements DialogInputSettings {

    @Override
    public Key type() {
        return DialogInputTypes.NUMBER_RANGE;
    }

    @Override
    public NumberRangeDialogInput createDialogInput(final DialogProviderContext context) {
        final Component renderedLabel = MiniMessage.miniMessage().deserialize(this.label, context.user());

        final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        final float formattedStart = serializer
                .serialize(MiniMessage.miniMessage().deserialize(this.start, context.user(), context.tagResolver()))
                .transform(Float::parseFloat);
        final float formattedEnd = serializer
                .serialize(MiniMessage.miniMessage().deserialize(this.end, context.user(), context.tagResolver()))
                .transform(Float::parseFloat);

        final NumberRangeDialogInput.Builder builder = DialogInput
                .numberRange(
                        this.key,
                        renderedLabel,
                        formattedStart,
                        formattedEnd
                );

        Optional.ofNullable(this.labelFormat)
                .map(format -> MiniMessage.miniMessage().deserialize(format, context.user(), context.tagResolver()))
                .map(PlainTextComponentSerializer.plainText()::serialize)
                .ifPresent(builder::labelFormat);

        Optional.ofNullable(this.width)
                .ifPresent(builder::width);

        Optional.ofNullable(this.labelFormat)
                .map(format -> MiniMessage.miniMessage().deserialize(format, context.user(), context.tagResolver()))
                .map(PlainTextComponentSerializer.plainText()::serialize)
                .ifPresent(builder::labelFormat);

        return builder.build();
    }

    public static class Builder {

        private final String key;
        private final String label;
        private final String start;
        private final String end;

        private @Nullable @Range(from = 1, to = 1024) Integer width = null;
        private @Nullable String labelFormat = null;
        private @Nullable String step = null;
        private @Nullable String initial = null;

        Builder(final String key, final String label, final String start, final String end) {
            this.key = key;
            this.label = label;
            this.start = start;
            this.end = end;
        }

        public Builder width(final @Range(from = 1, to = 1024) int width) {
            this.width = width;
            return this;
        }

        public Builder labelFormat(final @Nullable String labelFormat) {
            this.labelFormat = labelFormat;
            return this;
        }

        public Builder step(final @Nullable String step) {
            this.step = step;
            return this;
        }

        public Builder initial(final @Nullable String initial) {
            this.initial = initial;
            return this;
        }

        public NumberRangeDialogInputSettings build() {
            return new NumberRangeDialogInputSettings(
                    this.key,
                    this.width,
                    this.label,
                    this.labelFormat,
                    this.start,
                    this.end,
                    this.step,
                    this.initial
            );
        }
    }
}
