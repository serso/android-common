/*
 * Copyright (C) 2008-2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.solovyev.android.keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Example of writing an input method for a soft keyboard.  This code is
 * focused on simplicity over completeness, so it should in no way be considered
 * to be a complete soft keyboard implementation.  Its purpose is to provide
 * a basic example for how you would get started writing an input method, to
 * be fleshed out as appropriate.
 */
public abstract class AbstractInputMethodService extends InputMethodService {

    /*
    **********************************************************************
    *
    *                           CONSTANTS
    *
    **********************************************************************
    */
    private static final boolean DEBUG = false;

    /*
    **********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */

    @NotNull
    private final AKeyboardController keyboardController;

    protected AbstractInputMethodService(@NotNull AKeyboardController keyboardController) {
        this.keyboardController = keyboardController;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        keyboardController.onCreate(this);
    }

    @Override
    public void onInitializeInterface() {
        keyboardController.onInitializeInterface(this);
    }

    @Override
    public View onCreateInputView() {
        return keyboardController.createKeyboardView(this,  getLayoutInflater()).getAndroidKeyboardView();
    }

    @Override
    public View onCreateCandidatesView() {
        return keyboardController.onCreateCandidatesView();
    }

    @Override
    public void onStartInput(@NotNull EditorInfo attribute,
                             boolean restarting) {
        super.onStartInput(attribute, restarting);

        this.keyboardController.onStartInput(attribute, restarting);
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();

        // Clear current composing text and candidates.

        // We only hide the candidates window when finishing input on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);

        keyboardController.onFinishInput();
	}

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);

		keyboardController.onStartInputView(attribute, restarting);
    }

    @Override
    public void onCurrentInputMethodSubtypeChanged(InputMethodSubtype subtype) {
		keyboardController.onCurrentInputMethodSubtypeChanged(subtype);
	}

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                  int newSelStart, int newSelEnd,
                                  int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);

		keyboardController.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);

	}

    @Override
    public void onDisplayCompletions(@Nullable CompletionInfo[] completions) {
        keyboardController.onDisplayCompletions(completions);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean consumed = keyboardController.onKeyDown(keyCode, event);
        if ( !consumed ) {
            return super.onKeyDown(keyCode, event);
        } else {
            return consumed;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean consumed = keyboardController.onKeyUp(keyCode, event);
        if ( !consumed ) {
            return super.onKeyUp(keyCode, event);
        } else {
            return consumed;
        }
    }


}
