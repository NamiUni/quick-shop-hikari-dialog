package io.github.namiuni.qshdialog.configuration.configurations.dialog.body.item;

import java.util.Objects;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum QSHDialogItemType implements Keyed {
    PRODUCT(Key.key("qshdialog", "product"));

    private final Key key;

    QSHDialogItemType(final Key key) {
        this.key = key;
    }

    public Key key() {
        return this.key;
    }

    public static QSHDialogItemType of(final Key key) {
        for (final QSHDialogItemType itemType : QSHDialogItemType.values()) {
            if (Objects.equals(itemType.key, key)) {
                return itemType;
            }
        }

        throw new RuntimeException();
    }
}
