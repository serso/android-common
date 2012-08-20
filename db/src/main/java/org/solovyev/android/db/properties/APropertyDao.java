package org.solovyev.android.db.properties;

import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AProperty;

import java.util.List;

/**
 * User: serso
 * Date: 8/20/12
 * Time: 8:39 PM
 */
public interface APropertyDao {

    @NotNull
    List<AProperty> loadPropertiesById(@NotNull Object id);
}
