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

/**
 * User: serso
 * Date: 12/18/11
 * Time: 1:34 PM
 */
public class EnumMenu<T extends Enum & LabeledMenuItem<D>, D> implements AMenu<T, D> {

	@NotNull
	private final AMenu<T, D> menu;

	@NotNull
	public static <T extends Enum & LabeledMenuItem<D>, D> AMenu<T, D> newInstance(@NotNull Class<T> enumClass) {
		return new EnumMenu<T, D>(enumClass);
	}

	private EnumMenu(Class<T> enumClass) {
		this.menu = MenuImpl.newInstance(enumClass.getEnumConstants());
	}

	@Override
	public T itemAt(int i) {
		return this.menu.itemAt(i);
	}

	@NotNull
	@Override
	public CharSequence[] getMenuCaptions(@NotNull final Context context) {
		return this.menu.getMenuCaptions(context);
	}
}
