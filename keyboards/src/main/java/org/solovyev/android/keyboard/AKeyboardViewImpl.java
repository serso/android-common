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
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodSubtype;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:44
 */
public class AKeyboardViewImpl<K extends AKeyboard, KV extends View & AndroidKeyboardView<K>> implements AKeyboardView<K> {

    @Nullable
    private KV keyboardView;

    private final int keyboardLayoutResId;

    @Nonnull
    private final AKeyboardController keyboardController;

    @Nonnull
    private final InputMethodService inputMethodService;

    @Nullable
    private KeyboardView.OnKeyboardActionListener keyboardActionListener;


    public AKeyboardViewImpl(int keyboardLayoutResId,
                             @Nonnull AKeyboardController keyboardController,
                             @Nonnull InputMethodService inputMethodService) {
        this.keyboardLayoutResId = keyboardLayoutResId;
        this.keyboardController = keyboardController;
        this.inputMethodService = inputMethodService;
    }


    @Nullable
    protected KeyboardView.OnKeyboardActionListener getKeyboardActionListener() {
        return keyboardActionListener;
    }


    @Override
    public boolean isExtractViewShown() {
        return inputMethodService.isExtractViewShown();
    }

    public void setCandidatesViewShown(boolean shown) {
        inputMethodService.setCandidatesViewShown(shown);
    }

    @Nonnull
    public AKeyboardController getKeyboardController() {
        return keyboardController;
    }

    @Nonnull
    public InputMethodService getInputMethodService() {
        return inputMethodService;
    }


    @Override
    public void setOnKeyboardActionListener(@Nonnull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        this.keyboardActionListener = keyboardActionListener;

        if (this.keyboardView != null) {
            this.keyboardView.setOnKeyboardActionListener(keyboardActionListener);
        }
    }

    @Override
    public void createAndroidKeyboardView(@Nonnull Context context, @Nonnull LayoutInflater layoutInflater) {
        this.keyboardView = (KV) layoutInflater.inflate(keyboardLayoutResId, null);

        final KeyboardView.OnKeyboardActionListener keyboardActionListener = this.getKeyboardActionListener();
        if (keyboardActionListener != null) {
            this.keyboardView.setOnKeyboardActionListener(keyboardActionListener);
        }
    }

    @Override
    public void setKeyboard(@Nonnull K keyboard) {
        if (this.keyboardView != null) {
            this.keyboardView.setKeyboard(keyboard);
        }
    }

    @Override
    public void close() {
        if (this.keyboardView != null) {
            this.keyboardView.close();
        }
    }

    @Override
    public void setSubtypeOnSpaceKey(@Nonnull InputMethodSubtype subtype) {
    }

    @Override
    public void dismiss() {
        if (this.keyboardView != null) {
            this.keyboardView.dismiss();
        }
    }

    @Nonnull
    public View getAndroidKeyboardView() {
        assert keyboardView != null;
        return keyboardView;
    }

    @Override
    public void reloadAndroidKeyboardView() {
        if (this.keyboardView != null) {
            this.keyboardView.reload();
        }
    }

}
