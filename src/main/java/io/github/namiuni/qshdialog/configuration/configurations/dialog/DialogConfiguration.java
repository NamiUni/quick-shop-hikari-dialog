package io.github.namiuni.qshdialog.configuration.configurations.dialog;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputSettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.type.DialogTypeSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
@SuppressWarnings("UnstableApiUsage")
public record DialogConfiguration(
        String title,
        @Nullable String externalTitle,
        List<DialogBodySettings> body,
        List<? extends DialogInputSettings> inputs,
        @Nullable Boolean canCloseWithEscape,
        @Nullable Boolean pause,
        DialogBase.@Nullable DialogAfterAction afterAction,
        DialogTypeSettings button
) {

    public static Builder builder(final String title) {
        return new Builder(title);
    }

    public DialogLike createDialog(final DialogProviderContext context) {
        return Dialog.create(builder -> builder
                .empty()
                .base(DialogBase.create(
                        MiniMessage.miniMessage().deserialize(this.title, context.user()),
                        this.externalTitle == null ? null : MiniMessage.miniMessage().deserialize(this.externalTitle, context.user()),
                        Optional.ofNullable(this.canCloseWithEscape).orElse(true),
                        Optional.ofNullable(this.pause).orElse(true),
                        Optional.ofNullable(this.afterAction).orElse(DialogBase.DialogAfterAction.CLOSE),
                        this.body.stream().map(body -> body.createDialogBody(context)).toList(),
                        this.inputs.stream().map(input -> input.createDialogInput(context)).toList())
                )
                .type(this.button.createDialogType(context))
        );
    }

    public static final class Builder {

        private final String title;
        private @Nullable String externalTitle;
        private List<DialogBodySettings> body = List.of();
        private List<DialogInputSettings> inputs = List.of();
        private @Nullable Boolean canCloseWithEscape;
        private @Nullable Boolean pause;
        private DialogBase.@Nullable DialogAfterAction afterAction;

        public Builder(final String title) {
            this.title = title;
        }

        public Builder externalTitle(final @Nullable String externalTitle) {
            this.externalTitle = externalTitle;
            return this;
        }

        public Builder body(final List<DialogBodySettings> body) {
            this.body = body;
            return this;
        }

        public Builder inputs(final List<DialogInputSettings> inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder canCloseWithEscape(final @Nullable Boolean canCloseWithEscape) {
            this.canCloseWithEscape = canCloseWithEscape;
            return this;
        }

        public Builder pause(final @Nullable Boolean pause) {
            this.pause = pause;
            return this;
        }

        public Builder afterAction(final DialogBase.DialogAfterAction afterAction) {
            this.afterAction = afterAction;
            return this;
        }

        public DialogConfiguration build(final DialogTypeSettings type) {
            return new DialogConfiguration(
                    this.title,
                    this.externalTitle,
                    this.body,
                    this.inputs,
                    this.canCloseWithEscape,
                    this.pause,
                    this.afterAction,
                    type
            );
        }
    }
}
