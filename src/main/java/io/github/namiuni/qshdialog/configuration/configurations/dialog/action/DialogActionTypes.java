package io.github.namiuni.qshdialog.configuration.configurations.dialog.action;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DialogActionTypes {

    public static final Key OPEN_URL = Key.key("open_url");
    public static final Key RUN_COMMAND = Key.key("run_command");
    public static final Key SUGGEST_COMMAND = Key.key("suggest_command");
    public static final Key CHANGE_PAGE = Key.key("change_page");
    public static final Key COPY_TO_CLIPBOARD = Key.key("cop_to_clipboard");
    public static final Key SHOW_DIALOG = Key.key("show_dialog");
    public static final Key CUSTOM = Key.key("custom");
    public static final Key DYNAMIC_RUN_COMMAND = Key.key("dynamic/run_command");
    public static final Key DYNAMIC_CUSTOM = Key.key("dynamic/custom");

    private DialogActionTypes() {
    }
}
