package io.github.namiuni.qshdialog.minecraft.paper.integration;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MiniPlaceholdersExtension {

    private MiniPlaceholdersExtension() {
    }

    public static boolean miniPlaceholdersLoaded() {
        return Bukkit.getPluginManager().isPluginEnabled("MiniPlaceholders");
    }

    public static TagResolver audienceGlobalPlaceholders() {
        if (miniPlaceholdersLoaded()) {
            return MiniPlaceholders.audienceGlobalPlaceholders();
        } else {
            return TagResolver.empty();
        }
    }
}
