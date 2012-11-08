package org.solovyev.android.keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	@NotNull
	private List<CompletionInfo> completions = Collections.emptyList();

	public AKeyboardViewWithSuggestionsImpl(int keyboardLayoutResId,
                                            @NotNull AKeyboardController keyboardController,
                                            @NotNull InputMethodService inputMethodService) {
		super(keyboardLayoutResId,keyboardController, inputMethodService);
	}

    @Override
    public void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype) {
        super.setSubtypeOnSpaceKey(subtype);

		final View keyboardView = getAndroidKeyboardView();
        if ( keyboardView instanceof KeyboardViewAKeyboardView ) {
            // todo serso: refactor
            ((KeyboardViewAKeyboardView) keyboardView).setSubtypeOnSpaceKey(subtype);
        }
    }

    @Override
	public void setSuggestions(@NotNull List<String> suggestions, boolean completions, boolean typedWordValid) {
		if (candidateView != null) {
			candidateView.setSuggestions(suggestions, completions, typedWordValid);
		}
	}

	@Override
	public void setCompletions(@NotNull List<CompletionInfo> completions) {
		this.completions = completions;
	}

	@NotNull
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
