package io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import java.util.UUID;
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

        switch (this.name) {
            case "<owner_name>" -> builder.name(context.owner().name());
            case "<customer_name>" -> builder.name(context.customer().name());
            case null, default -> builder.name(this.name);
        }

        switch (this.uuid) {
            case "<owner_uuid>" -> builder.uuid(context.owner().uuid());
            case "<customer_uuid>" -> builder.uuid(context.customer().uuid());
            case null, default -> builder.uuid(this.uuid == null ? null : UUID.fromString(this.uuid));
        }

        return builder.build();
    }
}
