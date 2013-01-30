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
import android.view.Menu;
import android.view.MenuInflater;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 4/23/12
 * Time: 1:57 PM
 */
@Deprecated
public class LayoutActivityMenu extends AbstractActivityMenu<IdentifiableMenuItem> {

    public final int menuLayoutId;

    private LayoutActivityMenu(int menuLayoutId) {
        this.menuLayoutId = menuLayoutId;
    }

    @NotNull
    public static <E extends Enum & IdentifiableMenuItem> ActivityMenu newInstance(int menuLayoutId, @NotNull Class<E> enumMenuClass) {
        final LayoutActivityMenu result = new LayoutActivityMenu(menuLayoutId);

        Collections.addAll(result.getMenuItems(), enumMenuClass.getEnumConstants());

        return result;
    }

    @NotNull
    public static ActivityMenu newInstance(int menuLayoutId, @NotNull List<IdentifiableMenuItem> menuItems) {
        final LayoutActivityMenu result = new LayoutActivityMenu(menuLayoutId);

        result.addAll(menuItems);

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull Activity activity, @NotNull Menu menu) {
        final MenuInflater menuInflater = activity.getMenuInflater();
        menuInflater.inflate(menuLayoutId, menu);
        return true;
    }
}
