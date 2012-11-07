package org.solovyev.android.keyboard;

import android.content.Context;
import android.content.res.Resources;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 8:12 PM
 */
public class AKeyboardImpl<K extends AKeyboardDef> implements AKeyboard<K> {

    @NotNull
    private final K keyboard;

    public AKeyboardImpl(@NotNull K keyboard) {
        this.keyboard = keyboard;
    }

    @NotNull
    public static AKeyboard<AndroidAKeyboardDef> fromResource(@NotNull Context context, int keyboardRes) {
        return new AKeyboardImpl<AndroidAKeyboardDef>(AndroidAKeyboardDef.newInstance(String.valueOf(keyboardRes), new LatinKeyboard(context, keyboardRes)));
    }

    @NotNull
    @Override
    public K getKeyboard() {
        return keyboard;
    }

    @Override
    public void setImeOptions(@NotNull Resources resources, int imeOptions) {
        this.keyboard.setImeOptions(resources, imeOptions);
    }

    @Override
    public void setShifted(boolean shifted) {
        this.keyboard.setShifted(shifted);
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AKeyboardImpl)) return false;

		AKeyboardImpl aKeyboard = (AKeyboardImpl) o;

		if (!keyboard.equals(aKeyboard.keyboard)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return keyboard.hashCode();
	}
}
