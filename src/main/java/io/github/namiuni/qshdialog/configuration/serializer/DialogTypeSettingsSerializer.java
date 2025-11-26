package io.github.namiuni.qshdialog.configuration.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.type.ConfirmationDialogTypeSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.type.DialogTypeSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.type.DialogTypes;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.type.action.DialogActionButtonSettings;
import java.lang.reflect.Type;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
public final class DialogTypeSettingsSerializer implements TypeSerializer<DialogTypeSettings> {

    public static final DialogTypeSettingsSerializer INSTANCE = new DialogTypeSettingsSerializer();

    private static final String TYPE = "type";

    // confirmation
    private static final String YES = "yes";
    private static final String NO = "no";

    @Override
    public DialogTypeSettings deserialize(final Type ignored, final ConfigurationNode source) throws SerializationException {
        final Key type = Objects.requireNonNull(source.node(TYPE).get(Key.class));

        if (Objects.equals(DialogTypes.CONFIRMATION, type)) {
            return new ConfirmationDialogTypeSettings(
                    Objects.requireNonNull(source.node(YES).get(DialogActionButtonSettings.class)),
                    Objects.requireNonNull(source.node(YES).get(DialogActionButtonSettings.class))
            );
        }

        throw new SerializationException();
    }

    @Override
    public void serialize(final Type type, @Nullable final DialogTypeSettings dialogType, final ConfigurationNode source) throws SerializationException {
        if (dialogType != null) {
            source.node(TYPE).set(dialogType.type());

            if (dialogType instanceof ConfirmationDialogTypeSettings(
                    DialogActionButtonSettings yes,
                    DialogActionButtonSettings no
            )) {
                source.node(YES).set(yes);
                source.node(NO).set(no);
            }
        }
    }
}
