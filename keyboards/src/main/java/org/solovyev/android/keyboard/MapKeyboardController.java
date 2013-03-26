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
import android.inputmethodservice.InputMethodService;
import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 8:18 PM
 */
public abstract class MapKeyboardController extends AbstractKeyboardController {

	@Nonnull
	private final Map<String, AKeyboard> keyboards = new HashMap<String, AKeyboard>();

    @Nonnull
    @Override
    protected AKeyboardControllerState onInitializeInterface0(@Nonnull InputMethodService inputMethodService) {
        synchronized (this.keyboards) {

            this.keyboards.clear();

            final List<AKeyboard> keyboards = createKeyboard(inputMethodService);
            for (AKeyboard keyboard : keyboards) {
                this.keyboards.put(keyboard.getKeyboardId(), keyboard);
            }

            return AKeyboardControllerStateImpl.newDefaultState(this.keyboards.get(getDefaultKeyboardId()));
        }
    }

    @Nonnull
	protected abstract List<AKeyboard> createKeyboard(@Nonnull Context context);

	@Nonnull
	protected abstract String getDefaultKeyboardId();
}
