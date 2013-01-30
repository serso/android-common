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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.ActivityDestroyerController;
import org.solovyev.android.DialogOnActivityDestroyedListener;

/**
 * User: serso
 * Date: 12/19/11
 * Time: 10:54 AM
 */
public class AMenuBuilder<T extends LabeledMenuItem<D>, D>{

	@NotNull
	private final Context context;

	@NotNull
	private final AlertDialog.Builder menuBuilder;

	@NotNull
	private final AMenu<T, D> menu;

	@NotNull
	public static <T extends Enum & LabeledMenuItem<D>, D> AMenuBuilder<T, D> newInstance(@NotNull Context context, @NotNull Class<T> enumClass) {
		return new AMenuBuilder<T, D>(context, EnumMenu.<T, D>newInstance(enumClass));
	}

	@NotNull
	public static <T extends LabeledMenuItem<D>, D> AMenuBuilder<T, D> newInstance(@NotNull Context context, @NotNull AMenu<T, D> menu) {
		return new AMenuBuilder<T, D>(context, menu);
	}

	private AMenuBuilder(@NotNull Context context, @NotNull AMenu<T, D> menu) {
		this.context = context;
		this.menuBuilder = new AlertDialog.Builder(context);
		this.menu = menu;
	}

	@NotNull
	public AlertDialog.Builder getMenuBuilder() {
		return menuBuilder;
	}

	@NotNull
	public AlertDialog create(@NotNull final D data) {
		menuBuilder.setItems(menu.getMenuCaptions(context), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				final LabeledMenuItem<D> menuItem = menu.itemAt(item);
				if (menuItem != null) {
					menuItem.onClick(data, context);
                }
			}
		});

        final AlertDialog result = menuBuilder.create();
        if ( context instanceof Activity) {
            ActivityDestroyerController.getInstance().put((Activity)context, new DialogOnActivityDestroyedListener(result));
        }
        return result;
    }


}
