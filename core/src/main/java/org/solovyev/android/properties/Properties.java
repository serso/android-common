package org.solovyev.android.properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public final class Properties {

    private Properties() {
        throw new AssertionError();
    }

    @Nonnull
    public static AProperty newProperty(@Nonnull String name, @Nullable String value) {
        return APropertyImpl.newInstance(name, value);
    }

    @Nonnull
    public static AProperties newProperties(@Nonnull Collection<AProperty> properties) {
        return MutableAPropertiesImpl.newInstance(properties);
    }

    @Nonnull
    public static AProperties newProperties(@Nonnull Map<String, AProperty> properties) {
        return MutableAPropertiesImpl.newInstance(properties);
    }

    @Nonnull
    public static MutableAProperties copyOf(@Nonnull MutableAProperties properties) {
        return MutableAPropertiesImpl.copyOf(properties);
    }
}
