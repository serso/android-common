/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodSubtype;
import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 11:41 PM
 */
public class KeyboardViewAKeyboardView extends KeyboardView implements AndroidKeyboardView<AndroidAKeyboard> {

    public KeyboardViewAKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardViewAKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setOnKeyboardActionListener(@Nonnull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        super.setOnKeyboardActionListener(keyboardActionListener);
    }

    @Override
    public void setKeyboard(@Nonnull AndroidAKeyboard keyboard) {
        super.setKeyboard(keyboard.getKeyboard());
    }

    @Override
    public void close() {
        super.closing();
    }

    @Override
    public void dismiss() {
        super.handleBack();
    }

    @Override
    public void reload() {
        setKeyboard(getKeyboard());
    }

    static final int KEYCODE_OPTIONS = -100;

    @Override
    protected boolean onLongPress(Keyboard.Key key) {
        if (key.codes[0] == Keyboard.KEYCODE_CANCEL) {
            getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
            return true;
        } else {
            return super.onLongPress(key);
        }
    }

    void setSubtypeOnSpaceKey(final InputMethodSubtype subtype) {
        final LatinKeyboard keyboard = (LatinKeyboard)getKeyboard();
        keyboard.setSpaceIcon(getResources().getDrawable(subtype.getIconResId()));
        invalidateAllKeys();
    }
}
