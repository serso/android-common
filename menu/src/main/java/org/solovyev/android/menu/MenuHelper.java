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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.menu;

import android.app.Activity;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 8/4/12
 * Time: 2:49 PM
 */
public interface MenuHelper<M, MI> {

    int size(@NotNull M menu);

    @NotNull
    MI add(@NotNull M menu, int groupId, int itemId, int orderId, @NotNull String caption);

    public void setOnMenuItemClickListener(@NotNull MI menuItem, @NotNull AMenuItem<MI> onMenuItemClick, @NotNull Activity activity);

    void removeItem(@NotNull M menu, @NotNull Integer menuItemId);

    void inflateMenu(@NotNull Activity activity, int layoutId, @NotNull M menu);

    @NotNull
    Integer getItemId(@NotNull MI item);
}
