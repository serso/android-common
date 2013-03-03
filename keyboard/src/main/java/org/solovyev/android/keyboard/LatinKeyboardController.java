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

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:22 PM
 */
final class LatinKeyboardController extends AbstractAndroidKeyboardController<AndroidAKeyboard> {

	private long lastShiftTime;

	@Nonnull
	private AndroidAKeyboard qwertyKeyboard;

	@Nonnull
	private AndroidAKeyboard symbolsKeyboard;

	@Nonnull
	private AndroidAKeyboard symbolsShiftedKeyboard;

    @Nonnull
    @Override
    protected AKeyboardControllerState<AndroidAKeyboard> onInitializeInterface0(@Nonnull InputMethodService inputMethodService) {
        qwertyKeyboard = AndroidAKeyboard.newInstance(String.valueOf(R.xml.qwerty), new LatinKeyboard(inputMethodService, R.xml.qwerty));
        symbolsKeyboard = AndroidAKeyboard.newInstance(String.valueOf(R.xml.qwerty), new LatinKeyboard(inputMethodService, R.xml.symbols));
        symbolsShiftedKeyboard = AndroidAKeyboard.newInstance(String.valueOf(R.xml.qwerty), new LatinKeyboard(inputMethodService, R.xml.symbols_shift));

        return AKeyboardControllerStateImpl.newDefaultState(qwertyKeyboard);
    }

    @Nonnull
	@Override
	public AKeyboardControllerState<AndroidAKeyboard> onStartInput0(@Nonnull EditorInfo attribute, boolean restarting) {
		final AKeyboardControllerState<AndroidAKeyboard> result;

		// We are now going to initialize our state based on the type of
		// text being edited.
		switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
			case InputType.TYPE_CLASS_NUMBER:
			case InputType.TYPE_CLASS_DATETIME:
				// Numbers and dates default to the symbols keyboard, with
				// no extra features.
				result = AKeyboardControllerStateImpl.newDefaultState(symbolsKeyboard);
				break;

			case InputType.TYPE_CLASS_PHONE:
				// Phones will also default to the symbols keyboard, though
				// often you will want to have a dedicated phone keyboard.
				result = AKeyboardControllerStateImpl.newDefaultState(symbolsKeyboard);
				break;

			case InputType.TYPE_CLASS_TEXT:
				// This is general text editing.  We will default to the
				// normal alphabetic keyboard, and assume that we should
				// be doing predictive text (showing candidates as the
				// user types).
				boolean prediction = true;
				boolean completion = false;

				// We now look for a few special variations of text that will
				// modify our behavior.
				int variation = attribute.inputType & InputType.TYPE_MASK_VARIATION;
				if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
						variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
					// Do not display predictions / what the user is typing
					// when they are entering a password.
					prediction = false;
				}

				if (variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
						|| variation == InputType.TYPE_TEXT_VARIATION_URI
						|| variation == InputType.TYPE_TEXT_VARIATION_FILTER) {
					// Our predictions are not useful for e-mail addresses
					// or URIs.
					prediction = false;
				}

				if ((attribute.inputType & InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
					// If this is an auto-complete text view, then our predictions
					// will not be shown and instead we will allow the editor
					// to supply their own.  We only show the editor's
					// candidates when in fullscreen mode, otherwise relying
					// own it displaying its own UI.
					prediction = false;
					completion = getInputMethodService().isFullscreenMode();
				}

				result = AKeyboardControllerStateImpl.newInstance(prediction, completion, qwertyKeyboard);

				// We also want to look at the current state of the editor
				// to decide whether our alphabetic keyboard should start out
				// shifted.
				updateShiftKeyState(attribute);
				break;

			default:
				// For all unknown input types, default to the alphabetic
				// keyboard with no special features.
				updateShiftKeyState(attribute);
				result = AKeyboardControllerStateImpl.newDefaultState(qwertyKeyboard);
		}

		return result;
	}

	@Override
	public void onFinishInput() {
		super.onFinishInput();
		setCurrentKeyboard(qwertyKeyboard);
	}

    @Override
    protected void handleModeChange() {
        super.handleModeChange();

        AndroidAKeyboard current = getCurrentKeyboard();
        if (current == symbolsKeyboard || current == symbolsShiftedKeyboard) {
            current = qwertyKeyboard;
        } else {
            current = symbolsKeyboard;
        }
        setCurrentKeyboard(current);
        if (current == symbolsKeyboard) {
            current.setShifted(false);
        }
    }

    @Nonnull
    @Override
    protected AKeyboardConfiguration onCreate0(@Nonnull Context context) {
        return new AKeyboardConfigurationImpl(context.getResources().getString(R.string.word_separators));
    }

    @Nonnull
	@Override
	public AKeyboardViewWithSuggestions<AndroidAKeyboard> createKeyboardView0(@Nonnull Context context) {
		return new AKeyboardViewWithSuggestionsImpl<AndroidAKeyboard, KeyboardViewAKeyboardView>(R.layout.latin_keyboard, this, getInputMethodService());
	}

	private void checkToggleCapsLock() {
		long now = System.currentTimeMillis();
		if (lastShiftTime + 800 > now) {
            setState(getState().copyForNewCapsLock(!getState().isCapsLock()));
			lastShiftTime = 0;
		} else {
			lastShiftTime = now;
		}
	}
}
