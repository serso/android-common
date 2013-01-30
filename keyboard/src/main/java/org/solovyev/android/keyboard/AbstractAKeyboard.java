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

package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractAKeyboard implements AKeyboard {

	@NotNull
	private final String keyboardId;

	protected AbstractAKeyboard(@NotNull String keyboardId) {
		this.keyboardId = keyboardId;
	}

	@Override
	@NotNull
	public String getKeyboardId() {
		return keyboardId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbstractAKeyboard)) return false;

		AbstractAKeyboard that = (AbstractAKeyboard) o;

		if (!keyboardId.equals(that.keyboardId)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return keyboardId.hashCode();
	}
}
