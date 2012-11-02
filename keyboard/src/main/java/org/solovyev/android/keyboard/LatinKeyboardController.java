package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.text.StringUtils;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:22 PM
 */
final class LatinKeyboardController extends AbstractKeyboardController {

	@NotNull
	private String wordSeparators;

	private long lastShiftTime;

	@NotNull
	private AKeyboard qwertyKeyboard;

	@NotNull
	private AKeyboard symbolsKeyboard;

	@NotNull
	private AKeyboard symbolsShiftedKeyboard;

	@Override
	public void onInitializeInterface(@NotNull InputMethodService inputMethodService) {
		super.onInitializeInterface(inputMethodService);

		qwertyKeyboard = AKeyboardImpl.fromResource(inputMethodService, R.xml.qwerty);
		symbolsKeyboard = AKeyboardImpl.fromResource(inputMethodService, R.xml.symbols);
		symbolsShiftedKeyboard = AKeyboardImpl.fromResource(inputMethodService, R.xml.symbols_shift);

		setCurrentKeyboard(qwertyKeyboard);

	}

	@NotNull
	@Override
	public AKeyboardControllerState onStartInput0(@NotNull EditorInfo attribute, boolean restarting) {
		final AKeyboardControllerState result;

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
				setCurrentKeyboard(symbolsKeyboard);
				result = AKeyboardControllerStateImpl.newDefaultState();
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

				result = AKeyboardControllerStateImpl.newInstance(prediction, completion).copyForNewKeyboard(qwertyKeyboard);

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
	public void onKey(int primaryCode, int[] keyCodes) {
		if (isWordSeparator(primaryCode)) {
			// Handle separator
			if (!StringUtils.isEmpty(getKeyboardInput().getText())) {
				getKeyboardInput().commitTyped();
			}
			sendKey(primaryCode);
			updateShiftKeyState(getInputMethodService().getCurrentInputEditorInfo());
		} else if (primaryCode == Keyboard.KEYCODE_DELETE) {
			handleBackspace();
		} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
			handleShift();
		} else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
			handleClose();
		} else if (primaryCode == LatinKeyboardView.KEYCODE_OPTIONS) {
			// Show a menu or somethin'
		} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
			AKeyboard current = getCurrentKeyboard();
			if (current == symbolsKeyboard || current == symbolsShiftedKeyboard) {
				current = qwertyKeyboard;
			} else {
				current = symbolsKeyboard;
			}
			setCurrentKeyboard(current);
			if (current == symbolsKeyboard) {
				current.setShifted(false);
			}
		} else {
			handleCharacter(primaryCode, keyCodes);
		}
	}

	@Override
	public void onCreate(@NotNull Context context) {
		super.onCreate(context);

		wordSeparators = context.getResources().getString(R.string.word_separators);
	}

	@NotNull
	@Override
	public AKeyboardView createKeyboardView0(@NotNull Context context, @NotNull LayoutInflater layoutInflater) {
		return new AKeyboardViewImpl((KeyboardView)layoutInflater.inflate(R.layout.input, null), this, getInputMethodService(), null);
	}

	public void handleShift() {

		final AKeyboard currentKeyboard = getCurrentKeyboard();
		if (qwertyKeyboard == currentKeyboard) {
			// Alphabet keyboard
			checkToggleCapsLock();
			getKeyboardView().setShifted(getState().isCapsLock() || !getKeyboardView().isShifted());
		} else if (currentKeyboard == symbolsKeyboard) {
			symbolsKeyboard.setShifted(true);
			getKeyboardView().setKeyboard(symbolsShiftedKeyboard.getKeyboard());
			symbolsShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == symbolsShiftedKeyboard) {
			symbolsShiftedKeyboard.setShifted(false);
			getKeyboardView().setKeyboard(symbolsKeyboard.getKeyboard());
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

	@NotNull
	private String getWordSeparators() {
		return wordSeparators;
	}

	public boolean isWordSeparator(int code) {
		final String separators = getWordSeparators();
		return separators.contains(String.valueOf((char) code));
	}
}
