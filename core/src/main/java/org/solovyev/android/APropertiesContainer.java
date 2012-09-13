package org.solovyev.android;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.JCloneable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * User: serso
 * Date: 8/21/12
 * Time: 2:18 PM
 */
public interface APropertiesContainer extends APropertiesProvider, JCloneable<APropertiesContainer>, Serializable {

    @NotNull
    @Override
    APropertiesContainer clone();

    @NotNull
    AProperty setProperty(@NotNull String name, @NotNull String value);

    void setProperty(@NotNull AProperty property);

    @Nullable
    AProperty removeProperty(@NotNull String name);

    void clearProperties();

    @NotNull
    Map<String, AProperty> getProperties();

    void setPropertiesFrom(@NotNull APropertiesContainer that);

    void setPropertiesFrom(@NotNull Collection<AProperty> properties);
}
