package io.github.namiuni.qshdialog.common.utilities;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record NumberRange<N>(N min, N max) {
}
