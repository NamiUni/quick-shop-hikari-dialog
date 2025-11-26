package io.github.namiuni.qshdialog.configuration.serializer;

import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import java.lang.reflect.Type;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class TextDialogInputMultilineOptionsSerializer implements TypeSerializer<TextDialogInput.MultilineOptions> {

    public static final TextDialogInputMultilineOptionsSerializer INSTANCE = new TextDialogInputMultilineOptionsSerializer();

    private static final String MAX_LINES = "max_lines";
    private static final String HEIGHT = "height";

    @Override
    public TextDialogInput.MultilineOptions deserialize(final Type type, final ConfigurationNode source) throws SerializationException {
        final ConfigurationNode maxLinesNode = source.node(MAX_LINES);
        final @Positive Integer maxLines = source.hasChild(MAX_LINES) ? maxLinesNode.getInt() : null;
        if (maxLines != null && maxLines < 1) {
            throw new SerializationException(maxLinesNode, Integer.class, "The value of max-lines must be 1 or greater!");
        }

        final ConfigurationNode heightNode = source.node(HEIGHT);
        final @Range(from = 1, to = 512) Integer height = source.hasChild(HEIGHT) ? heightNode.getInt() : null;
        if (height != null && (height < 1 || 512 < height)) {
            throw new SerializationException(heightNode, Integer.class, "The value of height must be between 1 and 512!");
        }

        return TextDialogInput.MultilineOptions.create(maxLines, height);
    }

    @Override
    public void serialize(final Type type, final TextDialogInput.@Nullable MultilineOptions obj, final ConfigurationNode node) throws SerializationException {
        if (obj != null) {
            node.node(MAX_LINES).set(obj.maxLines());
            node.node(HEIGHT).set(obj.height());
        }
    }
}
