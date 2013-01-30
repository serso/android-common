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

package org.solovyev.android.samples.db;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 4:40 PM
 */
public interface DbItemService {

    @NotNull
    List<DbItem> getAllDbItems(@NotNull Context context);

    @NotNull
    List<DbItem> getAllStartsWith(@NotNull String prefix, @NotNull Context context);

    void addItem(@NotNull DbItem dbItem, @NotNull Context context);

    @NotNull
    List<DbItem> removeItemByName(@NotNull String name, @NotNull Context context);
}
