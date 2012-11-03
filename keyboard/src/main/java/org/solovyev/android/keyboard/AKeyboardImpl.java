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
    private final String keyboardId;

    @NotNull
    private final K keyboard;

    public AKeyboardImpl(@NotNull String keyboardId, @NotNull K keyboard) {
        this.keyboardId = keyboardId;
        this.keyboard = keyboard;
    }

    @NotNull
    public static AKeyboard<AndroidAKeyboardDef> fromResource(@NotNull Context context, int keyboardRes) {
        return new AKeyboardImpl<AndroidAKeyboardDef>(String.valueOf(keyboardRes), AndroidAKeyboardDef.newInstance(new AbstractKeyboard(context, keyboardRes)));
    }

    @NotNull
    @Override
    public String getKeyboardId() {
        return keyboardId;
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

        if (!keyboardId.equals(aKeyboard.keyboardId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return keyboardId.hashCode();
    }
}
