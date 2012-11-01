package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 8:18 PM
 */
public abstract class MapKeyboardProvider implements AKeyboardProvider {

    @NotNull
    private final Map<String, AKeyboard> keyboards = new HashMap<String, AKeyboard>();

    @NotNull
    private AKeyboard currentKeyboard;

    private boolean initialized = false;

    @NotNull
    private InputMethodService inputMethodService;

    @Override
    public <I extends InputMethodService & InputMethodServiceInterface> void onInitializeInterface(@NotNull I inputMethodService) {
        this.inputMethodService = inputMethodService;

        if (!initialized) {
            final List<AKeyboard> keyboards = createKeyboard(inputMethodService);
            for (AKeyboard keyboard : keyboards) {
                this.keyboards.put(keyboard.getKeyboardId(), keyboard);
            }

            currentKeyboard = this.keyboards.get(getDefaultKeyboardId());

            initialized = true;
        }
    }

    @NotNull
    protected InputMethodService getInputMethodService() {
        return inputMethodService;
    }

    @NotNull
    protected InputMethodServiceInterface getInputMethodServiceInterface() {
        return (InputMethodServiceInterface)inputMethodService;
    }


    @NotNull
    @Override
    public AKeyboard getCurrentKeyboard() {
        return currentKeyboard;
    }

    protected void setCurrentKeyboard(@NotNull String currentKeyboardId) {
        this.currentKeyboard = this.keyboards.get(currentKeyboardId);
    }

    @NotNull
    protected abstract List<AKeyboard> createKeyboard(@NotNull Context context);

    @NotNull
    protected abstract String getDefaultKeyboardId();
}
