package io.github.namiuni.qshdialog.minecraft.paper.dialog;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class InvalidPriceException extends Exception {

    private final String rawInput;

    public InvalidPriceException(final String rawInput, final NumberFormatException cause) {
        super("Invalid price input: \"" + rawInput + "\"", cause);
        this.rawInput = rawInput;
    }

    public String rawInput() {
        return this.rawInput;
    }
}
