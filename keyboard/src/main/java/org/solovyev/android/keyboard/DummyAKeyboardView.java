package org.solovyev.android.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 13:53
 */
public class DummyAKeyboardView implements AKeyboardView {
	@NotNull
	@Override
	public View getKeyboardView() {
		return null;
	}

	@Override
	public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
	}

	@Override
	public void setKeyboard(@NotNull Keyboard keyboard) {
	}

	@Override
	public void closing() {
	}

	@Override
	public void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype) {
	}

	@Override
	public boolean handleBack() {
		return false;
	}

	@Override
	public boolean isShifted() {
		return false;
	}

	@Override
	public void setShifted(boolean shifted) {
	}

	@Override
	public boolean isExtractViewShown() {
		return false;
	}

	@Override
	public void setCandidatesViewShown(boolean shown) {
	}

	@Override
	public void setSuggestions(@NotNull List<String> suggestions, boolean completions, boolean typedWordValid) {
	}

	@Override
	public void setCompletions(@NotNull List<CompletionInfo> completions) {
	}

	@NotNull
	@Override
	public List<CompletionInfo> getCompletions() {
		return null;
	}

	@Override
	public void clearCandidateView() {
	}

	@Override
	public View onCreateCandidatesView() {
		return null;
	}
}
