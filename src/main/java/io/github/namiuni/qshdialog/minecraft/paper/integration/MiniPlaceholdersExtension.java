package io.github.namiuni.qshdialog.minecraft.paper.integration;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MiniPlaceholdersExtension {

    private MiniPlaceholdersExtension() {
    }

    public static TagResolver audienceGlobalPlaceholders() {
        return MiniPlaceholders.audienceGlobalPlaceholders(); // TODO ロードチェック
    }
}
