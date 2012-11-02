package org.solovyev.android.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:47
 */
public class DragAKeyboardView extends AbstractAKeyboardView {

	@Override
	public void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setSuggestions(@NotNull List<String> suggestions, boolean completions, boolean typedWordValid) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setCompletions(@NotNull List<CompletionInfo> completions) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@NotNull
	@Override
	public List<CompletionInfo> getCompletions() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void clearCandidateView() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public View onCreateCandidatesView() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public DragAKeyboardView(@NotNull KeyboardView keyboardView, @NotNull AKeyboardController keyboardController, @NotNull InputMethodService inputMethodService) {
		super(keyboardView, keyboardController, inputMethodService);
	}
}
