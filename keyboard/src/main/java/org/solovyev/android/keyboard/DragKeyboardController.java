package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.StringUtils;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:42
 */
public abstract class DragKeyboardController extends AbstractKeyboardController<DragAKeyboardDef> {

    protected static String ACTION = "action:";

    protected static String COPY = ACTION + "copy";
    protected static String PASTE = ACTION + "paste";
    protected static String ERASE = ACTION + "erase";
    protected static String CLEAR = ACTION + "clear";

    protected static String LEFT = ACTION + "left";
    protected static String RIGHT = ACTION + "right";

    @NotNull
    @Override
    protected AKeyboardView<DragAKeyboardDef> createKeyboardView0(@NotNull Context context) {
        return new DragAKeyboardView(this, getInputMethodService());
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

    @Override
    public void onKey(int primaryCode, @Nullable int[] keyCodes) {
    }

    @Override
    public void onText(@Nullable CharSequence text) {
        if (!StringUtils.isEmpty(text)) {
            if (COPY.equals(text)) {
                getKeyboardInput().handleCopy();
            } else if (PASTE.equals(text)) {
                getKeyboardInput().handlePaste();
            } else if (ERASE.equals(text)) {
                getKeyboardInput().handleBackspace();
            } else if (CLEAR.equals(text)) {
                getKeyboardInput().handleClear();
            } else if (LEFT.equals(text)) {
                getKeyboardInput().handleCursorLeft();
            } else if (RIGHT.equals(text)) {
                getKeyboardInput().handleCursorRight();
            } else {
                super.onText(text);
            }
        } else {
            super.onText(text);
        }
    }

}
