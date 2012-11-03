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
public abstract class AbstractAndroidAKeyboardView extends AbstractAKeyboardView<AndroidAKeyboardDef> implements AndroidAKeyboardView {

	@Nullable
	private KeyboardView keyboardView;

    private final int keyboardLayoutResId;

    public AbstractAndroidAKeyboardView(int keyboardLayoutResId,
                                        @NotNull AKeyboardController keyboardController,
                                        @NotNull InputMethodService inputMethodService) {
        super(keyboardController, inputMethodService);
        this.keyboardLayoutResId = keyboardLayoutResId;
	}


	@Override
	public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        super.setOnKeyboardActionListener(keyboardActionListener);
        if (this.keyboardView != null) {
            this.keyboardView.setOnKeyboardActionListener(keyboardActionListener);
        }
    }

    @Override
    public void createAndroidKeyboardView(@NotNull Context context, @NotNull LayoutInflater layoutInflater) {
        this.keyboardView = (KeyboardView)layoutInflater.inflate(keyboardLayoutResId, null);

        final KeyboardView.OnKeyboardActionListener keyboardActionListener = this.getKeyboardActionListener();
        if (keyboardActionListener != null) {
            this.keyboardView.setOnKeyboardActionListener(keyboardActionListener);
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

    @NotNull
	public KeyboardView getAndroidKeyboardView() {
        assert keyboardView != null;
		return keyboardView;
	}

}
