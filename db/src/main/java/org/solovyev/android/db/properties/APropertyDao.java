/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.db.properties;

import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AProperty;

import java.util.Collection;
import java.util.List;

/**
 * User: serso
 * Date: 8/20/12
 * Time: 8:39 PM
 */
public interface APropertyDao {

    @NotNull
    List<AProperty> loadPropertiesById(@NotNull Object id);

    void removePropertiesById(@NotNull Object id);

    void insertProperty(@NotNull Object id, @NotNull AProperty property);

    void insertProperties(@NotNull Object id, @NotNull Collection<AProperty> properties);
}
