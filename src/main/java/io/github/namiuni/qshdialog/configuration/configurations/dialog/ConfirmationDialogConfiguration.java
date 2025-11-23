package io.github.namiuni.qshdialog.configuration.configurations.dialog;

import io.github.namiuni.qshdialog.configuration.configurations.dialog.body.DialogBodySettings;
import io.github.namiuni.qshdialog.configuration.configurations.dialog.input.DialogInputSettings;
import io.github.namiuni.qshdialog.shop.dialog.DialogProviderContext;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.util.List;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
@SuppressWarnings("UnstableApiUsage")
public record ConfirmationDialogConfiguration(
        Component title,
        @Nullable Component externalTitle,
        List<DialogBodySettings<?>> body,
        List<DialogInputSettings<?>> inputs,
        boolean canCloseWithEscape,
        boolean pause,
        DialogBase.DialogAfterAction afterAction,
        DialogActionButtonSettings yes,
        DialogActionButtonSettings no
) implements DialogConfiguration {

    @Override
    public DialogLike createDialog(final DialogProviderContext context) {
        return Dialog.create(builder -> builder
                .empty()
                .base(DialogBase.create(
                        GlobalTranslator.render(this.title, context.owner().locale()),
                        this.externalTitle == null ? null : GlobalTranslator.render(this.externalTitle, context.owner().locale()),
                        this.canCloseWithEscape,
                        this.pause,
                        this.afterAction,
                        this.body.stream().map(body -> body.createDialogBody(context)).toList(),
                        this.inputs.stream().map(input -> input.createDialogInput(context)).toList())
                )
                .type(DialogType.confirmation(
                        this.yes.createDialogActionButton(context),
                        this.no.createDialogActionButton(context))
                )
        );
    }
}
