package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:42
 */
public abstract class DragKeyboardController extends AbstractAndroidKeyboardController<DragAKeyboard> {

    @NotNull
    @Override
    protected AKeyboardViewWithSuggestions<DragAKeyboard> createKeyboardView0(@NotNull Context context) {
        return new AKeyboardViewWithSuggestionsImpl<DragAKeyboard, DragAndroidKeyboardView>(R.layout.drag_keyboard, this, getInputMethodService());
    }

    @NotNull
    @Override
    protected AKeyboardControllerState<DragAKeyboard> onInitializeInterface0(@NotNull InputMethodService inputMethodService) {
        return AKeyboardControllerStateImpl.newDefaultState(createKeyboardDef(inputMethodService));
    }

    protected abstract DragAKeyboard createKeyboardDef(@NotNull Context context);

    @NotNull
    @Override
    public AKeyboardControllerState<DragAKeyboard> onStartInput0(@NotNull EditorInfo attribute, boolean restarting) {
        return getState();
    }

    @NotNull
    @Override
    protected AKeyboardConfiguration onCreate0(@NotNull Context context) {
        return new AKeyboardConfigurationImpl(context.getResources().getString(R.string.word_separators));
    }

    @Override
    protected void handleShift() {
    }
}
