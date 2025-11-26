package io.github.namiuni.qshdialog.configuration.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.QSHDialogItemType;
import java.lang.reflect.Type;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
public final class QSHDialogItemTypeSerializer implements TypeSerializer<QSHDialogItemType> {

    public static final QSHDialogItemTypeSerializer INSTANCE = new QSHDialogItemTypeSerializer();

    private QSHDialogItemTypeSerializer() {
    }

    @Override
    public QSHDialogItemType deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final Key key = Objects.requireNonNull(node.get(Key.class));
        return QSHDialogItemType.of(key);
    }

    @Override
    public void serialize(final Type type, @Nullable final QSHDialogItemType itemType, final ConfigurationNode node) throws SerializationException {
        if (itemType != null) {
             node.set(itemType.key());
        }
    }
}
