package org.solovyev.android.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 10:01 PM
 */
public interface AKeyboardView {

    @NotNull
    View getKeyboardView();

    void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener);

    void setKeyboard(@NotNull Keyboard keyboard);

    void closing();

    void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype);

    boolean handleBack();

    boolean isShifted();

    void setShifted(boolean shifted);

	boolean isExtractViewShown();

	void setCandidatesViewShown(boolean shown);

	void setSuggestions(@NotNull List<String> suggestions, boolean completions, boolean typedWordValid);

	void setCompletions(@NotNull List<CompletionInfo> completions);

	@NotNull
	List<CompletionInfo> getCompletions();

	void clearCandidateView();

	View onCreateCandidatesView();
}
