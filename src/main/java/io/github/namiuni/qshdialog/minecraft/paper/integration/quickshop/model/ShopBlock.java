package io.github.namiuni.qshdialog.minecraft.paper.integration.quickshop.model;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ShopBlock(Container container, Block frontBlock, ShopComponent component) {

    public ShopBlock withComponent(final ShopComponent component) {
        return new ShopBlock(this.container, this.frontBlock, component);
    }
}
