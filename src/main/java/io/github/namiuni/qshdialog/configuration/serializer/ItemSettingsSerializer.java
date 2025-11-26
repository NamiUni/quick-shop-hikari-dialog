package io.github.namiuni.qshdialog.configuration.serializer;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.DynamicItemSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.ItemSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.QSHDialogItemType;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.StaticItemSettings;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.datacomponent.DataComponentType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class ItemSettingsSerializer implements TypeSerializer<ItemSettings<?>> {

    public static final ItemSettingsSerializer INSTANCE = new ItemSettingsSerializer();

    private static final String ID = "id";
    private static final String COMPONENTS = "components";
    private static final String COUNT = "count";

    @Override
    public ItemSettings<?> deserialize(final Type type, final ConfigurationNode source) throws SerializationException {
        final ConfigurationNode idNode = source.node(ID);

        final Map<DataComponentType, ?> dataComponents = source.node(COMPONENTS).get(new TypeToken<>() { }, Map.of());
        final Integer count = source.node(COUNT).get(Integer.class);

        final QSHDialogItemType key = Objects.requireNonNull(idNode.get(QSHDialogItemType.class));
        if (QSHDialogItemType.PRODUCT == key) {
            return new DynamicItemSettings(key, dataComponents, count);
        }

        final org.bukkit.inventory.ItemType staticId = Objects.requireNonNull(idNode.get(org.bukkit.inventory.ItemType.class));
        return new StaticItemSettings(staticId, dataComponents, count);
    }

    @Override
    public void serialize(final Type ignored, @Nullable final ItemSettings<?> item, final ConfigurationNode source) throws SerializationException {
        if (item != null) {
            source.node(ID).set(item.id());
            if (!item.components().isEmpty()) {
                for (final Map.Entry<DataComponentType, ?> entry : item.components().entrySet()) {
                    final DataComponentType dataComponentType = entry.getKey();
                    switch (dataComponentType) {
                        case DataComponentType.Valued<?> valued -> source.node(COMPONENTS).node(valued.key()).set(entry.getValue());
                        case DataComponentType.NonValued nonValued -> source.node(COMPONENTS).set(nonValued);
                        default -> throw new IllegalStateException("Unexpected value: " + dataComponentType);
                    }
                }
            }

            if (!Objects.equals(1, item.count())) {
                source.node(COUNT).set(item.count());
            }
        }
    }
}
