package org.solovyev.android.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 11:19 PM
 */
public abstract class AbstractAKeyboardView<K extends AKeyboardDef> implements AKeyboardView<K> {

    @NotNull
    private final AKeyboardController keyboardController;

    @NotNull
    private final InputMethodService inputMethodService;

    @Nullable
    private KeyboardView.OnKeyboardActionListener keyboardActionListener;

    protected AbstractAKeyboardView(@NotNull AKeyboardController keyboardController, @NotNull InputMethodService inputMethodService) {
        this.keyboardController = keyboardController;
        this.inputMethodService = inputMethodService;
    }

    @Override
    public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        this.keyboardActionListener = keyboardActionListener;
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

    @NotNull
    public AKeyboardController getKeyboardController() {
        return keyboardController;
    }

    @NotNull
    public InputMethodService getInputMethodService() {
        return inputMethodService;
    }
}
