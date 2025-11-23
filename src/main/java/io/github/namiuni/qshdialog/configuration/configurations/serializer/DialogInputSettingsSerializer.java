package io.github.namiuni.qshdialog.configuration.configurations.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputTypes;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.TextDialogInputSettings;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import java.lang.reflect.Type;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DialogInputSettingsSerializer implements TypeSerializer<DialogInputSettings<?>> {

    public static final DialogInputSettingsSerializer INSTANCE = new DialogInputSettingsSerializer();

    private static final String TYPE = "type";
    private static final String KEY = "key";
    private static final String WIDTH = "width";
    private static final String LABEL = "label";
    private static final String LABEL_VISIBLE = "label_visible";
    private static final String INITIAL = "initial";
    private static final String MAX_LENGTH = "max_length";
    private static final String MULTILINE_OPTIONS = "multiline";

    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_MAX_LENGTH = 32;

    @Override
    public DialogInputSettings<?> deserialize(final Type ignored, final ConfigurationNode source) throws SerializationException {
        final Key type = Objects.requireNonNull(source.node(TYPE).get(Key.class));

        if (type.equals(DialogInputTypes.TEXT)) {
            final String key = Objects.requireNonNull(source.node(KEY).getString());
            final @Range(from = 1, to = 1024) int width = source.node(WIDTH).getInt(DEFAULT_WIDTH);
            final Component label = source.node(LABEL).get(Component.class, Component.empty());
            final boolean labelVisible = source.node(LABEL_VISIBLE).getBoolean(true);
            final String initial = source.node(INITIAL).getString("");
            final @Positive int maxLength = source.node(MAX_LENGTH).getInt(DEFAULT_MAX_LENGTH);
            final TextDialogInput.@Nullable MultilineOptions multilineOptions = source.node(MULTILINE_OPTIONS).get(TextDialogInput.MultilineOptions.class);
            return new TextDialogInputSettings(
                    key,
                    width,
                    label,
                    labelVisible,
                    initial,
                    maxLength,
                    multilineOptions
            );
        }

        throw new SerializationException();
    }

    @Override
    public void serialize(final Type type, final @Nullable DialogInputSettings<?> dialogInput, final ConfigurationNode source) throws SerializationException {
        if (dialogInput instanceof TextDialogInputSettings(
                String key,
                int width,
                Component label,
                boolean labelVisible,
                String initial,
                int maxLength,
                TextDialogInput.MultilineOptions multilineOptions)
        ) {
            source.node(TYPE).set(DialogInputTypes.TEXT);
            source.node(KEY).set(key);
            if (width != DEFAULT_WIDTH) {
                source.node(WIDTH).set(width);
            }
            source.node(LABEL).set(label);
            if (!labelVisible) {
                source.node(LABEL_VISIBLE).set(labelVisible);
            }
            source.node(INITIAL).set(initial);
            if (maxLength != DEFAULT_MAX_LENGTH) {
                source.node(MAX_LENGTH).set(maxLength);
            }
            source.node(MULTILINE_OPTIONS).set(multilineOptions);
        }
    }
}
