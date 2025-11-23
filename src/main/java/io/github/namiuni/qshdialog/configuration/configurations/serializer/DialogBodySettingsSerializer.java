package io.github.namiuni.qshdialog.configuration.configurations.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodyTypes;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.ItemDialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.PlainMessageDialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.ItemSettings;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Type;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
public final class DialogBodySettingsSerializer implements TypeSerializer<DialogBodySettings<?>> {

    public static final DialogBodySettingsSerializer INSTANCE = new DialogBodySettingsSerializer();

    // general
    private static final String WIDTH = "width";

    // plain message
    private static final String CONTENTS = "contents";

    // item
    private static final String TYPE = "type";
    private static final String ITEM = "item";
    private static final String DESCRIPTION = "description";
    private static final String SHOW_DECORATION = "show_decoration";
    private static final String SHOW_TOOLTIP = "show_tooltip";
    private static final String HEIGHT = "height";
    private static final int PLAIN_MESSAGE_DEFAULT_WIDTH = 200;
    private static final int ITEM_DEFAULT_WIDTH = 16;
    private static final int DEFAULT_HEIGHT = 16;

    @Override
    public DialogBodySettings<?> deserialize(final Type ignored, final ConfigurationNode source) throws SerializationException {
        final Key type = Objects.requireNonNull(source.node(TYPE).get(Key.class));

        if (type.equals(DialogBodyTypes.PLAIN_MESSAGE)) {
            final int width = source.node(WIDTH).getInt(PLAIN_MESSAGE_DEFAULT_WIDTH);
            final Component contents = Objects.requireNonNull(source.node(CONTENTS).get(Component.class));
            return new PlainMessageDialogBodySettings(contents, width);
        }

        if (type.equals(DialogBodyTypes.ITEM)) {
            final ItemSettings<?> item = Objects.requireNonNull(source.node(ITEM).get(new TypeToken<>() { }));
            final PlainMessageDialogBodySettings description = source.node(DESCRIPTION).get(PlainMessageDialogBodySettings.class);
            final boolean showDecoration = source.node(SHOW_DECORATION).getBoolean(true);
            final boolean showTooltip = source.node(SHOW_TOOLTIP).getBoolean(true);
            final int width = source.node(WIDTH).getInt(ITEM_DEFAULT_WIDTH);
            final int height = source.node(HEIGHT).getInt(DEFAULT_HEIGHT);
            return new ItemDialogBodySettings(
                    item,
                    description,
                    showDecoration,
                    showTooltip,
                    width,
                    height
            );
        }

        throw new SerializationException(source, DialogBodySettings.class, "Invalid body componentType!");
    }

    @Override
    public void serialize(final Type type, @Nullable final DialogBodySettings<?> dialogBody, final ConfigurationNode node) throws SerializationException {
        if (dialogBody != null) {
            if (dialogBody instanceof PlainMessageDialogBodySettings(Component contents, int width)) {
                node.node(TYPE).set(DialogBodyTypes.PLAIN_MESSAGE);
                node.node(CONTENTS).set(contents);
                if (width != PLAIN_MESSAGE_DEFAULT_WIDTH) {
                    node.node(WIDTH).set(width);
                }
            }

            if (dialogBody instanceof ItemDialogBodySettings(
                    ItemSettings<?> item,
                    @Nullable PlainMessageDialogBodySettings description,
                    boolean showDecoration,
                    boolean showTooltip,
                    int width,
                    int height)
            ) {
                node.node(TYPE).set(DialogBodyTypes.ITEM);
                node.node(ITEM).set(item);
                if (description != null) {
                    node.node(DESCRIPTION).set(description);
                }

                if (!showDecoration) {
                    node.node(SHOW_DECORATION).set(showDecoration);
                }

                if (!showTooltip) {
                    node.node(SHOW_TOOLTIP).set(showTooltip);
                }

                if (width != ITEM_DEFAULT_WIDTH) {
                    node.node(WIDTH).set(width);
                }

                if (height != DEFAULT_HEIGHT) {
                    node.node(HEIGHT).set(height);
                }
            }
        }
    }
}
