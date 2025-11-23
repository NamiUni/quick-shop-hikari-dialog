package io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.Map;
import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public sealed interface ItemSettings<T extends Keyed> permits DynamicItemSettings, StaticItemSettings {

    T id();

    Map<DataComponentType, ?> components();

    @Nullable Integer count();

    ItemStack createItem(DialogProviderContext context);

    @SuppressWarnings("unchecked")
    static <T> void setValuedData(
            final ItemStack itemStack,
            final DataComponentType.Valued<T> type,
            final Object value,
            final DialogProviderContext context
    ) {
        if (type == DataComponentTypes.PROFILE && value instanceof ResolvableProfileSettings resolvableProfile) {
            itemStack.setData(DataComponentTypes.PROFILE, resolvableProfile.createResolvableProfile(context));
        } else {
            itemStack.setData(type, (T) value);
        }
    }
}
