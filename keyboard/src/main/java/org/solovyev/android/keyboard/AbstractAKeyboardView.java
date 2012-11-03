package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:44
 */
public abstract class AbstractAKeyboardView implements AndroidAKeyboardView {

	@Nullable
	private KeyboardView keyboardView;

    private final int keyboardLayoutResId;

    @NotNull
	private final AKeyboardController keyboardController;

	@NotNull
	private final InputMethodService inputMethodService;

    @Nullable
    private KeyboardView.OnKeyboardActionListener keyboardActionListener;

    public AbstractAKeyboardView(int keyboardLayoutResId,
								 @NotNull AKeyboardController keyboardController,
								 @NotNull InputMethodService inputMethodService) {
        this.keyboardLayoutResId = keyboardLayoutResId;
        this.keyboardController = keyboardController;
		this.inputMethodService = inputMethodService;
	}


	@Override
	public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        this.keyboardActionListener = keyboardActionListener;
        if (this.keyboardView != null) {
            this.keyboardView.setOnKeyboardActionListener(keyboardActionListener);
        }
    }

    @Override
    public void createAndroidKeyboardView(@NotNull Context context, @NotNull LayoutInflater layoutInflater) {
        this.keyboardView = (KeyboardView)layoutInflater.inflate(keyboardLayoutResId, null);
        if (this.keyboardActionListener != null) {
            this.keyboardView.setOnKeyboardActionListener(this.keyboardActionListener);
        }
    }

    @Override
	public void setKeyboard(@NotNull AndroidAKeyboardDef keyboard) {
        if (this.keyboardView != null) {
            this.keyboardView.setKeyboard(keyboard.getKeyboard());
        }
    }

	@Override
	public void closing() {
        if (this.keyboardView != null) {
            this.keyboardView.closing();
        }
    }

	@Override
	public boolean handleBack() {
        if (this.keyboardView != null) {
            return this.keyboardView.handleBack();
        } else {
            return false;
        }
    }

	@Override
	public boolean isShifted() {
        if (this.keyboardView != null) {
            return this.keyboardView.isShifted();
        } else {
            return false;
        }
    }

	@Override
	public void setShifted(boolean shifted) {
        if (this.keyboardView != null) {
            this.keyboardView.setShifted(shifted);
        }
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
	public KeyboardView getAndroidKeyboardView() {
        assert keyboardView != null;
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
