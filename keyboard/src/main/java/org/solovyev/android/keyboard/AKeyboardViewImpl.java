package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:44
 */
public class AKeyboardViewImpl<K extends AKeyboard, KV extends View & AndroidKeyboardView<K>> implements AKeyboardView<K> {

    @Nullable
    private KV keyboardView;

    private final int keyboardLayoutResId;

    @NotNull
    private final AKeyboardController keyboardController;

    @NotNull
    private final InputMethodService inputMethodService;

    @Nullable
    private KeyboardView.OnKeyboardActionListener keyboardActionListener;


    public AKeyboardViewImpl(int keyboardLayoutResId,
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
    public void close() {
        if (this.keyboardView != null) {
            this.keyboardView.close();
        }
    }

    @Override
    public void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype) {
    }

    @Override
    public void dismiss() {
        if (this.keyboardView != null) {
            this.keyboardView.dismiss();
        }
    }

    @NotNull
    public View getAndroidKeyboardView() {
        assert keyboardView != null;
        return keyboardView;
    }

    @Override
    public void reloadAndroidKeyboardView() {
        if (this.keyboardView != null) {
            this.keyboardView.reload();
        }
    }

}
