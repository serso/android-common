package org.solovyev.android.keyboard;

import android.content.res.Resources;
import android.inputmethodservice.Keyboard;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 7:55 PM
 */
public interface AKeyboard {

    @NotNull
    String getKeyboardId();

    @NotNull
    Keyboard getKeyboard();

    void setImeOptions(@NotNull Resources resources, int imeOptions);

    void setShifted(boolean shifted);
}
