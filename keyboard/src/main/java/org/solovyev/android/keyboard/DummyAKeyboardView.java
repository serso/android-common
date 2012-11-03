package org.solovyev.android.keyboard;

import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 13:53
 */
public class DummyAKeyboardView<K extends AKeyboardDef> implements AKeyboardView<K> {
    @NotNull
    @Override
    public View getKeyboardView() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setKeyboard(@NotNull K keyboard) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void closing() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean handleBack() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isShifted() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setShifted(boolean shifted) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExtractViewShown() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
