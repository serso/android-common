package org.solovyev.android.keyboard;

import android.view.View;
import android.view.inputmethod.CompletionInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:02 PM
 */
public interface AKeyboardViewWithSuggestions<K extends AKeyboardDef> extends AKeyboardView<K> {

    void setCandidatesViewShown(boolean shown);

    void setSuggestions(@NotNull List<String> suggestions, boolean completions, boolean typedWordValid);

    void setCompletions(@NotNull List<CompletionInfo> completions);

    @NotNull
    List<CompletionInfo> getCompletions();

    void clearCandidateView();

    View onCreateCandidatesView();

}
