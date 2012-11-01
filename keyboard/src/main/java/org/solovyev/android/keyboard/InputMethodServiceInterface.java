package org.solovyev.android.keyboard;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 8:40 PM
 */
public interface InputMethodServiceInterface {

    void sendKey(int keyCode);

    void commitTyped(@NotNull InputConnection inputConnection);

    @NotNull
    StringBuilder getText();

    @NotNull
    AKeyboardView getKeyboardView();

    void handleBackspace();

    void handleShift();

    void handleClose();

    void handleCharacter(int primaryCode, int[] keyCodes);

    boolean isWordSeparator(int primaryCode);

    @NotNull
    InputMethodServiceState getState();

    void updateShiftKeyState(@Nullable EditorInfo attr);
}
