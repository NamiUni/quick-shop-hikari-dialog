package io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
@SuppressWarnings("UnstableApiUsage")
public record ResolvableProfileSettings(@Nullable String name, @Nullable String uuid) {

    @SuppressWarnings("PatternValidation")
    public ResolvableProfile createResolvableProfile(final DialogProviderContext context) {
        final ResolvableProfile.Builder builder = ResolvableProfile.resolvableProfile();

        if (this.name != null) {
            final Component component = MiniMessage.miniMessage().deserialize(this.name, context.user(), context.tagResolver());
            final String name = PlainTextComponentSerializer.plainText().serialize(component);
            builder.name(name);
        }

        if (this.uuid != null) {
            final Component component = MiniMessage.miniMessage().deserialize(this.uuid, context.user(), context.tagResolver());
            final String uuid = PlainTextComponentSerializer.plainText().serialize(component);
            builder.uuid(UUID.fromString(uuid));
        }

        return builder.build();
    }
}
