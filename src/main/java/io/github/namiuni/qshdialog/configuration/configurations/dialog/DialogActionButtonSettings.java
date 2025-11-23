package io.github.namiuni.qshdialog.configuration.configurations.dialog;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
@SuppressWarnings("UnstableApiUsage")
public record DialogActionButtonSettings(
        Component label,
        @Nullable Component tooltip,
        @Range(from = 1, to = 1024) int width,
        @Nullable DialogAction action
) {

    public static Builder builder(final Component label) {
        return new Builder(label);
    }

    public ActionButton createDialogActionButton(final DialogProviderContext context) {
        final Component renderedLabel = GlobalTranslator.render(this.label, context.owner().locale());
        final Component tooltip = this.tooltip == null
                ? null
                : GlobalTranslator.render(this.tooltip, context.owner().locale());

        return ActionButton.create(
                renderedLabel,
                tooltip,
                this.width,
                this.action
        );
    }

    public static final class Builder {

        private final Component label;
        private @Nullable Component tooltip = null;
        private @Range(from = 1, to = 1024) int width = 150;
        private @Nullable DialogAction action = null;

        Builder(final Component label) {
            this.label = label;
        }

        public Builder tooltip(final @Nullable Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder width(final @Range(from = 1, to = 1024) int width) {
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
