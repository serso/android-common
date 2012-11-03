package org.solovyev.android.keyboard;

import android.inputmethodservice.KeyboardView;
import android.view.View;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 11:27 PM
 */
public interface AndroidKeyboardView<K extends AKeyboardDef> {

    void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener);

    void setKeyboard(@NotNull K keyboard);

    void closing();

    boolean handleBack();

    boolean isShifted();

    void setShifted(boolean shifted);

    @NotNull
    View getView();
}
