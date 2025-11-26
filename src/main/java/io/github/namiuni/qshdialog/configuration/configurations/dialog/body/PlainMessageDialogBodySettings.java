package io.github.namiuni.qshdialog.configuration.configurations.dialog.body;

import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record PlainMessageDialogBodySettings(
        String contents,
        @Nullable @Range(from = 1L, to = 1024L) Integer width
) implements DialogBodySettings {

    @Override
    public Key type() {
        return DialogBodyTypes.PLAIN_MESSAGE;
    }

    @Override
    public PlainMessageDialogBody createDialogBody(final DialogProviderContext context) {
        final Component renderedContents = MiniMessage.miniMessage().deserialize(this.contents, context.user());
        return DialogBody.plainMessage(
                renderedContents,
                Optional.ofNullable(this.width).orElse(200)
        );
    }

    public static final class Builder {

        private final String contents;
        private @Nullable @Range(from = 1L, to = 1024L) Integer width;

        Builder(final String contents) {
            this.contents = contents;
        }

        public Builder width(final @Nullable @Range(from = 1L, to = 1024L) Integer width) {
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
