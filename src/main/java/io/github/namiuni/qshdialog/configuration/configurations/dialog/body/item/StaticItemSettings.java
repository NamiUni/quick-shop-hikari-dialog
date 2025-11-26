package io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.datacomponent.DataComponentType;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record StaticItemSettings(
        ItemType id,
        Map<DataComponentType, ?> components,
        @Nullable Integer count
) implements ItemSettings<ItemType> {

    @Override
    public ItemStack createItem(final DialogProviderContext context) {
        final ItemStack item = this.id.createItemStack();
        this.components().forEach((dataComponentType, value) -> {
            switch (dataComponentType) {
                case DataComponentType.Valued<?> valued -> ItemSettings.setValuedData(item, valued, value, context);
                case DataComponentType.NonValued nonValued -> item.setData(nonValued);
                default -> throw new IllegalStateException("Unexpected value: " + dataComponentType);
            }
        });

        if (this.count != null) {
            item.setAmount(this.count);
        }

        return item;
    }
}
