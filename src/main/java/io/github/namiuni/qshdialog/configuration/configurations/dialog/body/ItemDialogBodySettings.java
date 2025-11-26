package io.github.namiuni.qshdialog.configuration.configurations.dialog.body;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.ItemSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.ItemDialogBody;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record ItemDialogBodySettings(
        ItemSettings<?> item,
        @Nullable PlainMessageDialogBodySettings description,
        @Nullable Boolean showDecoration,
        @Nullable Boolean showTooltip,
        @Nullable @Range(from = 1, to = 256) Integer width,
        @Nullable @Range(from = 1, to = 256) Integer height
) implements DialogBodySettings {

    @Override
    public Key type() {
        return DialogBodyTypes.ITEM;
    }

    @Override
    public ItemDialogBody createDialogBody(final DialogProviderContext context) {
        return DialogBody.item(
                this.item.createItem(context),
                this.description == null ? null : this.description.createDialogBody(context),
                Optional.ofNullable(this.showDecoration).orElse(true),
                Optional.ofNullable(this.showTooltip).orElse(true),
                Optional.ofNullable(this.width).orElse(16),
                Optional.ofNullable(this.height).orElse(16)
        );
    }

    public static final class Builder {

        private final ItemSettings<?> item;
        private @Nullable PlainMessageDialogBodySettings description;
        private @Nullable Boolean showDecoration;
        private @Nullable Boolean showTooltip;
        private @Nullable @Range(from = 1, to = 256) Integer width;
        private @Nullable @Range(from = 1, to = 256) Integer height;

        Builder(final ItemSettings<?> item) {
            this.item = item;
        }

        public Builder description(final @Nullable PlainMessageDialogBodySettings description) {
            this.description = description;
            return this;
        }

        public Builder showDecoration(final @Nullable Boolean showDecoration) {
            this.showDecoration = showDecoration;
            return this;
        }

        public Builder showTooltip(final @Nullable Boolean showTooltip) {
            this.showTooltip = showTooltip;
            return this;
        }

        public Builder width(final @Range(from = 1, to = 256) Integer width) {
            this.width = width;
            return this;
        }

        public Builder height(final @Range(from = 1, to = 256) Integer height) {
            this.height = height;
            return this;
        }

        public ItemDialogBodySettings build() {
            return new ItemDialogBodySettings(
                    this.item,
                    this.description,
                    this.showDecoration,
                    this.showTooltip,
                    this.width,
                    this.height
            );
        }
    }
}
