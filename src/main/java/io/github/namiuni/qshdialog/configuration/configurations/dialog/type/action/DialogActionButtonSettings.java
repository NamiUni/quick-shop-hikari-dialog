package io.github.namiuni.qshdialog.configuration.configurations.dialog.type.action;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
@SuppressWarnings("UnstableApiUsage")
public record DialogActionButtonSettings(
        String label,
        @Nullable String tooltip,
        @Nullable @Range(from = 1, to = 1024) Integer width,
        @Nullable DialogAction action
) {

    public static Builder builder(final String label) {
        return new Builder(label);
    }

    public ActionButton createDialogActionButton(final DialogProviderContext context) {
        final Component renderedLabel = MiniMessage.miniMessage().deserialize(this.label, context.user());
        final Component tooltip = this.tooltip == null
                ? null
                : MiniMessage.miniMessage().deserialize(this.tooltip, context.user());

        return ActionButton.create(
                renderedLabel,
                tooltip,
                Optional.ofNullable(this.width).orElse(150),
                this.action
        );
    }

    public static final class Builder {

        private final String label;
        private @Nullable String tooltip;
        private @Nullable @Range(from = 1, to = 1024) Integer width;
        private @Nullable DialogAction action;

        Builder(final String label) {
            this.label = label;
        }

        public Builder tooltip(final @Nullable String tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder width(final @Nullable @Range(from = 1, to = 1024) Integer width) {
            this.width = width;
            return this;
        }

        public Builder action(final @Nullable DialogAction action) {
            this.action = action;
            return this;
        }

        public DialogActionButtonSettings build() {
            return new DialogActionButtonSettings(
                    this.label,
                    this.tooltip,
                    this.width,
                    this.action
            );
        }
    }
}
