package org.solovyev.android.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
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
public class AKeyboardViewImpl extends AbstractAndroidAKeyboardView {

	@Nullable
	private CandidateView candidateView;

	@NotNull
	private List<CompletionInfo> completions = Collections.emptyList();

	public AKeyboardViewImpl(@NotNull int keyboardLayoutResId,
							 @NotNull AKeyboardController keyboardController,
							 @NotNull InputMethodService inputMethodService,
							 @Nullable CandidateView candidateView) {
		super(keyboardLayoutResId,keyboardController, inputMethodService);
		this.candidateView = candidateView;
	}

    @Override
    public void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype) {
		final KeyboardView keyboardView = getAndroidKeyboardView();
        if ( keyboardView instanceof LatinKeyboardView ) {
            // todo serso: refactor
            ((LatinKeyboardView) keyboardView).setSubtypeOnSpaceKey(subtype);
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
