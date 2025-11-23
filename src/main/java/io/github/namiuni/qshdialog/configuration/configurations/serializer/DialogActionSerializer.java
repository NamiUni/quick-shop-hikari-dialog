package io.github.namiuni.qshdialog.configuration.configurations.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.action.DialogActionTypes;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Objects;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DialogActionSerializer implements TypeSerializer<DialogAction> {

    public static final DialogActionSerializer INSTANCE = new DialogActionSerializer();

    private static final String TYPE = "type";
    private static final String URL = "url";
    private static final String COMMAND = "command";
    private static final String PAGE = "page";
    private static final String VALUE = "value";
    private static final String DIALOG = "dialog";
    private static final String PAYLOAD = "payload";
    private static final String ID = "id";
    private static final String TEMPLATE = "template";
    private static final String ADDITIONS = "additions";

    @Override
    public DialogAction deserialize(final Type ignored, final ConfigurationNode source) throws SerializationException {
        final Key type = source.node(TYPE).get(Key.class);

        if (Objects.equals(DialogActionTypes.OPEN_URL, type)) {
            final URL url = Objects.requireNonNull(source.node(URL).get(URL.class));
            return DialogAction.staticAction(ClickEvent.openUrl(url));
        }

        if (Objects.equals(DialogActionTypes.RUN_COMMAND, type)) {
            final String command = source.node(COMMAND).getString("");
            return DialogAction.staticAction(ClickEvent.runCommand(command));
        }

        if (Objects.equals(DialogActionTypes.SUGGEST_COMMAND, type)) {
            final String command = source.node(COMMAND).getString("");
            return DialogAction.staticAction(ClickEvent.suggestCommand(command));
        }

        if (Objects.equals(DialogActionTypes.CHANGE_PAGE, type)) {
            final int page = source.node(PAGE).getInt();
            return DialogAction.staticAction(ClickEvent.changePage(page));
        }

        if (Objects.equals(DialogActionTypes.COPY_TO_CLIPBOARD, type)) {
            final String value = source.node(VALUE).getString("");
            return DialogAction.staticAction(ClickEvent.copyToClipboard(value));
        }

        if (Objects.equals(DialogActionTypes.SHOW_DIALOG, type)) {
            final Key dialogType = source.node(DIALOG).get(Key.class);
            if (dialogType != null) {
                final Dialog dialog = RegistryAccess.registryAccess()
                        .getRegistry(RegistryKey.DIALOG)
                        .getOrThrow(dialogType);
                return DialogAction.staticAction(ClickEvent.showDialog(dialog));
            }

            final DialogLike dialog = Objects.requireNonNull(source.node(DIALOG).get(Dialog.class));
            return DialogAction.staticAction(ClickEvent.showDialog(dialog));
        }

        if (Objects.equals(DialogActionTypes.CUSTOM, type)) {
            final Key id = Objects.requireNonNull(source.node(ID).get(Key.class));
            final BinaryTagHolder payload = Objects.requireNonNull(source.node(PAYLOAD).get(BinaryTagHolder.class));
            return DialogAction.staticAction(ClickEvent.custom(id, payload));
        }

        if (Objects.equals(DialogActionTypes.DYNAMIC_RUN_COMMAND, type)) {
            final String command = source.node(TEMPLATE).getString("");
            return DialogAction.commandTemplate(command);
        }

        if (Objects.equals(DialogActionTypes.DYNAMIC_CUSTOM, type)) {
            final Key id = Objects.requireNonNull(source.node(ID).get(Key.class));
            final BinaryTagHolder additions = Objects.requireNonNull(source.node(ADDITIONS).get(BinaryTagHolder.class));
            return DialogAction.customClick(id, additions);
        }

        throw new SerializationException();
    }

    @Override
    public void serialize(final Type type, @Nullable final DialogAction dialogAction, final ConfigurationNode source) throws SerializationException {
        // TODO
    }
}
