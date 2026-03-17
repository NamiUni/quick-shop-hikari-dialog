package io.github.namiuni.qshdialog.minecraft.paper.configuration.serializer;

import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.ShopInputType;
import java.lang.reflect.Type;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
public final class ShopInputTypeSerializer implements TypeSerializer<ShopInputType> {

    public static final ShopInputTypeSerializer INSTANCE = new ShopInputTypeSerializer();

    @Override
    public ShopInputType deserialize(final Type type, final ConfigurationNode node) {
        final String nodeString = Objects.requireNonNull(node.getString());
        return ShopInputType.of(nodeString);
    }

    @Override
    public void serialize(final Type type, @Nullable final ShopInputType obj, final ConfigurationNode node) throws SerializationException {
        if (obj != null) {
            node.set(obj.toString());
        }
    }
}
