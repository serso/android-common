package org.solovyev.android.keyboard;

import android.content.res.Resources;
import android.inputmethodservice.Keyboard;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:06 PM
 */
public class AndroidAKeyboard extends AbstractAKeyboard {

    @NotNull
    private Keyboard keyboard;

    private AndroidAKeyboard(@NotNull String keyboardId) {
		super(keyboardId);
	}

    @NotNull
    public static AndroidAKeyboard newInstance(@NotNull String keyboardId, @NotNull Keyboard keyboard) {
        final AndroidAKeyboard result = new AndroidAKeyboard(keyboardId);
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
        if ( keyboard instanceof LatinKeyboard) {
            ((LatinKeyboard) keyboard).setImeOptions(resources, imeOptions);
        }
    }

    @Override
    public void setShifted(boolean shiftState) {
        keyboard.setShifted(shiftState);
    }


}
