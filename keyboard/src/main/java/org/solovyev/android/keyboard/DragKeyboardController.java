package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:42
 */
public abstract class DragKeyboardController extends AbstractAndroidKeyboardController<DragAKeyboardDef> {

    protected static String ACTION = "action:";

    protected static String ENTER = ACTION + (int)'\n';


    protected static String COPY = ACTION + KEYCODE_COPY;
    protected static String PASTE = ACTION + KEYCODE_PASTE;

    protected static String DELETE = ACTION + Keyboard.KEYCODE_DELETE;
    protected static String CLEAR = ACTION + KEYCODE_CLEAR;

    protected static String LEFT = ACTION + KEYCODE_CURSOR_LEFT;
    protected static String RIGHT = ACTION + KEYCODE_CURSOR_RIGHT;

    @NotNull
    @Override
    protected AKeyboardViewWithSuggestions<DragAKeyboardDef> createKeyboardView0(@NotNull Context context) {
        return new AKeyboardViewWithSuggestionsImpl<DragAKeyboardDef, DragAndroidKeyboardView>(R.layout.drag_keyboard, this, getInputMethodService());
    }

    @NotNull
    @Override
    protected AKeyboardControllerState<DragAKeyboardDef> onInitializeInterface0(@NotNull InputMethodService inputMethodService) {
        return AKeyboardControllerStateImpl.newDefaultState(new AKeyboardImpl<DragAKeyboardDef>("drag_keyboard", new DragAKeyboardDef(createKeyboardDef(inputMethodService))));
    }

    protected abstract DragAKeyboardDef.KeyboardDef createKeyboardDef(@NotNull Context context);

    @NotNull
    @Override
    public AKeyboardControllerState<DragAKeyboardDef> onStartInput0(@NotNull EditorInfo attribute, boolean restarting) {
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
