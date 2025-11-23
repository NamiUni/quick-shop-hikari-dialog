package io.github.namiuni.qshdialog.configuration.configurations.serializer;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.lang.reflect.Type;
import java.util.function.Predicate;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class DataComponentTypeSerializer extends ScalarSerializer<DataComponentType> {

    public static final DataComponentTypeSerializer INSTANCE = new DataComponentTypeSerializer();

    public DataComponentTypeSerializer() {
        super(DataComponentType.class);
    }

    @Override
    public DataComponentType deserialize(final Type type, final Object obj) throws SerializationException {
        if (obj instanceof Key key) {
            return RegistryAccess.registryAccess()
                    .getRegistry(RegistryKey.DATA_COMPONENT_TYPE)
                    .getOrThrow(key);
        }

        throw new SerializationException("Cannot deserialize DataComponentType from " + obj);
    }

    @Override
    protected Object serialize(final DataComponentType item, final Predicate<Class<?>> typeSupported) {
        return item.key();
    }
}
