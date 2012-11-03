package org.solovyev.android.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import org.jetbrains.annotations.NotNull;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:44
 */
public abstract class AbstractAKeyboardView implements AndroidAKeyboardView {

	@NotNull
	private final KeyboardView keyboardView;

	@NotNull
	private final AKeyboardController keyboardController;

	@NotNull
	private final InputMethodService inputMethodService;

	public AbstractAKeyboardView(@NotNull KeyboardView keyboardView,
								 @NotNull AKeyboardController keyboardController,
								 @NotNull InputMethodService inputMethodService) {
		this.keyboardView = keyboardView;
		this.keyboardController = keyboardController;
		this.inputMethodService = inputMethodService;
	}


	@Override
	public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
		this.keyboardView.setOnKeyboardActionListener(keyboardActionListener);
	}

	@Override
	public void setKeyboard(@NotNull AndroidAKeyboardDef keyboard) {
		this.keyboardView.setKeyboard(keyboard.getKeyboard());
	}

	@Override
	public void closing() {
		this.keyboardView.closing();
	}

	@Override
	public boolean handleBack() {
		return this.keyboardView.handleBack();
	}

	@Override
	public boolean isShifted() {
		return this.keyboardView.isShifted();
	}

	@Override
	public void setShifted(boolean shifted) {
		this.keyboardView.setShifted(shifted);
	}

	@Override
	public boolean isExtractViewShown() {
		return inputMethodService.isExtractViewShown();
	}

	@Override
	public void setCandidatesViewShown(boolean shown) {
		inputMethodService.setCandidatesViewShown(shown);
	}

	@NotNull
	public KeyboardView getKeyboardView() {
		return keyboardView;
	}

	@NotNull
	public AKeyboardController getKeyboardController() {
		return keyboardController;
	}

	@NotNull
	public InputMethodService getInputMethodService() {
		return inputMethodService;
	}
}
