package io.github.namiuni.qshdialog.configuration.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.BooleanDialogInputSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputTypes;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.NumberRangeDialogInputSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.SingleOptionDialogInputSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.SingleOptionEntrySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.TextDialogInputSettings;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DialogInputSettingsSerializer implements TypeSerializer<DialogInputSettings> {

    public static final DialogInputSettingsSerializer INSTANCE = new DialogInputSettingsSerializer();

    private static final String TYPE = "type";
    private static final String KEY = "key";
    private static final String WIDTH = "width";
    private static final String LABEL = "label";
    private static final String LABEL_VISIBLE = "label_visible";
    private static final String INITIAL = "initial";
    private static final String MAX_LENGTH = "max_length";

    private static final String MULTILINE_OPTIONS = "multiline";

    private static final String OPTIONS = "options";
    private static final String LABEL_FORMAT = "label_format";
    private static final String START = "start";
    private static final String END = "end";
    private static final String STEP = "step";

    private static final String ON_TRUE = "on_true";
    private static final String ON_FALSE = "on_false";

    @Override
    public DialogInputSettings deserialize(final Type ignored, final ConfigurationNode source) throws SerializationException {
        final Key type = Objects.requireNonNull(source.node(TYPE).get(Key.class));

        if (Objects.equals(DialogInputTypes.TEXT, type)) {
            final String key = Objects.requireNonNull(source.node(KEY).getString());
            final Integer width = source.node(WIDTH).get(Integer.class);
            final String label = Objects.requireNonNull(source.node(LABEL).getString());
            final Boolean labelVisible = source.node(LABEL_VISIBLE).get(Boolean.class);
            final String initial = source.node(INITIAL).getString();
            final Integer maxLength = source.node(MAX_LENGTH).get(Integer.class);
            final TextDialogInput.MultilineOptions multilineOptions =
                    source.node(MULTILINE_OPTIONS).get(TextDialogInput.MultilineOptions.class);

            return new TextDialogInputSettings(key, width, label, labelVisible, initial, maxLength, multilineOptions);
        }

        if (Objects.equals(DialogInputTypes.BOOLEAN, type)) {
            final String key = Objects.requireNonNull(source.node(KEY).getString());
            final String label = Objects.requireNonNull(source.node(LABEL).getString());
            final Boolean initial = source.node(INITIAL).get(Boolean.class);
            final String onTrue = source.node(ON_TRUE).getString();
            final String onFalse = source.node(ON_FALSE).getString();

            return new BooleanDialogInputSettings(key, label, initial, onTrue, onFalse);
        }

        if (Objects.equals(DialogInputTypes.SINGLE_OPTION, type)) {
            final String key = Objects.requireNonNull(source.node(KEY).getString());
            final Integer width = source.node(WIDTH).get(Integer.class);
            final String label = Objects.requireNonNull(source.node(LABEL).getString());
            final Boolean labelVisible = source.node(LABEL_VISIBLE).get(Boolean.class);
            final List<SingleOptionEntrySettings> options =
                    Objects.requireNonNull(source.node(OPTIONS).getList(SingleOptionEntrySettings.class));

            return new SingleOptionDialogInputSettings(key, width, label, labelVisible, options);
        }

        if (Objects.equals(DialogInputTypes.NUMBER_RANGE, type)) {
            final String key = Objects.requireNonNull(source.node(KEY).getString());
            final Integer width = source.node(WIDTH).get(Integer.class);
            final String label = Objects.requireNonNull(source.node(LABEL).getString());
            final String labelFormat = source.node(LABEL_FORMAT).getString();
            final String start = Objects.requireNonNull(source.node(START).getString());
            final String end = Objects.requireNonNull(source.node(END).getString());
            final String step = source.node(STEP).getString();
            final String initial = source.node(INITIAL).getString();

            return new NumberRangeDialogInputSettings(key, width, label, labelFormat, start, end, step, initial);
        }

        throw new SerializationException(source, DialogInputSettings.class, "Invalid input type: " + type);
    }

    @Override
    public void serialize(final Type ignored, final @Nullable DialogInputSettings dialogInput, final ConfigurationNode source) throws SerializationException {
        if (dialogInput == null) {
            return;
        }

        source.node(TYPE).set(dialogInput.type());

        if (dialogInput instanceof TextDialogInputSettings(
                String key,
                @Nullable Integer width,
                String label,
                @Nullable Boolean labelVisible,
                @Nullable String initial,
                @Nullable Integer maxLength,
                TextDialogInput.@Nullable MultilineOptions multilineOptions
        )) {
            source.node(KEY).set(key);
            source.node(WIDTH).set(width);
            source.node(LABEL).set(label);
            source.node(LABEL_VISIBLE).set(labelVisible);
            source.node(INITIAL).set(initial);
            source.node(MAX_LENGTH).set(maxLength);
            source.node(MULTILINE_OPTIONS).set(multilineOptions);
        }

        if (dialogInput instanceof BooleanDialogInputSettings(
                String key,
                String label,
                @Nullable Boolean initial,
                @Nullable String onTrue,
                @Nullable String onFalse
        )) {
            source.node(KEY).set(key);
            source.node(LABEL).set(label);
            source.node(INITIAL).set(initial);
            source.node(ON_TRUE).set(onTrue);
            source.node(ON_FALSE).set(onFalse);
        }

        if (dialogInput instanceof SingleOptionDialogInputSettings(
                String key,
                @Nullable Integer width,
                String label,
                @Nullable Boolean labelVisible,
                List<SingleOptionEntrySettings> options
        )) {
            source.node(KEY).set(key);
            source.node(WIDTH).set(width);
            source.node(LABEL).set(label);
            source.node(LABEL_VISIBLE).set(labelVisible);
            source.node(OPTIONS).setList(SingleOptionEntrySettings.class, options);
        }

        if (dialogInput instanceof NumberRangeDialogInputSettings(
                String key,
                @Nullable Integer width,
                String label,
                @Nullable String labelFormat,
                String start,
                String end,
                @Nullable String step,
                @Nullable String initial
        )) {
            source.node(KEY).set(key);
            source.node(WIDTH).set(width);
            source.node(LABEL).set(label);
            source.node(LABEL_FORMAT).set(labelFormat);
            source.node(START).set(start);
            source.node(END).set(end);
            source.node(STEP).set(step);
            source.node(INITIAL).set(initial);
        }
    }
}
