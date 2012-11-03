package org.solovyev.android.keyboard;

import android.content.res.Resources;
import android.inputmethodservice.Keyboard;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:06 PM
 */
public class AndroidAKeyboardDef implements AKeyboardDef {

    @NotNull
    private Keyboard keyboard;

    private AndroidAKeyboardDef() {
    }

    @NotNull
    public static AndroidAKeyboardDef newInstance(@NotNull Keyboard keyboard) {
        final AndroidAKeyboardDef result = new AndroidAKeyboardDef();
        result.keyboard = keyboard;
        return result;
    }

    @NotNull
    public Keyboard getKeyboard() {
        return keyboard;
    }

    @Override
    public void setImeOptions(@NotNull Resources resources, int imeOptions) {
        // todo serso: refactor
        if ( keyboard instanceof AbstractKeyboard) {
            ((AbstractKeyboard) keyboard).setImeOptions(resources, imeOptions);
        }
    }

    @Override
    public void setShifted(boolean shiftState) {
        keyboard.setShifted(shiftState);
    }


}
