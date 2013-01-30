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

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:42
 */
public abstract class DragKeyboardController extends AbstractAndroidKeyboardController<DragAKeyboard> {

    @NotNull
    @Override
    protected AKeyboardViewWithSuggestions<DragAKeyboard> createKeyboardView0(@NotNull Context context) {
        return new AKeyboardViewWithSuggestionsImpl<DragAKeyboard, DragAndroidKeyboardView>(R.layout.drag_keyboard, this, getInputMethodService());
    }

    @NotNull
    @Override
    protected AKeyboardControllerState<DragAKeyboard> onInitializeInterface0(@NotNull InputMethodService inputMethodService) {
        return AKeyboardControllerStateImpl.newDefaultState(createKeyboardDef(inputMethodService));
    }

    protected abstract DragAKeyboard createKeyboardDef(@NotNull Context context);

    @NotNull
    @Override
    public AKeyboardControllerState<DragAKeyboard> onStartInput0(@NotNull EditorInfo attribute, boolean restarting) {
        return getState();
    }

    @NotNull
    @Override
    protected AKeyboardConfiguration onCreate0(@NotNull Context context) {
        return new AKeyboardConfigurationImpl(context.getResources().getString(R.string.word_separators));
    }
}
