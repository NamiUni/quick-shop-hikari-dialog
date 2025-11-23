package io.github.namiuni.qshdialog.configuration.configurations.dialog.body;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DialogBodyTypes {

    public static final Key PLAIN_MESSAGE = Key.key("plain_message");
    public static final Key ITEM = Key.key("item");

    private DialogBodyTypes() {
    }
}
