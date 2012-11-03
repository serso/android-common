package org.solovyev.android.keyboard;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:11 PM
 */
public abstract class AbstractAndroidKeyboardController extends AbstractKeyboardController<AndroidAKeyboardDef> {

    @Override
    public void onStartInput(@NotNull EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        updateCandidates();

        getKeyboardView().setCompletions(Collections.<CompletionInfo>emptyList());
    }

    @NotNull
    @Override
    public AKeyboardViewWithSuggestions getKeyboardView() {
        return (AKeyboardViewWithSuggestions) super.getKeyboardView();
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();

        updateCandidates();
    }

    @Override
    public boolean handleBackspace() {
        boolean changed = super.handleBackspace();

        if (changed) {
            updateCandidates();
        }

        return changed;
    }

    @NotNull
    @Override
    protected abstract AKeyboardViewWithSuggestions createKeyboardView0(@NotNull Context context);

    public void setSuggestions(@NotNull List<String> suggestions,
                               boolean completions,
                               boolean typedWordValid) {
        final AKeyboardViewWithSuggestions keyboardView = getKeyboardView();

        if (suggestions.size() > 0) {
            keyboardView.setCandidatesViewShown(true);
        } else if (keyboardView.isExtractViewShown()) {
            keyboardView.setCandidatesViewShown(true);
        }

        keyboardView.setSuggestions(suggestions, completions, typedWordValid);
    }

    @Override
    public void onDisplayCompletions(@Nullable CompletionInfo[] completions) {
        super.onDisplayCompletions(completions);

        if (getState().isCompletion()) {

            if (completions == null) {
                setSuggestions(Collections.<String>emptyList(), false, false);
            } else {
                final List<String> suggestions = new ArrayList<String>();
                for (CompletionInfo completion : Arrays.asList(completions)) {
                    if (completion != null) {
                        suggestions.add(completion.getText().toString());
                    }
                }

                setSuggestions(suggestions, true, true);
            }
        }
    }

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    protected void updateCandidates() {
        if (!getState().isCompletion()) {
            final CharSequence text = getKeyboardInput().getTypedText();
            if (!StringUtils.isEmpty(text)) {
                final List<String> list = new ArrayList<String>();
                list.add(text.toString());
                setSuggestions(list, true, true);
            } else {
                setSuggestions(Collections.<String>emptyList(), false, false);
            }
        }
    }

    @Override
    public void handleClose() {
        super.handleClose();

        updateCandidates();
    }

    @Override
    public void pickSuggestionManually(int index) {
        super.pickSuggestionManually(index);

        final AKeyboardViewWithSuggestions keyboardView = getKeyboardView();
        final AKeyboardInput keyboardInput = getKeyboardInput();

        final List<CompletionInfo> completions = keyboardView.getCompletions();

        final CharSequence text = keyboardInput.getTypedText();

        if (getState().isCompletion() && index >= 0 && index < completions.size()) {
            final CompletionInfo ci = completions.get(index);
            keyboardInput.commitCompletion(ci);
            keyboardView.clearCandidateView();
            updateShiftKeyState(keyboardInput.getCurrentInputEditorInfo());
        } else if (!StringUtils.isEmpty(text)) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
            keyboardInput.commitTyped();
        }
    }

    @Override
    public View onCreateCandidatesView() {
        return this.getKeyboardView().onCreateCandidatesView();
    }
}
