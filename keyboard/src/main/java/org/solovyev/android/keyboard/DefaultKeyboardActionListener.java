package org.solovyev.android.keyboard;

import android.inputmethodservice.KeyboardView;
import org.jetbrains.annotations.NotNull;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 11:30
 */
public class DefaultKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

	@NotNull
	private final AKeyboardController keyboardController;

	public DefaultKeyboardActionListener(@NotNull AKeyboardController keyboardController) {
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
		if (keyboardController.getState().isCompletion()) {
			keyboardController.pickDefaultCandidate();
		}
	}

	@Override
	public void swipeDown() {
		keyboardController.handleClose();
	}

	@Override
	public void swipeUp() {
	}
}
