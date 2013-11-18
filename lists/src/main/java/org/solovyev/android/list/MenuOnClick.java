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

package org.solovyev.android.list;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import org.solovyev.android.menu.ContextMenuBuilder;
import org.solovyev.android.menu.LabeledMenuItem;
import org.solovyev.android.menu.ListContextMenu;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class MenuOnClick<T> implements ListItem.OnClickAction {

	@Nonnull
	private final List<? extends LabeledMenuItem<ListItemOnClickData<T>>> menuItems;

	@Nonnull
	private final String menuName;

	protected MenuOnClick(@Nonnull List<? extends LabeledMenuItem<ListItemOnClickData<T>>> menuItems,
						  @Nonnull String menuName) {
		this.menuItems = menuItems;
		this.menuName = menuName;
	}

	@Override
	public void onClick(@Nonnull Context context, @Nonnull ListAdapter<? extends ListItem> adapter) {
		if (!menuItems.isEmpty()) {
			ContextMenuBuilder.newInstance((FragmentActivity) context, menuName, ListContextMenu.newInstance(menuItems)).build(new ListItemOnClickDataImpl<T>(getData(), adapter)).show();
		}
	}

	@Nonnull
	protected abstract T getData();
}
