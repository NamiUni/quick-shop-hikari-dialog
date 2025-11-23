package io.github.namiuni.qshdialog.configuration.configurations.serializer;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.lang.reflect.Type;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
public final class ItemTypeSerializer implements TypeSerializer<ItemType> {

    public static final ItemTypeSerializer INSTANCE = new ItemTypeSerializer();

    @Override
    public ItemType deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final Key key = Objects.requireNonNull(node.get(Key.class));
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ITEM).getOrThrow(key);
    }

    @Override
    public void serialize(final Type type, @Nullable final ItemType itemType, final ConfigurationNode node) throws SerializationException {
        if (itemType != null) {
            node.set(itemType.key());
        }
    }
}
