package io.github.namiuni.qshdialog.configuration.configurations.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.ResolvableProfileSettings;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DataComponentMapSerializer implements TypeSerializer<Map<DataComponentType, ?>> {

    public static final DataComponentMapSerializer INSTANCE = new DataComponentMapSerializer();

    @Override
    public Map<DataComponentType, ?> deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final Map<DataComponentType, @Nullable Object> dataComponents = new HashMap<>();

        for (final Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
            final DataComponentType componentType = RegistryAccess.registryAccess()
                    .getRegistry(RegistryKey.DATA_COMPONENT_TYPE)
                    .getOrThrow(Key.key((String) entry.getKey()));
            switch (componentType) {
                case DataComponentType.Valued<?> valued -> dataComponents.put(componentType, getValue(valued, entry.getValue()));
                case DataComponentType.NonValued ignored -> dataComponents.put(componentType, null);
                default -> throw new SerializationException();
            }
        }

        return dataComponents;
    }

    @Override
    public void serialize(final Type type, @Nullable final Map<DataComponentType, ?> dataComponents, final ConfigurationNode source) throws SerializationException {
        if (dataComponents != null) {
            for (final Map.Entry<DataComponentType, ?> entry : dataComponents.entrySet()) {
                final DataComponentType dataComponentType = entry.getKey();
                source.set(dataComponentType);
                if (dataComponentType instanceof DataComponentType.Valued<?>) {
                    final Object value = entry.getValue();
                    source.node(dataComponentType).set(value);
                }
            }
        }
    }

    private static <T> Object getValue(final DataComponentType.Valued<T> componentType, final ConfigurationNode node) throws SerializationException {
        if (componentType == DataComponentTypes.PROFILE) {
            return Objects.requireNonNull(node.get(ResolvableProfileSettings.class));
        }

        return Objects.requireNonNull(node.get(new TypeToken<>() { }));
    }
}
