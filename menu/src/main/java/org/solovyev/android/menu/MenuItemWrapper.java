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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
* User: serso
* Date: 8/13/12
* Time: 4:24 PM
*/
class MenuItemWrapper<MI> {

    @Nullable
    private final LabeledMenuItem<MI> labeledMenuItem;

    @Nullable
    private Integer menuItemId;

    @Nullable
    private final IdentifiableMenuItem<MI> identifiableMenuItem;

    MenuItemWrapper(@NotNull LabeledMenuItem<MI> labeledMenuItem) {
        this.labeledMenuItem = labeledMenuItem;
        this.identifiableMenuItem = null;
    }

    MenuItemWrapper(@NotNull IdentifiableMenuItem<MI> identifiableMenuItem) {
        this.identifiableMenuItem = identifiableMenuItem;
        this.labeledMenuItem = null;
    }

    @NotNull
    public AMenuItem<MI> getMenuItem() {
        return labeledMenuItem != null ? labeledMenuItem : identifiableMenuItem;
    }

    @Nullable
    public Integer getMenuItemId() {
        return identifiableMenuItem == null ? menuItemId : identifiableMenuItem.getItemId();
    }

    public void setMenuItemId(@Nullable Integer menuItemId) {
        assert labeledMenuItem != null;
        this.menuItemId = menuItemId;
    }

    @NotNull
    public String getCaption(@NotNull Activity activity) {
        assert labeledMenuItem != null;
        return labeledMenuItem.getCaption(activity);
    }
}
