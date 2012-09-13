package org.solovyev.android;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * User: serso
 * Date: 8/22/12
 * Time: 1:45 AM
 */
public interface APropertiesProvider {

    @Nullable
    AProperty getProperty(@NotNull String name);

    @Nullable
    String getPropertyValue(@NotNull String name);

    @NotNull
    Collection<AProperty> getPropertiesCollection();
}
