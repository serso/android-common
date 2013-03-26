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

import android.inputmethodservice.KeyboardView;
import javax.annotation.Nonnull;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 11:30
 */
public class DefaultKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

	@Nonnull
	private final AKeyboardController keyboardController;

	public DefaultKeyboardActionListener(@Nonnull AKeyboardController keyboardController) {
		this.keyboardController = keyboardController;
	}

	@Override
	public void onPress(int primaryCode) {
	}

	@Override
	public void onRelease(int primaryCode) {
	}

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		keyboardController.onKey(primaryCode, keyCodes);
	}

	@Override
	public void onText(CharSequence text) {
		keyboardController.onText(text);
	}

	@Override
	public void swipeLeft() {
		keyboardController.handleBackspace();
	}

	@Override
	public void swipeRight() {
		keyboardController.pickDefaultCandidate();
	}

	@Override
	public void swipeDown() {
		keyboardController.handleClose();
	}

	@Override
	public void swipeUp() {
	}
}
