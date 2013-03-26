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

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodSubtype;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 10:04 PM
 */
public class AKeyboardViewWithSuggestionsImpl<K extends AKeyboard, KV extends View & AndroidKeyboardView<K>> extends AKeyboardViewImpl<K, KV> implements AKeyboardViewWithSuggestions<K> {

	@Nullable
	private CandidateView candidateView;

	@Nonnull
	private List<CompletionInfo> completions = Collections.emptyList();

	public AKeyboardViewWithSuggestionsImpl(int keyboardLayoutResId,
                                            @Nonnull AKeyboardController keyboardController,
                                            @Nonnull InputMethodService inputMethodService) {
		super(keyboardLayoutResId,keyboardController, inputMethodService);
	}

    @Override
    public void setSubtypeOnSpaceKey(@Nonnull InputMethodSubtype subtype) {
        super.setSubtypeOnSpaceKey(subtype);

		final View keyboardView = getAndroidKeyboardView();
        if ( keyboardView instanceof KeyboardViewAKeyboardView ) {
            // todo serso: refactor
            ((KeyboardViewAKeyboardView) keyboardView).setSubtypeOnSpaceKey(subtype);
        }
    }

    @Override
	public void setSuggestions(@Nonnull List<String> suggestions, boolean completions, boolean typedWordValid) {
		if (candidateView != null) {
			candidateView.setSuggestions(suggestions, completions, typedWordValid);
		}
	}

	@Override
	public void setCompletions(@Nonnull List<CompletionInfo> completions) {
		this.completions = completions;
	}

	@Nonnull
	@Override
	public List<CompletionInfo> getCompletions() {
		return this.completions;
	}

	@Override
	public void clearCandidateView() {
		if (candidateView != null) {
			candidateView.clear();
		}
	}

	@Override
	public View onCreateCandidatesView() {
		candidateView = new CandidateView(getInputMethodService());
		candidateView.setKeyboardController(getKeyboardController());
		return candidateView;
	}
}
