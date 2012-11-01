package org.solovyev.android.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 10:04 PM
 */
public class AKeyboardViewImpl implements AKeyboardView {

    @NotNull
    private final KeyboardView keyboardView;

    public AKeyboardViewImpl(@NotNull KeyboardView keyboardView) {
        this.keyboardView = keyboardView;
    }

    @NotNull
    @Override
    public View getKeyboardView() {
        return this.keyboardView;
    }

    @Override
    public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        this.keyboardView.setOnKeyboardActionListener(keyboardActionListener);
    }

    @NotNull
    @Override
    public Keyboard getKeyboard() {
        return this.keyboardView.getKeyboard();
    }

    @Override
    public void setKeyboard(@NotNull Keyboard keyboard) {
        this.keyboardView.setKeyboard(keyboard);
    }

    @Override
    public void closing() {
        this.keyboardView.closing();
    }

    @Override
    public void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype) {
        if ( this.keyboardView instanceof LatinKeyboardView ) {
            // todo serso: refactor
            ((LatinKeyboardView) this.keyboardView).setSubtypeOnSpaceKey(subtype);
        }
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
}
