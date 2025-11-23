package io.github.namiuni.qshdialog.configuration.configurations.dialog.body;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record PlainMessageDialogBodySettings(
        Component contents,
        @Range(from = 1L, to = 1024L) int width
) implements DialogBodySettings<PlainMessageDialogBody> {

    @Override
    public PlainMessageDialogBody createDialogBody(final DialogProviderContext context) {
        final Component renderedDescription = GlobalTranslator.render(this.contents, context.owner().locale());
        return DialogBody.plainMessage(
                renderedDescription,
                this.width
        );
    }

    public static final class Builder {

        private final Component contents;
        private @Range(from = 1L, to = 1024L) int width = 200;

        Builder(final Component contents) {
            this.contents = contents;
        }

        public Builder width(final @Range(from = 1L, to = 1024L) int width) {
            this.width = width;
            return this;
        }

        public PlainMessageDialogBodySettings build() {
            return new PlainMessageDialogBodySettings(
                    this.contents,
                    this.width
            );
        }
    }
}
