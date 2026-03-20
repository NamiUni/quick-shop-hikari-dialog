/*
 * quick-shop-hikari-dialog
 *
 * Copyright (c) 2025. Namiu (うにたろう)
 *                     Contributors []
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.namiuni.qshdialog.minecraft.paper.configuration.serializer;

import io.github.namiuni.qshdialog.minecraft.paper.dialog.elements.ShopInputType;
import java.lang.reflect.Type;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
public final class ShopInputTypeSerializer implements TypeSerializer<ShopInputType> {

    public static final ShopInputTypeSerializer INSTANCE = new ShopInputTypeSerializer();

    @Override
    public ShopInputType deserialize(final Type type, final ConfigurationNode node) {
        final String nodeString = Objects.requireNonNull(node.getString());
        return ShopInputType.of(nodeString);
    }

    @Override
    public void serialize(final Type type, @Nullable final ShopInputType obj, final ConfigurationNode node) throws SerializationException {
        if (obj != null) {
            node.set(obj.toString());
        }
    }
}
