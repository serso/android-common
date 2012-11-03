package org.solovyev.android.keyboard;

import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 10:01 PM
 */
public interface AKeyboardView<K extends AKeyboardDef> {

    @NotNull
    View getKeyboardView();

    void setKeyboard(@NotNull K keyboard);

    void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener);

    void closing();

    void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype);

    boolean handleBack();

    boolean isShifted();

    void setShifted(boolean shifted);

	boolean isExtractViewShown();
}
