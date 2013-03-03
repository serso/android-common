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

package org.solovyev.android.menu;

import android.app.Activity;
import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 8/4/12
 * Time: 2:49 PM
 */

/**
 * Helper class to provide support for Android and Sherlock menu
 * @param <M>
 * @param <MI>
 */
public interface MenuHelper<M, MI> {

    int size(@Nonnull M menu);

    @Nonnull
    MI add(@Nonnull M menu, int groupId, int itemId, int orderId, @Nonnull String caption);

    public void setOnMenuItemClickListener(@Nonnull MI menuItem, @Nonnull AMenuItem<MI> onMenuItemClick, @Nonnull Activity activity);

    void removeItem(@Nonnull M menu, @Nonnull Integer menuItemId);

    void inflateMenu(@Nonnull Activity activity, int layoutId, @Nonnull M menu);

    @Nonnull
    Integer getItemId(@Nonnull MI item);
}
