package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:22 PM
 */
final class LatinKeyboardController extends AbstractAndroidKeyboardController<AndroidAKeyboard> {

	private long lastShiftTime;

	@NotNull
	private AndroidAKeyboard qwertyKeyboard;

	@NotNull
	private AndroidAKeyboard symbolsKeyboard;

	@NotNull
	private AndroidAKeyboard symbolsShiftedKeyboard;

    @NotNull
    @Override
    protected AKeyboardControllerState<AndroidAKeyboard> onInitializeInterface0(@NotNull InputMethodService inputMethodService) {
        qwertyKeyboard = AndroidAKeyboard.newInstance(String.valueOf(R.xml.qwerty), new LatinKeyboard(inputMethodService, R.xml.qwerty));
        symbolsKeyboard = AndroidAKeyboard.newInstance(String.valueOf(R.xml.qwerty), new LatinKeyboard(inputMethodService, R.xml.symbols));
        symbolsShiftedKeyboard = AndroidAKeyboard.newInstance(String.valueOf(R.xml.qwerty), new LatinKeyboard(inputMethodService, R.xml.symbols_shift));

        return AKeyboardControllerStateImpl.newDefaultState(qwertyKeyboard);
    }

    @NotNull
	@Override
	public AKeyboardControllerState<AndroidAKeyboard> onStartInput0(@NotNull EditorInfo attribute, boolean restarting) {
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
	public boolean handleSpecialKey(int primaryCode) {
        boolean consumed = super.handleSpecialKey(primaryCode);

        if (!consumed) {
            switch (primaryCode) {
                case Keyboard.KEYCODE_MODE_CHANGE:

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

                    consumed = true;

                    break;

            }
        }

        return consumed;
	}

    @NotNull
    @Override
    protected AKeyboardConfiguration onCreate0(@NotNull Context context) {
        return new AKeyboardConfigurationImpl(context.getResources().getString(R.string.word_separators));
    }

    @NotNull
	@Override
	public AKeyboardViewWithSuggestions<AndroidAKeyboard> createKeyboardView0(@NotNull Context context) {
		return new AKeyboardViewWithSuggestionsImpl<AndroidAKeyboard, KeyboardViewAKeyboardView>(R.layout.latin_keyboard, this, getInputMethodService());
	}

	public void handleShift() {

		final AndroidAKeyboard currentKeyboard = getCurrentKeyboard();
		if (qwertyKeyboard == currentKeyboard) {
			// Alphabet keyboard
			checkToggleCapsLock();
            boolean shifted = getState().isCapsLock() || !getState().isShifted();
            setShifted(shifted);
		} else if (currentKeyboard == symbolsKeyboard) {
			symbolsKeyboard.setShifted(true);
			getKeyboardView().setKeyboard(symbolsShiftedKeyboard);
			symbolsShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == symbolsShiftedKeyboard) {
			symbolsShiftedKeyboard.setShifted(false);
			getKeyboardView().setKeyboard(symbolsKeyboard);
			symbolsKeyboard.setShifted(false);
		}
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
