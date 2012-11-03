package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodSubtype;
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
    public AndroidAKeyboardView getKeyboardView() {
        return (AndroidAKeyboardView) super.getKeyboardView();
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
    protected abstract AndroidAKeyboardView createKeyboardView0(@NotNull Context context, @NotNull LayoutInflater layoutInflater);

    @NotNull
    @Override
    protected AKeyboardView<AndroidAKeyboardDef> createDefaultKeyboardView0() {
        return new AndroidAKeyboardView() {
            @Override
            public void setCandidatesViewShown(boolean shown) {
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

            @NotNull
            @Override
            public View getKeyboardView() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setKeyboard(@NotNull AndroidAKeyboardDef keyboard) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void closing() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean handleBack() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean isShifted() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setShifted(boolean shifted) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean isExtractViewShown() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    public void setSuggestions(@NotNull List<String> suggestions,
                               boolean completions,
                               boolean typedWordValid) {
        final AndroidAKeyboardView keyboardView = getKeyboardView();

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

        final AndroidAKeyboardView keyboardView = getKeyboardView();
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
