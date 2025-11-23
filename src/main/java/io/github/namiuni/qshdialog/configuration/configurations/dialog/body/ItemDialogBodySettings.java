package io.github.namiuni.qshdialog.configuration.configurations.dialog.body;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item.ItemSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.ItemDialogBody;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record ItemDialogBodySettings(
        ItemSettings<?> item,
        @Nullable PlainMessageDialogBodySettings description,
        boolean showDecoration,
        boolean showTooltip,
        @Range(from = 1, to = 256)
        int width,
        @Range(from = 1, to = 256)
        int height
) implements DialogBodySettings<ItemDialogBody> {

    @Override
    public ItemDialogBody createDialogBody(final DialogProviderContext context) {
        return DialogBody.item(
                this.item.createItem(context),
                this.description == null ? null : this.description.createDialogBody(context),
                this.showDecoration,
                this.showTooltip,
                this.width,
                this.height
        );
    }

    public static final class Builder {

        private final ItemSettings<?> item;
        private @Nullable PlainMessageDialogBodySettings description;
        private boolean showDecoration;
        private boolean showTooltip;
        private @Range(from = 1, to = 256) int width = 16;
        private @Range(from = 1, to = 256) int height = 16;

        Builder(final ItemSettings<?> item) {
            this.item = item;
        }

        public Builder description(final @Nullable PlainMessageDialogBodySettings description) {
            this.description = description;
            return this;
        }

        public Builder showDecoration(final boolean showDecoration) {
            this.showDecoration = showDecoration;
            return this;
        }

        public Builder showTooltip(final boolean showTooltip) {
            this.showTooltip = showTooltip;
            return this;
        }

        public Builder width(final @Range(from = 1, to = 256) int width) {
            this.width = width;
            return this;
        }

        public Builder height(final @Range(from = 1, to = 256) int height) {
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
