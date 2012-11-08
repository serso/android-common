package org.solovyev.android.keyboard;

import android.inputmethodservice.KeyboardView;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 11:27 PM
 */
public interface AndroidKeyboardView<K extends AKeyboard> {

    void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener);

    void setKeyboard(@NotNull K keyboard);

    void close();

    void dismiss();

    void reload();
}
