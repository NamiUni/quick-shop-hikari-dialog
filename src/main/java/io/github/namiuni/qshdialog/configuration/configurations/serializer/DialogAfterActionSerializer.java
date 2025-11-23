package io.github.namiuni.qshdialog.configuration.configurations.serializer;

import io.papermc.paper.registry.data.dialog.DialogBase;
import java.lang.reflect.Type;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DialogAfterActionSerializer implements TypeSerializer<DialogBase.DialogAfterAction> {

    public static final DialogAfterActionSerializer INSTANCE = new DialogAfterActionSerializer();

    @Override
    public DialogBase.DialogAfterAction deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final DialogBase.DialogAfterAction afterAction = node.get(DialogBase.DialogAfterAction.class);

        return Objects.requireNonNullElse(afterAction, DialogBase.DialogAfterAction.CLOSE);
    }

    @Override
    public void serialize(final Type type, final DialogBase.@Nullable DialogAfterAction afterAction, final ConfigurationNode node) throws SerializationException {
        if (afterAction != null && afterAction != DialogBase.DialogAfterAction.CLOSE) {
            node.set(afterAction.name().toLowerCase());
        }
    }
}
