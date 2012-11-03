package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:44
 */
public abstract class AbstractAndroidAKeyboardView<K extends AKeyboardDef, KV extends AndroidKeyboardView<K>> implements AKeyboardView<K> {

    @Nullable
    private KV keyboardView;

    private final int keyboardLayoutResId;

    @NotNull
    private final AKeyboardController keyboardController;

    @NotNull
    private final InputMethodService inputMethodService;

    @Nullable
    private KeyboardView.OnKeyboardActionListener keyboardActionListener;


    public AbstractAndroidAKeyboardView(int keyboardLayoutResId,
                                        @NotNull AKeyboardController keyboardController,
                                        @NotNull InputMethodService inputMethodService) {
        this.keyboardLayoutResId = keyboardLayoutResId;
        this.keyboardController = keyboardController;
        this.inputMethodService = inputMethodService;
    }


    @Nullable
    protected KeyboardView.OnKeyboardActionListener getKeyboardActionListener() {
        return keyboardActionListener;
    }


    @Override
    public boolean isExtractViewShown() {
        return inputMethodService.isExtractViewShown();
    }

    public void setCandidatesViewShown(boolean shown) {
        inputMethodService.setCandidatesViewShown(shown);
    }

    @NotNull
    public AKeyboardController getKeyboardController() {
        return keyboardController;
    }

    @NotNull
    public InputMethodService getInputMethodService() {
        return inputMethodService;
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
        this.keyboardView = (KV) layoutInflater.inflate(keyboardLayoutResId, null);

        final KeyboardView.OnKeyboardActionListener keyboardActionListener = this.getKeyboardActionListener();
        if (keyboardActionListener != null) {
            this.keyboardView.setOnKeyboardActionListener(keyboardActionListener);
        }
    }

    @Override
    public void setKeyboard(@NotNull K keyboard) {
        if (this.keyboardView != null) {
            this.keyboardView.setKeyboard(keyboard);
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
    public View getAndroidKeyboardView() {
        assert keyboardView != null;
        return keyboardView.getView();
    }

}
