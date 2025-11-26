package io.github.namiuni.qshdialog.configuration.configurations.dialog.input;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DialogInputTypes {

    public static final Key TEXT = Key.key("text");
    public static final Key BOOLEAN = Key.key("boolean");
    public static final Key SINGLE_OPTION = Key.key("single_option");
    public static final Key NUMBER_RANGE = Key.key("number_range");

    private DialogInputTypes() {
    }
}
