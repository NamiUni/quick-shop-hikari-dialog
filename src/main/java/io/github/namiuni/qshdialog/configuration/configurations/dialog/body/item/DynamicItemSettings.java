package io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.datacomponent.DataComponentType;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record DynamicItemSettings(
        QSHDialogItemType id,
        Map<DataComponentType, ?> components,
        @Nullable Integer count
) implements ItemSettings<QSHDialogItemType> {

    @Override
    public ItemStack createItem(final DialogProviderContext context) {
        final ItemStack item = switch (this.id) {
            case QSHDialogItemType.PRODUCT -> context.product();
        };
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
