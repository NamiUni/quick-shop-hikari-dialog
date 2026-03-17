package io.github.namiuni.qshdialog.minecraft.paper.configuration.serializer;

import java.lang.reflect.Type;
import java.util.Locale;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@NullMarked
public final class LocaleSerializer implements TypeSerializer<Locale> {

    public static final LocaleSerializer INSTANCE = new LocaleSerializer();

    @Override
    public Locale deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final String localeString = node.getString();
        if (localeString == null) {
            throw new SerializationException();
        }

        final Locale parsed = Translator.parseLocale(localeString);
        if (parsed == null) {
            throw new SerializationException();
        }

        return parsed;
    }

    @Override
    public void serialize(final Type type, @Nullable final Locale obj, final ConfigurationNode node) throws SerializationException {
        if (obj != null) {
            node.set(obj.toString());
        }
    }
}
