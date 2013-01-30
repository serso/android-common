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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

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
