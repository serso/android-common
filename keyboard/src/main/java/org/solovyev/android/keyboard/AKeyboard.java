package org.solovyev.android.keyboard;

import android.content.res.Resources;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 7:55 PM
 */
public interface AKeyboard<K extends AKeyboardDef> {

    @NotNull
    K getKeyboard();

    void setImeOptions(@NotNull Resources resources, int imeOptions);

    void setShifted(boolean shifted);
}
