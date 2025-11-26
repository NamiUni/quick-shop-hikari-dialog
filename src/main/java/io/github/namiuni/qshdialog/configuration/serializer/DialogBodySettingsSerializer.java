package io.github.namiuni.qshdialog.configuration.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodyTypes;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.ItemDialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.PlainMessageDialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.ItemSettings;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Type;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
public final class DialogBodySettingsSerializer implements TypeSerializer<DialogBodySettings> {

    public static final DialogBodySettingsSerializer INSTANCE = new DialogBodySettingsSerializer();

    // general
    private static final String TYPE = "type";
    private static final String WIDTH = "width";

    // plain message
    private static final String CONTENTS = "contents";

    // item
    private static final String ITEM = "item";
    private static final String DESCRIPTION = "description";
    private static final String SHOW_DECORATION = "show_decoration";
    private static final String SHOW_TOOLTIP = "show_tooltip";
    private static final String HEIGHT = "height";

    @Override
    public DialogBodySettings deserialize(final Type ignored, final ConfigurationNode source) throws SerializationException {
        final Key type = Objects.requireNonNull(source.node(TYPE).get(Key.class));

        if (Objects.equals(DialogBodyTypes.PLAIN_MESSAGE, type)) {
            final String contents = Objects.requireNonNull(source.node(CONTENTS).getString());
            final Integer width = source.node(WIDTH).get(Integer.class);
            return new PlainMessageDialogBodySettings(contents, width);
        }

        if (Objects.equals(DialogBodyTypes.ITEM, type)) {
            final ItemSettings<?> item = Objects.requireNonNull(source.node(ITEM).get(new TypeToken<>() { }));

            final PlainMessageDialogBodySettings description;
            final ConfigurationNode descNode = source.node(DESCRIPTION);
            if (!descNode.virtual()) {
                final String descContents = Objects.requireNonNull(descNode.node(CONTENTS).getString());
                final Integer descWidth = descNode.node(WIDTH).get(Integer.class);
                description = new PlainMessageDialogBodySettings(descContents, descWidth);
            } else {
                description = null;
            }

            final Boolean showDecoration = source.node(SHOW_DECORATION).get(Boolean.class);
            final Boolean showTooltip = source.node(SHOW_TOOLTIP).get(Boolean.class);
            final Integer width = source.node(WIDTH).get(Integer.class);
            final Integer height = source.node(HEIGHT).get(Integer.class);

            return new ItemDialogBodySettings(item, description, showDecoration, showTooltip, width, height);
        }

        throw new SerializationException(source, DialogBodySettings.class, "Invalid body componentType: " + type);
    }

    @Override
    public void serialize(final Type ignore, @Nullable final DialogBodySettings dialogBody, final ConfigurationNode node) throws SerializationException {
        if (dialogBody != null) {
            node.node(TYPE).set(dialogBody.type());

            if (dialogBody instanceof PlainMessageDialogBodySettings(
                    String contents,
                    @Nullable Integer width)
            ) {
                node.node(CONTENTS).set(contents);
                node.node(WIDTH).set(width);
            }

            if (dialogBody instanceof ItemDialogBodySettings(
                    ItemSettings<?> item,
                    @Nullable PlainMessageDialogBodySettings description,
                    @Nullable Boolean showDecoration,
                    @Nullable Boolean showTooltip,
                    @Nullable Integer width,
                    @Nullable Integer height)
            ) {
                node.node(ITEM).set(item);

                if (description != null) {
                    final ConfigurationNode descNode = node.node(DESCRIPTION);
                    descNode.node(CONTENTS).set(description.contents());
                    descNode.node(WIDTH).set(description.width());
                }

                node.node(SHOW_DECORATION).set(showDecoration);
                node.node(SHOW_TOOLTIP).set(showTooltip);
                node.node(WIDTH).set(width);
                node.node(HEIGHT).set(height);
            }
        }
    }
}
