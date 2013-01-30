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

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.collections.Collections;

import java.util.ArrayList;
import java.util.List;

/**
* User: serso
* Date: 12/18/11
* Time: 1:31 PM
*/
public class MenuImpl<T extends LabeledMenuItem<D>, D> implements AMenu<T, D> {

	private final List<? extends T> menuItems;

	@NotNull
	public static <T extends LabeledMenuItem<D>, D> AMenu<T, D> newInstance(T... menuItems) {
		return new MenuImpl<T, D>(menuItems);
	}

	@NotNull
	public static <T extends LabeledMenuItem<D>, D> AMenu<T, D> newInstance(@NotNull List<? extends T> menuItems) {
		return new MenuImpl<T, D>(menuItems);
	}

	private MenuImpl(T... menuItems) {
		this(Collections.asList(menuItems));
	}

	private MenuImpl(@NotNull List<? extends T> menuItems) {
		this.menuItems = new ArrayList<T>(menuItems);
	}

	@Override
	@Nullable
	public T itemAt(int i) {
		if (i >= 0 && i < menuItems.size()) {
			return menuItems.get(i);
		} else {
			return null;
		}
	}

	@Override
	@NotNull
	public CharSequence[] getMenuCaptions(@NotNull final Context context) {
		final CharSequence[] result = new CharSequence[this.menuItems.size()];
		for (int i = 0; i < this.menuItems.size(); i++) {
			result[i] = this.menuItems.get(i).getCaption(context);
		}
		return result;
	}
}
