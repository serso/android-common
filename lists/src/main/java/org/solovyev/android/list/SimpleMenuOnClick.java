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

import org.solovyev.android.menu.LabeledMenuItem;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 7:58 PM
 */
// class for types object to be passed on click
public class SimpleMenuOnClick<T> extends MenuOnClick<T> {

	@Nonnull
	private final T data;

	public SimpleMenuOnClick(@Nonnull List<? extends LabeledMenuItem<ListItemOnClickData<T>>> labeledMenuItems,
							 @Nonnull T data,
							 @Nonnull String menuName) {
		super(labeledMenuItems, menuName);
		this.data = data;
	}

	@Nonnull
	@Override
	protected T getData() {
		return this.data;
	}
}
