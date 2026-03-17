package io.github.namiuni.qshdialog.common.utilities;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QSHDialogLogger {

    private static final ComponentLogger LOGGER = ComponentLogger.logger("QSH-Dialog");

    private QSHDialogLogger() {
    }

    public static ComponentLogger logger() {
        return LOGGER;
    }
}
