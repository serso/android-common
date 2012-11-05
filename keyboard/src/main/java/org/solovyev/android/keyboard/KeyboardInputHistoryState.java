package org.solovyev.android.keyboard;

import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 11/6/12
 * Time: 12:21 AM
 */
public class KeyboardInputHistoryState {

    @Nullable
    private CharSequence charSequence;

    private int selection;

    public KeyboardInputHistoryState(CharSequence charSequence, int selection) {
        this.charSequence = charSequence;
        this.selection = selection;
    }

    @Nullable
    public CharSequence getCharSequence() {
        return charSequence;
    }

    public int getSelection() {
        return selection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyboardInputHistoryState)) return false;

        KeyboardInputHistoryState that = (KeyboardInputHistoryState) o;

        if (selection != that.selection) return false;
        if (charSequence != null ? !charSequence.equals(that.charSequence) : that.charSequence != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = charSequence != null ? charSequence.hashCode() : 0;
        result = 31 * result + selection;
        return result;
    }
}
