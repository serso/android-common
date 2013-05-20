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

import android.content.res.Resources;
import android.inputmethodservice.Keyboard;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:06 PM
 */
public class AndroidAKeyboard extends AbstractAKeyboard {

	@Nonnull
	private Keyboard keyboard;

	private AndroidAKeyboard(@Nonnull String keyboardId) {
		super(keyboardId);
	}

	@Nonnull
	public static AndroidAKeyboard newInstance(@Nonnull String keyboardId, @Nonnull Keyboard keyboard) {
		final AndroidAKeyboard result = new AndroidAKeyboard(keyboardId);
		result.keyboard = keyboard;
		return result;
	}

	@Nonnull
	public Keyboard getKeyboard() {
		return keyboard;
	}

	@Override
	public void setImeOptions(@Nonnull Resources resources, int imeOptions) {
		// todo serso: refactor
		if (keyboard instanceof LatinKeyboard) {
			((LatinKeyboard) keyboard).setImeOptions(resources, imeOptions);
		}
	}

	@Override
	public void setShifted(boolean shiftState) {
		keyboard.setShifted(shiftState);
	}


}
