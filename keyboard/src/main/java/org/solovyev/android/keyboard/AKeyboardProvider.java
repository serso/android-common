package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 8:07 PM
 */
public interface AKeyboardProvider {

    <I extends InputMethodService & InputMethodServiceInterface> void onInitializeInterface(@NotNull I inputMethodService);

    @NotNull
    AKeyboard getCurrentKeyboard();

    @NotNull
    InputMethodServiceState onStartInput(@NotNull EditorInfo attribute, boolean restarting);

    void onFinishInput();

    void onKey(int primaryCode, int[] keyCodes);

    void onCreate(@NotNull Context context);
}
