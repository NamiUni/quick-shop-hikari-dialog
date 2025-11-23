package io.github.namiuni.qshdialog.configuration.configurations.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.ConfirmationDialogConfiguration;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.DialogActionButtonSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.DialogConfiguration;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputSettings;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.registry.data.dialog.DialogBase;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class QSHDialogSerializer implements TypeSerializer<DialogConfiguration> {

    public static final QSHDialogSerializer INSTANCE = new QSHDialogSerializer();

    // General
    private static final Key CONFIRMATION = Key.key("confirmation");
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private static final String EXTERNAL_TITLE = "external_title";
    private static final String BODY = "body";
    private static final String INPUTS = "inputs";
    private static final String CAN_CLOSE_WITH_ESCAPE = "can_close_with_escape";
    private static final String PAUSE = "pause";
    private static final String AFTER_ACTION = "after_action";

    // Confirmation
    private static final String YES = "yes";
    private static final String NO = "no";

    @Override
    public DialogConfiguration deserialize(final Type qshDialogType, final ConfigurationNode source) throws SerializationException {
        final Key type = Objects.requireNonNull(source.node(TYPE).get(Key.class));

        final Component title = source.node(TITLE).get(Component.class);
        final Component externalTitle = source.node(EXTERNAL_TITLE).get(Component.class);
        final List<DialogBodySettings<?>> body = source.node(BODY).getList(new TypeToken<>() { });
        final List<DialogInputSettings<?>> inputs = source.node(INPUTS).getList(new TypeToken<>() { });
        final boolean canCloseWithEscape = source.node(CAN_CLOSE_WITH_ESCAPE).getBoolean(true);
        final boolean pause = source.node(PAUSE).getBoolean(true);
        final DialogBase.DialogAfterAction afterAction = source.node(AFTER_ACTION).get(DialogBase.DialogAfterAction.class);

        if (type.equals(CONFIRMATION)) {
            final DialogActionButtonSettings yes = source.node(YES).get(DialogActionButtonSettings.class);
            final DialogActionButtonSettings no = source.node(NO).get(DialogActionButtonSettings.class);
            return new ConfirmationDialogConfiguration(
                    Objects.requireNonNull(title),
                    externalTitle,
                    body == null ? List.of() : body,
                    inputs == null ? List.of() : inputs,
                    canCloseWithEscape,
                    pause,
                    afterAction == null ? DialogBase.DialogAfterAction.CLOSE : afterAction,
                    Objects.requireNonNull(yes),
                    Objects.requireNonNull(no)
            );
        }

        throw new SerializationException(source, DialogBodySettings.class, "Invalid dialog componentType!: %s".formatted(type));
    }

    @Override
    public void serialize(final Type type, @Nullable final DialogConfiguration dialogConfiguration, final ConfigurationNode source) throws SerializationException {
        if (dialogConfiguration != null) {
            if (dialogConfiguration instanceof ConfirmationDialogConfiguration) {
                source.node(TYPE).set(CONFIRMATION);
            }

            source.node(TITLE).set(dialogConfiguration.title());
            source.node(EXTERNAL_TITLE).set(dialogConfiguration.externalTitle());
            source.node(BODY).setList(new TypeToken<>() { }, dialogConfiguration.body());
            source.node(INPUTS).setList(new TypeToken<>() { }, dialogConfiguration.inputs());
            if (!dialogConfiguration.canCloseWithEscape()) {
                source.node(CAN_CLOSE_WITH_ESCAPE).set(dialogConfiguration.canCloseWithEscape());
            }
            if (!dialogConfiguration.pause()) {
                source.node(PAUSE).set(dialogConfiguration.pause());
            }

            source.node(AFTER_ACTION).set(dialogConfiguration.afterAction());

            if (dialogConfiguration instanceof ConfirmationDialogConfiguration confirmation) {
                source.node(YES).set(confirmation.yes());
                source.node(NO).set(confirmation.no());
            }
        }
    }
}
